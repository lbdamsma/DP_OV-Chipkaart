package nl.hu.dp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private int kaart_nummer;
    private java.sql.Date geldig_tot = new java.sql.Date(0);
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
    private ArrayList<Product> producten = new ArrayList<Product>();

    public OVChipkaart() {
    }

    public OVChipkaart(int kaart_nummer, java.sql.Date geldig_tot, int klasse, double saldo) {
        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public void setKaart_nummer(int kaart_nummer) {
        this.kaart_nummer = kaart_nummer;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public void setProducten(List<Product> producten) {
        this.producten = (ArrayList) producten;
    }

    public List<Product> getProducten() {
        return this.producten;
    }

    public void addProdcut(Product product) {
        if (!producten.contains(product)) {
            producten.add(product);
        }
    }

    @Override
    public String toString() {
        String s = "";
        s = String.format("OVChipkaart: %d, %d, %.2f, %s ", this.kaart_nummer, this.klasse, this.saldo,
                this.geldig_tot);
        if (producten != null && producten.size() > 0) {
            s += "Met producten: ";
            for (Product p : producten) {
                s += p.toString();
            }
        }
        return s;
    }

    @Override
    public boolean equals(Object ovKaart) {
        if (ovKaart != null && ovKaart instanceof OVChipkaart) {
            ovKaart = (OVChipkaart) ovKaart;
            if (((OVChipkaart) ovKaart).getKaart_nummer() != this.kaart_nummer) {
                return true;
            }
        }
        return false;
    }
}