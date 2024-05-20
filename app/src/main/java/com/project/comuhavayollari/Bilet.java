package com.project.comuhavayollari;



public class Bilet {
    private String biletNo;
    private String ucusNo;
    private String kalkisNoktasi;
    private String varisNoktasi;
    private String ucusTarihi;
    private String ucusSaati;
    private String biletFiyati;
    public Bilet(){

    }

    public Bilet(String biletNo, String ucusNo, String kalkisNoktasi,String varisNoktasi, String ucusTarihi, String biletFiyati) {
        this.biletNo = biletNo;
        this.ucusNo = ucusNo;
        this.kalkisNoktasi = kalkisNoktasi;
        this.ucusTarihi = ucusTarihi;
        this.varisNoktasi=varisNoktasi;
        this.ucusSaati=ucusSaati;
        this.biletFiyati=biletFiyati;
    }

    public String getBiletNo() {
        return biletNo;
    }

    public String getUcusNo() {
        return ucusNo;
    }

    public String getKalkisNoktasi() {
        return kalkisNoktasi;
    }
    public String getVarisNoktasi(){
        return varisNoktasi;
    }

    public String getUcusTarihi() {
        return ucusTarihi;
    }
    public String getUcusSaati() {
            return ucusSaati;
        }

    public String getBiletFiyati() {
        return biletFiyati;
    }
}




