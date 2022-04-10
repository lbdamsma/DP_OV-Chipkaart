package nl.hu.dp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ProductDAOPsql pdao;

    public ProductDAOPsql getPdao() {
        return pdao;
    }

    public void setPdao(ProductDAOPsql pdao) {
        this.pdao = pdao;
    }

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean update(OVChipkaart inOVChipkaart) throws SQLException {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "update ov_chipkaart SET  kaart_nummer = ?, saldo =? , geldig_tot =?, klasse = ?, reiziger_id = ?;");
            preparedStatement.setInt(1, inOVChipkaart.getKaart_nummer());
            preparedStatement.setDouble(2, inOVChipkaart.getSaldo());
            preparedStatement.setDate(3, inOVChipkaart.getGeldig_tot());
            preparedStatement.setInt(4, inOVChipkaart.getKlasse());
            preparedStatement.setInt(5, inOVChipkaart.getReiziger().getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean save(OVChipkaart inOVChipkaart) throws SQLException {
        OVChipkaart ovChipkaart = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO ov_chipkaart (kaart_nummer, saldo, geldig_tot, klasse, reiziger_id) VALUES (?,?,?,?,?);");
            preparedStatement.setInt(1, inOVChipkaart.getKaart_nummer());
            preparedStatement.setDouble(2, inOVChipkaart.getSaldo());
            preparedStatement.setDate(3, inOVChipkaart.getGeldig_tot());
            preparedStatement.setInt(4, inOVChipkaart.getKlasse());
            preparedStatement.setInt(5, inOVChipkaart.getReiziger().getId());
            preparedStatement.executeQuery();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(OVChipkaart inOVChipkaart) throws SQLException {
        OVChipkaart ovChipkaart = null;
        try {
            PreparedStatement preparedStatement1 = conn
                    .prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaartnummer = ?;");
            preparedStatement1.setInt(1, inOVChipkaart.getKaart_nummer());
            preparedStatement1.executeQuery();
            preparedStatement1.close();

            PreparedStatement preparedStatement = conn
                    .prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?;");
            preparedStatement.setInt(1, inOVChipkaart.getKaart_nummer());
            preparedStatement.executeQuery();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        ArrayList<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT ov_chipkaart_product.kaart_nummer, ov_chipkaart.geldig_tot, ov_chipkaart.klasse, ov_chipkaart.saldo, product.product_nummer, product.naam, product.beschrijving, product.prijs "
                            +
                            "FROM ov_chipkaart " +
                            "left JOIN ov_chipkaart_product " +
                            "ON ov_chipkaart_product.kaart_nummer = ov_chipkaart.kaart_nummer " +
                            "LEFT JOIN product " +
                            "ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                            "ORDER BY kaart_nummer;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                OVChipkaart ovChipkaart = parseStatement(resultSet);
                Product prodcut = pdao.parseResultSet(resultSet);
                if (ovChipkaart != null) {
                    if (ovChipkaarten == null || !ovChipkaarten.contains(ovChipkaart)) {
                        ovChipkaarten.add(ovChipkaart);
                    }
                    if (ovChipkaarten != null) {
                        for (OVChipkaart inOvKaarten : ovChipkaarten) {
                            if (inOvKaarten != null && inOvKaarten.equals(ovChipkaart)) {
                                inOvKaarten.addProdcut(prodcut);
                            }
                        }
                    }
                }

            }
            resultSet.close();
            return ovChipkaarten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public OVChipkaart findById(int id) throws SQLException {
        OVChipkaart ovChipkaart = null;
        try {
            PreparedStatement preparedStatement = conn
                    .prepareStatement("SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                ovChipkaart = (parseStatement(resultSet));
            }
            resultSet.close();
            return ovChipkaart;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger r) throws SQLException {
        ArrayList<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();
        try {
            PreparedStatement preparedStatement = conn
                    .prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?;");
            preparedStatement.setInt(1, r.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet != null && resultSet.next()) {
                ovChipkaarten.add(parseStatement(resultSet));
            }
            resultSet.close();
            return ovChipkaarten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public OVChipkaart parseStatement(ResultSet theSet) throws SQLException {
        OVChipkaart o = new OVChipkaart();
        o.setKaart_nummer(theSet.getInt("kaart_nummer"));
        o.setSaldo(theSet.getDouble("saldo"));
        o.setGeldig_tot(theSet.getDate("geldig_tot"));
        o.setKlasse(theSet.getInt("klasse"));
        if (o.getKaart_nummer() == 0) {
            return null;
        }
        return o;
    }
}