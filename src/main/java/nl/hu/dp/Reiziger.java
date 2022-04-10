package nl.hu.dp;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters = "";
    private String tussenvoegsel = "";
    private String achternaam = "";
    private java.sql.Date geboortedatum = new java.sql.Date(0);
    private Adres adres;
    private ArrayList<OVChipkaart> oVChipkaarten;

    public List<OVChipkaart> getOVChipkaarten() {
        return this.oVChipkaarten;
    }

    public void setoVChipkaarten(List<OVChipkaart> oVChipkaarten) {
        this.oVChipkaarten = (ArrayList<OVChipkaart>) oVChipkaarten;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public Reiziger() {
    }

    public String toString() {
        String s = "Reiziger:\t(" + this.id + " " +
                this.voorletters + " " +
                this.achternaam + " " +
                this.tussenvoegsel + " " +
                this.geboortedatum.toString() + " " +
                ")";
        if (adres != null) {
            s += adres.toString();
        }
        if (oVChipkaarten != null) {
            for (OVChipkaart ovChipkaart : oVChipkaarten) {
                s += "\n\t" + ovChipkaart.toString();
            }
        }
        return s;
    }

    public java.sql.Date getGeboortedatum() {
        return geboortedatum;
    }

    public int getId() {
        return id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setAchternaam(String achternaam) {
        if (achternaam != null) {
            if (achternaam.length() > 255) {
                achternaam = achternaam.substring(0, 255);
            }
            this.achternaam = achternaam;
        }
    }

    public void setGeboortedatum(Date geboortedatum) {
        if (geboortedatum != null) {
            if (geboortedatum.after(new Date(0)))
                this.geboortedatum = geboortedatum;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        if (tussenvoegsel != null) {
            if (tussenvoegsel.length() > 10) {
                tussenvoegsel = tussenvoegsel.substring(0, 10);
            }
            this.tussenvoegsel = tussenvoegsel;
        }
    }

    public void setVoorletters(String voorletters) {
        if (voorletters != null) {
            if (voorletters.length() > 10) {
                voorletters = voorletters.substring(0, 10);
            }
            this.voorletters = voorletters;
        }
    }
}