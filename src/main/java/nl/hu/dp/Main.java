package nl.hu.dp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        System.out.println("Test");

        ReizigerDAOPsql rdp = new ReizigerDAOPsql(getConnection());
        AdresDAOsql adp = new AdresDAOsql(getConnection());
        OVChipkaartDAOPsql odp = new OVChipkaartDAOPsql(getConnection());
        ProductDAOPsql pdp = new ProductDAOPsql(getConnection());

        rdp.setAdao(adp);
        adp.setRdao(rdp);
        rdp.setOdao(odp);
        pdp.setOdoa(odp);
        odp.setPdao(pdp);

        testReizigerDAO(rdp);
        testAdresDAO(rdp);
        testOVChipkaartDAO(rdp);
        testProductDAO(pdp);
    }

    private static void testProductDAO(ProductDAO pdao) throws SQLException {
        ArrayList<Product> producten = new ArrayList<Product>();
        System.out.println("\n---------- Test ProductenDao -------------");
        // Current state van tabel
        producten = (ArrayList) pdao.findAll();
        System.out.println("[Testing findAll()] Gives following Producten: \n");
        for (Product p : producten) {
            System.out.println(p.toString());
        }
        System.out.println();

        // Test save + findByID
        System.out.println("\nTest save");
        Product p7 = new Product(7, "week kaart", "Volledige week gratis ov", 4.55);
        pdao.save(p7);
        System.out.println(pdao.findById(p7.getProductNummer()).toString());

        // Showing all results again
        producten = (ArrayList) pdao.findAll();
        System.out.println("[Testing findAll()] Gives following Producten: \n");
        for (Product p : producten) {
            System.out.println(p.toString());
        }
        System.out.println();

        // Test findByOVChipkaart + toString OVChipkaart
        OVChipkaart bestaandeKaart = (new OVChipkaart(35283, java.sql.Date.valueOf("2018-5-31"), 2, 25.50));
        bestaandeKaart.setProducten(pdao.findByOVChipkaart(bestaandeKaart));
        System.out.println(bestaandeKaart.toString());

        // Test update
        System.out.println("\nTest update");
        p7.addOVChipkaart(bestaandeKaart);
        pdao.update(p7);
        System.out.println(p7.toString() + "\n met ovkaarten:");
        // pak hier p7 uit de database om te laten zien dat hij nu een relatie in de
        // database heeft met de volgende OVCHIPkaarten
        for (OVChipkaart ovkaart : pdao.findById(p7.getProductNummer()).getOvChipkaarten()) {
            System.out.println("\t" + ovkaart.toString());
        }

        // test Delete:
        pdao.delete(p7);

        // Showing all results again
        producten = (ArrayList) pdao.findAll();
        System.out.println("\n[Testing findAll()] Gives following Producten: \n");
        for (Product p : producten) {
            System.out.println(p.toString());
        }
        System.out.println();

    }

    private static void testOVChipkaartDAO(ReizigerDAO rdao) throws SQLException {
        ArrayList<OVChipkaart> ovChipkaartenAika = new ArrayList<OVChipkaart>();

        System.out.println("\n---------- Test OVChipkaartDAOsql -------------");
        // Current state van tabel
        List<OVChipkaart> oVChipkaarten = rdao.getOdao().findAll();
        oVChipkaarten = (ArrayList) oVChipkaarten;
        System.out.println("[Testing findAll()] Gives following OVChipkaarten: \n");
        for (OVChipkaart o : oVChipkaarten) {
            System.out.println(o.toString());
        }
        System.out.println();

        // Test save
        System.out.println("\nTest save");
        String geldig_tot = "2023-01-01";
        String gbdatum = "1981-03-14";
        Reiziger aika = new Reiziger(6, "Aika", "", "Boers", java.sql.Date.valueOf(gbdatum));
        OVChipkaart ovChipkaart1 = new OVChipkaart(12345, java.sql.Date.valueOf(geldig_tot), 3, 33.2);

        ovChipkaart1.setReiziger(aika);
        ovChipkaartenAika.add(ovChipkaart1);
        aika.setoVChipkaarten(ovChipkaartenAika);

        rdao.save(aika);

        System.out.println(aika.toString() + "\n");

        // Test findById
        System.out.println("\nTest FindByID");
        OVChipkaart ovChipkaart2 = rdao.getOdao().findById(12345);
        System.out.println(ovChipkaart2.toString());

        // also add this ovchipkaart to aika
        // For some reason by adding an object to this list, object: Aika will add it to
        // her list as well
        // Even though function aika.setoVChipkaarten isn't being called?
        // if you remove this the test result will show only 1 ovchipkaart for aika
        ovChipkaartenAika.add(ovChipkaart2);

        // Current state van tabel
        oVChipkaarten = rdao.getOdao().findAll();
        System.out.println("\n[Testing findAll()] Gives following OVChipkaarten:");
        for (OVChipkaart o : oVChipkaarten) {
            System.out.println(o);
        }
        System.out.println();

        // Show all ovchipkaarten owned by aika
        System.out.println(aika.toString());

        // Test Delete
        System.out.println("\nTest Delete:");
        rdao.getOdao().delete(ovChipkaart2);
        rdao.getOdao().delete(ovChipkaart1);
        rdao.delete(aika);

        // Show all ovchipkaarten owned by aika
        System.out.println(rdao.findById(6));

        // Current state van tabel
        oVChipkaarten = rdao.getOdao().findAll();
        System.out.println("\n[Testing findAll()] Gives following OVChipkaarten:");
        for (OVChipkaart o : oVChipkaarten) {
            System.out.println(o);
        }
        System.out.println();

        // Showing all oVChipKaarten from every person:
        List<Reiziger> reizigers = new ArrayList<>();
        reizigers = rdao.findAll();
        for (Reiziger reiziger : reizigers) {
            System.out.println(reiziger.toString());
        }

        System.out.println("\n---------- Test complete -------------");
    }

    private static void testAdresDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAOSql -------------");

        // Current state van tabel
        List<Adres> adressen = rdao.getAdao().findAll();
        System.out.println("[Testing findAll()] Gives following adresses:");
        for (Adres r : adressen) {
            System.out.println(r);
        }
        System.out.println();

        // Test Delete Database
        String gbdatum = "1981-03-14";
        Reiziger aika = new Reiziger(22, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.delete(aika);
        Adres a1 = new Adres(3000, "5683fd", "22a", "Roomstraat", "Helmondus");
        rdao.getAdao().delete(a1);

        // Create + show Relations
        a1.setReiziger(aika);
        aika.setAdres(a1);
        System.out.println("Testing relations" + aika.toString() + "\n");

        // Persist Data
        System.out.println("[Testing insert] \nFirst " + adressen.size() + " adressen \nAfter AdreDAOsql.save() ");
        rdao.save(aika);
        adressen = rdao.getAdao().findAll();
        System.out.println("\n" + adressen.size() + " adressen\n");

        // Test Update
        System.out.println("\n[Testing update]: Helmondus vervangen door Geldropus \n");
        System.out.println("Before update: " + a1.toString() + "\n");
        a1.setWoonplaats("Geldropus \n");
        rdao.getAdao().update(a1);
        System.out.println("After update:" + rdao.getAdao().findById(a1.getAdres_id()).toString() + "\n");

        // Test Delete
        rdao.getAdao().delete(a1);
        rdao.delete(aika);

        // Current state van tabel
        adressen = rdao.getAdao().findAll();
        System.out.println("[Testing findAll()] Gives following adresses: \n");
        for (Adres r : adressen) {
            System.out.println(r);
        }

        System.out.println("\n---------- Test Complete--------------");
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAOSql -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Testing findAll()] Gives following reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Testing insert] \nFirst " + reizigers.size() + " reizigers, \nAfter ReizigerDAOSql.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println("\n" + reizigers.size() + " reizigers\n");

        // Test update
        System.out.println("\n[Testing update]: check last name of person with ID: 77");
        sietske.setAchternaam("Zus");
        rdao.update(sietske);
        reizigers = rdao.findAll();
        for (Reiziger r : reizigers) {
            System.out.println(r.toString());
        }

        // Test delete
        System.out.println("\n[Testing delete]: check last name of person with ID: 77, oh wait he's gone!");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        for (Reiziger r : reizigers) {
            System.out.println(r.toString());
        }

        System.out.println("\n---------- Test Complete--------------");
    }

    private static Connection getConnection() throws SQLException {
        if (connection == null) {
            String dbName = "ovchip";
            String uName = "postgres";
            String pWord = "MelissaVoets13";
            String port = ":5432";

            String url = String.format("jdbc:postgresql://localhost:5432/%s?user=%s&password=%s",
                    dbName, uName, pWord);
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    private static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    private static void testConnection() throws SQLException {
        getConnection();
        String query = "SELECT * FROM reiziger;";
        PreparedStatement prepQuery = connection.prepareStatement(query);
        ResultSet set = prepQuery.executeQuery();

        String output = "Alle reizigers: ";
        System.out.println(output);

        while (set != null && set.next()) {
            output = " \t ";
            String id = set.getString("Reiziger_id");
            String voorletter = set.getString("voorletters");
            String tussenvoegsel = set.getString("tussenvoegsel");
            String achternaam = set.getString("achternaam");
            String geboorteDatum = set.getString("geboortedatum");
            if (tussenvoegsel == null) {
                output += String.format(" #%s %s. %s (%s)", id, voorletter, achternaam, geboorteDatum);
            } else {
                output += String.format(" #%s %s. %s %s (%s)", id, voorletter, tussenvoegsel, achternaam,
                        geboorteDatum);
            }
            System.out.println(output);
        }

        closeConnection();
    }
}