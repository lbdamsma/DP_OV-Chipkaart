package nl.hu.dp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        getConnection();
        ReizigerDAOsql reizigerDAO = new ReizigerDAOsql(connection);
        AdresDAOsql adresDAO = new AdresDAOsql(connection);
        reizigerDAO.setAdao(adresDAO);
        testReizigerDAO(reizigerDAO);
        testAdresDao(adresDAO);
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

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        sietske.setAdres(new Adres(22, "3512PA", "5", "Lange nieuwstraat", "Utrecht", 77));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers");
        System.out.println(rdao.findById(sietske.getId()));

        // Update een gebruiker
        System.out.println("\n[Test] ReizigerDAO.update() geeft de volgende reiziger:");
        rdao.update(sietske);
        System.out.println(rdao.findById(sietske.getId()));

        // Delete een gebruiker
        System.out.print("\n[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Find een reiziger by ID
        System.out.println("[Test] ReizigerDAO.findById() geeft de volgende reiziger:");
        System.out.println(rdao.findById(2));

        // Find een reiziger by geboortedatum
        System.out.println("\n[Test] ReizigerDAO.findByGbdatum() geeft de volgende reiziger:");
        String gbdatum2 = "1998-08-11";
        System.out.println(rdao.findByGbdatum(gbdatum2));
    }

    private static void testAdresDao(AdresDAO adao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }

        // Delete een adres
        System.out.print("\n[Test] Eerst " + adressen.size() + " reizigers, na AdresDAO.delete() ");
        Adres test = new Adres(4, "3817CH", "4", "Arnhemseweg", "Amersfoort", 4);
        adao.delete(test);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // Save een adres
        System.out.print("[Test] Eerst " + adressen.size() + " reizigers, na AdresDAO.save() ");
        adao.save(test);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // Update een adres en find by reiziger
        System.out.println("[Test] AdresDAO.update() geeft het volgende adres:");
        adao.update(test);
        String gbdatum = "2002-12-03";
        Reiziger blank = new Reiziger(4, "F", null, "Memari", java.sql.Date.valueOf(gbdatum));
        System.out.println(adao.findByReiziger(blank));
    }

}