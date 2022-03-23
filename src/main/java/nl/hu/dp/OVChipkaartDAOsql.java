package nl.hu.dp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOsql implements OVChipkaartDAO {

    private Connection conn;
    private ReizigerDAO rdao;

    public OVChipkaartDAOsql(Connection conn) {
        this.conn = conn;
    }
    public  OVChipkaartDAOsql(Connection conn, ReizigerDAOsql rdaosql) {
        this.conn = conn;
        this.rdao = rdaosql;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        String SQl = "INSERT INTO ov_chipkaart VALUES(?, ?, ?, ?, ?)";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQl);

            prestat.setInt(1, ovChipkaart.getKaart_nummer());
            prestat.setDate(2, ovChipkaart.getGeldig_tot());
            prestat.setInt(3, ovChipkaart.getKlasse());
            prestat.setFloat(4, ovChipkaart.getSaldo());
            prestat.setInt(5, ovChipkaart.getReiziger_id());

            prestat.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        String SQL = "UPDATE ov_chipkaart SET saldo = saldo + 10 WHERE kaart_nummer = ?";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setInt(1, ovChipkaart.getKaart_nummer());

            prestat.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        String SQL = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setInt(1, ovChipkaart.getKaart_nummer());

            prestat.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        String SQL = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            prestat.setInt(1, reiziger.getId());

            ResultSet rs = prestat.executeQuery();

            List<OVChipkaart> ovs = new ArrayList<>();

            while (rs.next()) {
                int knm = rs.getInt("kaart_nummer");
                Date geldigTot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                float saldo = rs.getFloat("saldo");
                int rid = rs.getInt("reiziger_id");

                OVChipkaart ov = new OVChipkaart(knm, geldigTot, klasse, saldo, rid);
                ovs.add(ov);
            }

            return ovs;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OVChipkaart> findAll() {
        String SQL = "SELECT * FROM ov-chipkaart";

        try {
            PreparedStatement prestat = conn.prepareStatement(SQL);

            ResultSet rs = prestat.executeQuery();

            List<OVChipkaart> ovs = new ArrayList<>();

            while (rs.next()) {
                int knm = rs.getInt("kaart_nummer");
                Date geldigTot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                float saldo = rs.getFloat("saldo");
                int rid = rs.getInt("reiziger_id");

                OVChipkaart ov = new OVChipkaart(knm, geldigTot, klasse, saldo, rid);
                ovs.add(ov);
            }

            return ovs;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}