package nl.hu.dp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {

    private int kaart_nummer;
    private Date geldig_tot;
    private int klasse;
    private float saldo;
    private int reiziger_id;
    private List<Product> producten = new ArrayList<>();

    public OVChipkaart(int kaart_nummer, Date geldig_tot, int klasse, float saldo, int reiziger_id) {
        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger_id = reiziger_id;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public int getKlasse() {
        return klasse;
    }

    public float getSaldo() {
        return saldo;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public boolean addProduct(Product product) {
        for (Product p : producten) {
            if (p.getProduct_nummer() == product.getProduct_nummer()) {
                return false;
            }
        }
        product.addOVChipkaart(this);
        producten.add(product);
        return true;
    }

    public boolean deleteProduct(Product product) {
        for (Product p : producten) {
            if (p.getProduct_nummer() == product.getProduct_nummer()) {
                product.deleteOVChipkaart(this);
                producten.remove(product);
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        String x = "";

        for(Product p : producten) {
            x += p.getNaam() + ", ";
        }

        return "OVChipkaart: " +
                "kaart_nummer = " + kaart_nummer +
                ", geldig_tot = " + geldig_tot +
                ", klasse = " + klasse +
                ", saldo = " + saldo +
                ", reiziger_id = " + reiziger_id +
                ", product(en) = " + x;
    }
}