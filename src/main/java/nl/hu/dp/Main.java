package nl.hu.dp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        getConnection();
        ReizigerDAOPsql reizigerDAO = new ReizigerDAOPsql(connection);
        AdresDAOsql adresDAO = new AdresDAOsql(connection);
        OVChipkaartDAOPsql ovchipDAO = new OVChipkaartDAOPsql(connection);
        ProductDAOPsql prDAO = new ProductDAOPsql(connection);
        reizigerDAO.setAdao(adresDAO);
        reizigerDAO.setOvcdao(ovchipDAO);
        prDAO.setOvcdao(ovchipDAO);
        testProductDAO(prDAO);
//        delete(prDAO);

        closeConnection();
    }

    private static void getConnection(){
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=MelissaVoets13";
        try {
            Connection conn = DriverManager.getConnection(url);
            connection = conn;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void closeConnection() throws SQLException {
        connection.close();
    }

    private static void testProductDAO(ProductDAOPsql prdao) throws SQLException {
        Product pr1 = new Product(33, "Test", "Test", 10.00F);
        System.out.println("Save [" + pr1 + "] in de Database");
        OVChipkaart ov1 = new OVChipkaart(18326, Date.valueOf("2017-12-31"), 2, 0.00F, 5);
        ov1.addProduct(pr1);

        prdao.save(pr1);

        System.out.println();

        System.out.println("findAll: ");
        List<Product> y = prdao.findAll();
        for (Product p : y) {
            System.out.println(p);
        }
        System.out.println(y.size() + " producten in totaal");

        System.out.println();

        System.out.println("Update: ");
        prdao.update(pr1);
        y = prdao.findAll();
        for (Product p : y) {
            if (p.getProduct_nummer() == 33) {
                System.out.println(p);
            }
        }

        System.out.println();

        prdao.delete(pr1);
        List<Product> producten = prdao.findAll();
        System.out.println("Aantal producten na delete: ");
        System.out.println(producten.size());

        System.out.println();

        System.out.println("findByOvchipkaart: ");
        Date d = Date.valueOf("2018-05-31");
        OVChipkaart test = new OVChipkaart(35283, d, 2, 25.50F, 2);
        List<Product> x = prdao.findByOVChipkaart(test);
        for (Product p : x){
            System.out.println(p);
        }
    }

    private static void delete(ProductDAOPsql prdao) {
        Product pr1 = new Product(33, "Test", "Test", 10.00F);
        prdao.delete(pr1);

    }


}