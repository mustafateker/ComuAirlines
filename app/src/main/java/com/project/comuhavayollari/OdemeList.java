package com.project.comuhavayollari;

public class OdemeList {
    private String BiletFiyati;
    private String UyelikIndirimi;
    private String GidisDonusIndirimi;
    private String ToplamFiyat;

    public OdemeList(){

    }

    public OdemeList(String BiletFiyati , String UyelikIndirimi, String GidisDonusIndirimi , String ToplamFiyat){
        this.BiletFiyati = BiletFiyati;
        this.UyelikIndirimi = UyelikIndirimi;
        this.GidisDonusIndirimi = GidisDonusIndirimi;
        this.ToplamFiyat = ToplamFiyat;
    }

    public String getBiletFiyati() {
        return BiletFiyati;
    }

    public void setBiletFiyati(String biletFiyati) {
        BiletFiyati = biletFiyati;
    }

    public String getUyelikIndirimi() {
        return UyelikIndirimi;
    }

    public void setUyelikIndirimi(String uyelikIndirimi) {
        UyelikIndirimi = uyelikIndirimi;
    }

    public String getGidisDonusIndirimi() {
        return GidisDonusIndirimi;
    }

    public void setGidisDonusIndirimi(String gidisDonusIndirimi) {
        GidisDonusIndirimi = gidisDonusIndirimi;
    }

    public String getToplamFiyat() {
        return ToplamFiyat;
    }

    public void setToplamFiyat(String toplamFiyat) {
        ToplamFiyat = toplamFiyat;
    }
}
