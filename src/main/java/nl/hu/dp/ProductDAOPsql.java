package nl.hu.dp;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {

    private Connection conn;
    private OVChipkaartDAOPsql ovcdao;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public ProductDAOPsql(Connection conn, OVChipkaartDAOPsql ovcdao) {
        this.conn = conn;
        this.ovcdao = ovcdao;
    }

    public void setOvcdao(OVChipkaartDAOPsql ovcdao) {
        this.ovcdao = ovcdao;
    }

    @Override
    public boolean save(Product product) {
        String SQL = "INSERT INTO product VALUES (?, ?, ?, ?)";
        String SQL2 = "INSERT INTO ov_chipkaart_product VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setInt(1, product.getProduct_nummer());
            prestat.setString(2, product.getNaam());
            prestat.setString(3, product.getBeschrijving());
            prestat.setFloat(4, product.getPrijs());

            prestat.executeUpdate();


            for (OVChipkaart o : product.getOVChipkaarten()) {
                PreparedStatement prestat2 = conn.prepareStatement(SQL2);
                prestat2.setInt(1, o.getKaart_nummer());
                prestat2.setInt(2, product.getProduct_nummer());
                prestat2.setString(3, "actief");
                prestat2.setDate(4, Date.valueOf(LocalDate.now()));

                prestat2.executeUpdate();
            }
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Product product) {
//       De update functie is niet heel erg uitgebreid of complex, maar als standaard heb ik genomen dat de prijs van het product met 5 word verhoogd

        String SQL =  "UPDATE product SET prijs = prijs + 5 WHERE product_nummer = ?";

//        Ik nam aan dat "last update" in de koppel tabel stond voor wanneer een product of ovchipkaart voor het laatst is veranderd/geupdate
//        En ik verander de prijs van een product, dus dan moet in de koppel tabel de "last update" upgedate worden op de plekken met dat zelfde product nummer

        String SQL2 = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        String SQL3 = "INSERT INTO ov_chipkaart_product VALUES (?, ?, ?, ?)";


        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);
            PreparedStatement prestat2 = conn.prepareStatement(SQL2);

            prestat.setInt(1, product.getProduct_nummer());
            prestat2.setInt(1, product.getProduct_nummer());

            prestat.executeUpdate();
            prestat2.executeUpdate();

            for (OVChipkaart o : product.getOVChipkaarten()) {
                PreparedStatement prestat3 = conn.prepareStatement(SQL3);
                prestat3.setInt(1, o.getKaart_nummer());
                prestat3.setInt(2, product.getProduct_nummer());
                prestat3.setString(3, "actief");
                prestat3.setDate(4, Date.valueOf(LocalDate.now()));

                prestat3.executeUpdate();
            }

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Product product) {
        String SQL = "DELETE FROM product WHERE product_nummer = ?";
        String SQL2 = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);
            PreparedStatement prestat2 = conn.prepareStatement(SQL2);


            prestat.setInt(1, product.getProduct_nummer());
            prestat2.setInt(1, product.getProduct_nummer());

            prestat2.executeUpdate();
            prestat.executeUpdate();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        String SQL = "SELECT ov_chipkaart_product.kaart_nummer, product.product_nummer, product.naam, product.beschrijving, product.prijs " +
                "FROM product " +
                "JOIN ov_chipkaart_product " +
                "ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                "WHERE ov_chipkaart_product.kaart_nummer = ? " +
                "ORDER BY kaart_nummer, product_nummer";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setInt(1, ovChipkaart.getKaart_nummer());

            ResultSet rs = prestat.executeQuery();

            List<Product> producten = new ArrayList<>();

            while (rs.next()) {
                int prnr = rs.getInt("product_nummer");
                String nm = rs.getString("naam");
                String besch = rs.getString("beschrijving");
                Float prijs = rs.getFloat("prijs");

                Product pr = new Product(prnr, nm, besch, prijs);
                pr.addOVChipkaart(ovChipkaart);
                producten.add(pr);
            }

            return producten;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<Product> findAll(){

//        Hier veranderd van "SELECT * FROM product;" naar deze query met JOIN zodat je ook de ovchipkaarten ook op haalt en toevoegd aan de producten
//        Hetzelfde in OVChipkaartDAOPsql

        String SQL = "SELECT ov_chipkaart_product.kaart_nummer, ov_chipkaart.geldig_tot, ov_chipkaart.klasse, ov_chipkaart.saldo, ov_chipkaart.reiziger_id, product.product_nummer, product.naam, product.beschrijving, product.prijs " +
                "FROM product " +
                "JOIN ov_chipkaart_product " +
                "ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                "JOIN ov_chipkaart " +
                "ON ov_chipkaart_product.kaart_nummer = ov_chipkaart.kaart_nummer " +
                "ORDER BY kaart_nummer, product_nummer;";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            ResultSet rs = prestat.executeQuery();

            List<Product> producten = new ArrayList<>();

            while(rs.next()) {
                int prnr = rs.getInt("product_nummer");
                String nm = rs.getString("naam");
                String besch = rs.getString("beschrijving");
                Float prijs = rs.getFloat("prijs");

                int knm = rs.getInt("kaart_nummer");
                Date geldigTot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                float saldo = rs.getFloat("saldo");
                int rid = rs.getInt("reiziger_id");

                OVChipkaart ov = new OVChipkaart(knm, geldigTot, klasse, saldo, rid);

                Product pr = new Product(prnr, nm, besch, prijs);


                for(Product p : producten){
                    if(p.getProduct_nummer() == pr.getProduct_nummer()){
                        p.addOVChipkaart(ov);
                        break;
                    }
                }
                pr.addOVChipkaart(ov);
                producten.add(pr);
            }

            return producten;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}