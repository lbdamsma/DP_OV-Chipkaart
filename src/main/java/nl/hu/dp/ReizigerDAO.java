package nl.hu.dp;

import java.sql.Connection;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    boolean update(Reiziger inReiziger) throws SQLException;

    boolean save(Reiziger inReiziger) throws SQLException;

    boolean delete(Reiziger inReiziger) throws SQLException;

    List<Reiziger> findAll() throws SQLException;

    Reiziger findById(int id) throws SQLException;

    AdresDAO getAdao();

    OVChipkaartDAO getOdao();
}