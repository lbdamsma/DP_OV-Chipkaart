package nl.hu.dp;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;

    public OVChipkaartDAOPsql getOdoa() {
        return odoa;
    }

    public void setOdoa(OVChipkaartDAOPsql odoa) {
        this.odoa = odoa;
    }

    private OVChipkaartDAOPsql odoa;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Product inProduct) throws SQLException {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?,?,?,?);");
            preparedStatement.setInt(1, inProduct.getProductNummer());
            preparedStatement.setString(2, inProduct.getNaam());
            preparedStatement.setString(3, inProduct.getBeschrijving());
            preparedStatement.setDouble(4, inProduct.getPrijs());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            if (inProduct.getOvChipkaarten() != null && odoa != null) {
                for (OVChipkaart inOvKaart : inProduct.getOvChipkaarten()) {
                    if (odoa.findById(inOvKaart.getKaart_nummer()) == null) {
                        odoa.save(inOvKaart);
                    }
                    PreparedStatement preparedStatement2 = conn.prepareStatement(
                            "INSERT INTO ov_chipkaart_product (product_nummer, kaart_nummer, status, last_update) VALUES (?,?,?,?);");
                    preparedStatement2.setInt(1, inProduct.getProductNummer());
                    preparedStatement2.setInt(2, inOvKaart.getKaart_nummer());
                    preparedStatement2.setString(3, "actief");
                    preparedStatement2.setDate(4, Date.valueOf(LocalDate.now()));
                    preparedStatement2.executeUpdate();
                    preparedStatement2.close();
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Product inProduct) throws SQLException {
        try {
            delete(inProduct);
            save(inProduct);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        try {
            if (findById(product.getProductNummer()).getOvChipkaarten().size() > 0) {
                PreparedStatement preparedStatement1 = conn
                        .prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
                preparedStatement1.setInt(1, product.getProductNummer());
                preparedStatement1.executeUpdate();
                preparedStatement1.close();
            }

            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
            preparedStatement.setInt(1, product.getProductNummer());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        ArrayList<Product> producten = new ArrayList<Product>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT ov_chipkaart_product.kaart_nummer, ov_chipkaart.geldig_tot, ov_chipkaart.klasse, ov_chipkaart.saldo, product.product_nummer, product.naam, product.beschrijving, product.prijs "
                            +
                            "FROM product " +
                            "left JOIN ov_chipkaart_product " +
                            "ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                            "LEFT JOIN ov_chipkaart " +
                            "ON ov_chipkaart_product.kaart_nummer = ov_chipkaart.kaart_nummer " +
                            "ORDER BY product_nummer;");

            ResultSet set = preparedStatement.executeQuery();
            while (set != null && set.next()) {
                Product p = (parseResultSet(set));
                OVChipkaart o = odoa.parseStatement(set);

                if (producten == null || !producten.contains(p)) {
                    producten.add(p);
                }
                if (producten != null) {
                    for (Product inProduct : producten) {
                        if (inProduct.equals(p)) {
                            inProduct.addOVChipkaart(o);
                        }
                    }
                }
            }
            set.close();
            preparedStatement.close();
            return producten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return producten;
    }

    @Override
    public Product findById(int id) throws SQLException {
        Product p = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT ov_chipkaart_product.kaart_nummer, ov_chipkaart.geldig_tot, ov_chipkaart.klasse, ov_chipkaart.saldo, product.product_nummer, product.naam, product.beschrijving, product.prijs "
                            +
                            "FROM product " +
                            "left JOIN ov_chipkaart_product " +
                            "ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                            "left JOIN ov_chipkaart " +
                            "ON ov_chipkaart_product.kaart_nummer = ov_chipkaart.kaart_nummer " +
                            "WHERE product.product_nummer = ? " +
                            "ORDER BY product_nummer;");
            preparedStatement.setInt(1, id);
            ResultSet set = preparedStatement.executeQuery();
            while (set != null && set.next()) {
                OVChipkaart o = odoa.parseStatement(set);
                if (p == null) {
                    p = (parseResultSet(set));
                }
                if (o != null) {
                    p.addOVChipkaart(o);
                }
            }
            set.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return p;
    }

    public Product parseResultSet(ResultSet set) throws SQLException {
        Product product = new Product();
        product.setProductNummer(set.getInt("product_nummer"));
        product.setNaam(set.getString("naam"));
        product.setBeschrijving(set.getString("beschrijving"));
        product.setPrijs(set.getDouble("prijs"));
        return product;
    }

    @Override
    public ArrayList<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        ArrayList<Product> producten = new ArrayList<Product>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT ov_chipkaart_product.kaart_nummer, ov_chipkaart.geldig_tot, ov_chipkaart.klasse, ov_chipkaart.saldo, product.product_nummer, product.naam, product.beschrijving, product.prijs "
                            +
                            "FROM product " +
                            "inner JOIN ov_chipkaart_product " +
                            "ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                            "inner JOIN ov_chipkaart " +
                            "ON ov_chipkaart_product.kaart_nummer = ov_chipkaart.kaart_nummer " +
                            "WHERE ov_chipkaart.kaart_nummer = ?" +
                            "ORDER BY product_nummer;");
            preparedStatement.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet set = preparedStatement.executeQuery();
            while (set != null && set.next()) {
                Product p = (parseResultSet(set));
                OVChipkaart o = odoa.parseStatement(set);

                if (producten == null || !producten.contains(p)) {
                    producten.add(p);
                }
                if (producten != null) {
                    for (Product inProduct : producten) {
                        if (inProduct.equals(p)) {
                            inProduct.addOVChipkaart(o);
                        }
                    }
                }
            }
            set.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return producten;
    }

}