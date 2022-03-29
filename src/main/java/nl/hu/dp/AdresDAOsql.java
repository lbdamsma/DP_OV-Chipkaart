package nl.hu.dp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOsql implements AdresDAO {

    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOsql(Connection conn) {
        this.conn = conn;
    }

    public AdresDAOsql(Connection conn, ReizigerDAOPsql rdaosql){
        this.conn = conn;
        this.rdao = rdaosql;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) {

        String SQL = "INSERT INTO adres(" +
                "adres_id, " +
                "postcode, " +
                "huisnummer, " +
                "straat, " +
                "woonplaats, " +
                "reiziger_id) " +
                "VALUES(?, ?, ?, ?, ?, ?)";


        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setInt(1, adres.getAdres_id());
            prestat.setString(2, adres.getPostcode());
            prestat.setString(3, adres.getHuisnummer());
            prestat.setString(4, adres.getStraat());
            prestat.setString(5, adres.getWoonplaats());
            prestat.setInt(6, adres.getReiziger_id());

            prestat.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Adres adres) {
        String SQL = "UPDATE adres SET postcode = ? WHERE reiziger_id = ?";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setString(1, "5482LE");
            prestat.setInt(2, adres.getReiziger_id());

            prestat.executeUpdate();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Adres adres) {

        String SQL = "DELETE FROM adres WHERE reiziger_id = ?";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setInt(1, adres.getReiziger_id());

            prestat.executeUpdate();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        String SQL = "SELECT * FROM adres WHERE reiziger_id = ?";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setInt(1, reiziger.getId());

            ResultSet rs = prestat.executeQuery();

            while(rs.next()) {
                int aid = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnr = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String wnplts = rs.getString("woonplaats");
                int reiziger_id = rs.getInt("reiziger_id");
                return new Adres(aid, postcode, huisnr, straat, wnplts, reiziger_id);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Adres> findAll() {
        String SQL = "SELECT * FROM adres";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            ResultSet rs = prestat.executeQuery();

            List<Adres> adressen = new ArrayList<>();

            while (rs.next()) {
                int aid = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnr = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String wnplts = rs.getString("woonplaats");
                int reiziger_id = rs.getInt("reiziger_id");

                Adres adres = new Adres(aid, postcode, huisnr,straat, wnplts, reiziger_id);
                adressen.add(adres);
            }

            return adressen;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}