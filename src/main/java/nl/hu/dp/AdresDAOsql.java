package nl.hu.dp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOsql implements AdresDAO {
    private Connection conn;

    public ReizigerDAO getRdao() {
        return rdao;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    private ReizigerDAO rdao;

    public AdresDAOsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Adres inAdres) throws SQLException {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?,?,?,?,?,?)");
            statement.setInt(1, inAdres.getAdres_id());
            statement.setString(2, inAdres.getPostcode());
            statement.setString(3, inAdres.getHuisnummer());
            statement.setString(4, inAdres.getStraat());
            statement.setString(5, inAdres.getWoonplaats());
            statement.setInt(6, inAdres.getReiziger().getId());

            statement.executeUpdate();

            statement.close();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Adres inAdres) throws SQLException {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "UPDATE adres SET  adres_id = ?, postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?;");
            statement.setInt(1, inAdres.getAdres_id());
            statement.setString(2, inAdres.getPostcode());
            statement.setString(3, inAdres.getHuisnummer());
            statement.setString(4, inAdres.getStraat());
            statement.setString(5, inAdres.getWoonplaats());
            statement.setInt(6, inAdres.getReiziger().getId());
            statement.setInt(7, inAdres.getAdres_id());

            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Adres inAdres) throws SQLException {
        try {
            PreparedStatement statement = this.conn.prepareStatement(
                    "DELETE FROM adres WHERE adres_id = ?;");
            statement.setInt(1, inAdres.getAdres_id());
            statement.executeUpdate();
            statement.close();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        ArrayList<Adres> adressen = new ArrayList<Adres>();
        try {
            PreparedStatement statement = this.conn.prepareStatement("SELECT * FROM adres;");
            ResultSet theSet = statement.executeQuery();

            while (theSet.next()) {
                adressen.add(parseStatement(theSet));
            }

            // Because it's an interface where these functinos are designed
            // If you uncomment this you will enter an infinte loop
            // r.setReiziger(rdao.findById(theSet.getInt("reiziger_id")));

            theSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return adressen;
    }

    public Adres findById(int id) throws SQLException {
        try {
            PreparedStatement statement = this.conn.prepareStatement("SELECT * FROM adres where adres_id = ?;");
            statement.setInt(1, id);
            ResultSet theSet = statement.executeQuery();

            if (theSet != null && theSet.next()) {
                return parseStatement(theSet);
            }
            theSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {

        ArrayList<Adres> adressen = new ArrayList<Adres>();
        try {
            PreparedStatement statement = this.conn.prepareStatement("SELECT * FROM adres where reiziger_id = ?;");
            statement.setInt(1, reiziger.getId());
            ResultSet theSet = statement.executeQuery();

            if (theSet != null && theSet.next()) {
                return parseStatement(theSet);
            }

            theSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Adres parseStatement(ResultSet theSet) throws SQLException {
        Adres a = new Adres();

        a.setAdres_id(theSet.getInt("adres_id"));
        a.setPostcode(theSet.getString("postcode"));
        a.setHuisnummer(theSet.getString("huisnummer"));
        a.setStraat(theSet.getString("straat"));
        a.setWoonplaats(theSet.getString("woonplaats"));

        return a;

    }

}