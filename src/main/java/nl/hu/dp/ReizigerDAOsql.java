package nl.hu.dp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOsql implements ReizigerDAO {
    private Connection connection = null;

    public ReizigerDAOsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Reiziger inReiziger)  {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?,?,?,?,?);");
            statement.setInt(1, inReiziger.getId());
            statement.setString(2,inReiziger.getVoorletters());
            statement.setString(3, inReiziger.getTussenvoegsel());
            statement.setString(4, inReiziger.getAchternaam());
            statement.setDate(5, inReiziger.getGeboortedatum());
            statement.executeQuery();
            statement.close();
        }catch (SQLException throwables) {
            System.err.println("SQLExecption:" + throwables.getMessage());
        }
        return true;
    }

    @Override
    public boolean update(Reiziger inReiziger) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? where reiziger_id = ?");
            statement.setString(1,inReiziger.getVoorletters());
            statement.setString(2, inReiziger.getTussenvoegsel());
            statement.setString(3, inReiziger.getAchternaam());
            statement.setDate(4, inReiziger.getGeboortedatum());
            statement.setInt(5, inReiziger.getId());
            statement.executeQuery();
            statement.close();
        }catch (SQLException throwables) {
            System.err.println("SQLExecption:" + throwables.getMessage());
        }
        return true;
    }

    public boolean delete(Reiziger inReiziger) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?;");
            statement.setInt(1, inReiziger.getId());
            statement.executeQuery();
            statement.close();
        } catch (SQLException throwables) {
            System.err.println("SQLExecption:" + throwables.getMessage());
        }
        return true;
    }

    public List<Reiziger> findAll() {
        ArrayList<Reiziger> reizigers = new ArrayList<Reiziger>();
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * from reiziger;");
            ResultSet set = statement.executeQuery();
            while (set != null && set.next()) {
                Reiziger r = new Reiziger();
                r.setId(set.getInt("reiziger_id"));
                r.setAchternaam(set.getString("achternaam"));
                r.setTussenvoegsel(set.getString("tussenvoegsel"));
                r.setGeboortedatum(set.getDate("geboortedatum"));
                reizigers.add(r);
            }
            if(set != null) set.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigers;
    }
}