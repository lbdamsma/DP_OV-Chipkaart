package nl.hu.dp;

import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger inReiziger) ;
    public boolean update(Reiziger inReiziger);
    public boolean delete(Reiziger inReiziger);
    public List<Reiziger> findAll() ;
}
