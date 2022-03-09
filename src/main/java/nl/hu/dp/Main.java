package nl.hu.dp;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class Main {
    private static Connection conn = null;

    private static void createConnection() {
        if(conn == null) {
            String url = "jdbc:postgresql://localhost/ovchip";
            // creates a property object
            Properties props = new Properties();
            // sets the user property on the props
            props.setProperty("user", "postgres");
            // sets the user property on the props
            props.setProperty("password", "MelissaVoets13");
            try {
                // use the props and base url to create a db connection
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeConnection() {
        try {
            if(conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createConnection();
        System.out.println("Alle reizigers: ");
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from reiziger");
            while (rs != null && rs.next()) {
                String name;
                String insertion = rs.getString("tussenvoegsel");
                String initial = rs.getString("voorletters");
                String lastName = rs.getString("achternaam");
                if (insertion != null) {
                    name = String.format("%s %s %s",initial ,insertion, lastName);
                } else {
                    name = String.format("%s %s", initial, lastName);
                }
                System.out.printf("#%s %s (%s)%n", rs.getInt("reiziger_id"), name, rs.getString("geboortedatum"));
            }
            System.out.println(new ReizigerDAOsql(conn).findAll());
            testReizigerDAO(new ReizigerDAOsql(conn));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
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
        Reiziger sietske = new Reiziger();
        sietske.setAchternaam("Broers");
        sietske.setGeboortedatum(Date.valueOf(gbdatum));
        sietske.setVoorletters("S");
        sietske.setTussenvoegsel("");
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }
}