package nl.hu.dp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    Connection inConnection = null;
    private AdresDAO adao;
    private OVChipkaartDAO odao;

    public OVChipkaartDAO getOdao() {
        return odao;
    }

    public void setOdao(OVChipkaartDAO odao) {
        this.odao = odao;
    }

    public AdresDAO getAdao() {
        return adao;
    }

    public void setAdao(AdresDAO adao) {
        this.adao = adao;
    }

    public ReizigerDAOPsql(Connection inConnection) {
        this.inConnection = inConnection;
    }

    @Override
    public boolean save(Reiziger inReiziger) {
        try {
            PreparedStatement statement = this.inConnection.prepareStatement(
                    "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?,?,?,?,?)");
            statement.setInt(1, inReiziger.getId());
            statement.setString(2, inReiziger.getVoorletters());
            statement.setString(3, inReiziger.getTussenvoegsel());
            statement.setString(4, inReiziger.getAchternaam());
            statement.setDate(5, inReiziger.getGeboortedatum());

            statement.executeUpdate();

            if (adao != null && inReiziger.getAdres() != null) {
                adao.save(inReiziger.getAdres());
            }

            if (odao != null && inReiziger.getOVChipkaarten() != null) {
                for (OVChipkaart ovChipkaart : inReiziger.getOVChipkaarten()) {
                    ovChipkaart.setReiziger(inReiziger);
                    odao.save(ovChipkaart);
                }
            }

            statement.close();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public List<Reiziger> findAll() {
        ArrayList<Reiziger> reizigers = new ArrayList<Reiziger>();
        try {
            PreparedStatement statement = this.inConnection.prepareStatement("SELECT * FROM reiziger;");
            ResultSet theSet = statement.executeQuery();

            while (theSet.next()) {
                Reiziger r = new Reiziger();
                r.setId(theSet.getInt("reiziger_id"));
                r.setVoorletters(theSet.getString("voorletters"));
                r.setTussenvoegsel(theSet.getString("tussenvoegsel"));
                r.setAchternaam(theSet.getString("achternaam"));
                r.setGeboortedatum(theSet.getDate("geboortedatum"));

                // Geef het reiziger object mee om het bijbehorende adres op te vragen
                if (adao != null && adao.findByReiziger(r) != null) {
                    r.setAdres(adao.findByReiziger(r));
                    r.getAdres().setReiziger(r);
                }

                // Ook hier geven we reiziger mee aan oVChipkaart
                if (odao != null && odao.findByReiziger(r) != null) {
                    r.setoVChipkaarten(odao.findByReiziger(r));
                    for (OVChipkaart ovChipkaart : odao.findByReiziger(r)) {
                        ovChipkaart.setReiziger(r);
                    }
                }

                reizigers.add(r);
            }
            theSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reizigers;
    }

    @Override
    public boolean update(Reiziger inReiziger) throws SQLException {
        try {
            PreparedStatement statement = this.inConnection.prepareStatement(
                    "UPDATE reiziger SET  reiziger_id = ?, voorletters = ?, tussenvoegsel=? , achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?;");
            statement.setInt(1, inReiziger.getId());
            statement.setString(2, inReiziger.getVoorletters());
            statement.setString(3, inReiziger.getTussenvoegsel());
            statement.setString(4, inReiziger.getAchternaam());
            statement.setDate(5, inReiziger.getGeboortedatum());
            statement.setInt(6, inReiziger.getId());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger inReiziger) throws SQLException {
        try {
            PreparedStatement statement = this.inConnection.prepareStatement(
                    "DELETE FROM reiziger WHERE reiziger_id = ?;");
            statement.setInt(1, inReiziger.getId());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        ArrayList<Reiziger> reizigers = new ArrayList<Reiziger>();
        try {
            PreparedStatement statement = this.inConnection
                    .prepareStatement("SELECT * FROM reiziger WHERE reiziger_id = ?;");
            statement.setInt(1, id);
            ResultSet theSet = statement.executeQuery();

            if (theSet.next()) {
                Reiziger r = new Reiziger();
                r.setId(theSet.getInt("reiziger_id"));
                r.setVoorletters(theSet.getString("voorletters"));
                r.setTussenvoegsel(theSet.getString("tussenvoegsel"));
                r.setAchternaam(theSet.getString("achternaam"));
                r.setGeboortedatum(theSet.getDate("geboortedatum"));

                return r;
            }
            theSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}