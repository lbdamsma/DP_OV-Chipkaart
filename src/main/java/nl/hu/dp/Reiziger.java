package nl.hu.dp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {

    private int id;
    private String voorletters;
    private String tussenvoegsels;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> OVChipkaarten = new ArrayList<>();

    public Reiziger(int id, String voorletters, String tussenvoegsels, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsels = tussenvoegsels;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public String getTussenvoegsels() {
        return tussenvoegsels;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OVChipkaart> getOVChipkaarten() {
        return OVChipkaarten;
    }

    @Override
    public String toString() {
        return "Reiziger: " + "id: " + id + ", voorletters: '" + voorletters + '\'' +
                ", tussenvoegsels: '" + tussenvoegsels + '\'' + ", achternaam: '" +
                achternaam + '\'' + ", geboortedatum: " + geboortedatum + ", " + adres;
    }
}