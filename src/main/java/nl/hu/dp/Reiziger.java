package nl.hu.dp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int id = 0;
    private String voorletters = "";
    private String tussenvoegsel = "";
    private String achternaam = "";
    private Date geboortedatum = new Date(0);

    public String getVoorletters() {
        return this.voorletters;
    }

    public void setVoorletters(String voorletters) {
        if (voorletters != null) {
            if (voorletters.length() > 10) {
                voorletters = voorletters.substring(0, 10);
            }
            this.voorletters = voorletters;
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public String getTussenvoegsel() {
        return this.tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        if (tussenvoegsel != null) {
            if (tussenvoegsel.length() > 10) {
                tussenvoegsel = tussenvoegsel.substring(0, 10);
            }
            this.tussenvoegsel = tussenvoegsel;
        }
    }

    public void setAchternaam(String achternaam) {
        if (achternaam != null) {
            if (achternaam.length() > 10) {
                achternaam = achternaam.substring(0, 10);
            }
            this.achternaam = achternaam;
        }
    }

    public String toString(){
        String string ="#" + id + ": " + voorletters + ". " + tussenvoegsel + " " + achternaam + " " + "(" + geboortedatum + ")";
        return string;
    }

}
