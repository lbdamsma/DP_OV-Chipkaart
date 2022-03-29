package nl.hu.dp;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private int product_nummer;
    private String naam;
    private String beschrijving;
    private float prijs;
    private List<OVChipkaart> OVChipkaarten = new ArrayList<>();

    public Product(int porduct_nummer, String naam, String beschrijving, float prijs) {
        this.product_nummer = porduct_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public String getNaam() {
        return naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public float getPrijs() {
        return prijs;
    }

    public List<OVChipkaart> getOVChipkaarten() {
        return OVChipkaarten;
    }

    public boolean addOVChipkaart(OVChipkaart ovChipkaart) {
        for (OVChipkaart ov : OVChipkaarten) {
            if (ov.getKaart_nummer() == ovChipkaart.getKaart_nummer()){
                return false;
            }
        }

        OVChipkaarten.add(ovChipkaart);
        return true;
    }

    public boolean deleteOVChipkaart(OVChipkaart ovChipkaart) {
        for (OVChipkaart ov : OVChipkaarten) {
            if (ov.getKaart_nummer() == ovChipkaart.getKaart_nummer()) {
                OVChipkaarten.remove(ovChipkaart);
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        String x = "";

        for(OVChipkaart ov : OVChipkaarten) {
            x += ov.getKaart_nummer() + ", ";
        }

        return "Product: " +
                "product_nummer = " + product_nummer +
                ", naam = '" + naam + '\'' +
                ", beschrijving = '" + beschrijving + '\'' +
                ", prijs = " + prijs +
                ", kaart(en) = " + x;
    }
}