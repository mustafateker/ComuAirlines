package com.project.comuhavayollari;


public class BiletDetaylari {
    private String ucus_no;
    private String bilet_no;
    private String ucus_tarihi;
    private String kalkıs_noktası;
    private String varıs_noktası;


    public BiletDetaylari(String ucus_no, String bilet_no, String ucus_tarihi, String kalkıs_noktası, String varı_snoktası) {
        this.ucus_no = ucus_no;
        this.bilet_no = bilet_no;
        this.ucus_tarihi = ucus_tarihi;
        this.kalkıs_noktası = kalkıs_noktası;
        this.varıs_noktası = varıs_noktası;
    }

    public void setUcusNo(String ucus_no) {
        this.ucus_no = ucus_no;
    }

    public String getUcusno() {
        return ucus_no;
    }

    public void setBiletNo(String bilet_no) {
        this.bilet_no = bilet_no;
    }

    public String getBiletNo() {
        return bilet_no;
    }

    public void setUcusTarihi(String ucus_tarihi) {
        this.ucus_tarihi = ucus_tarihi;
    }

    public String getUcustarihi() {
        return ucus_tarihi;
    }

    public void setKalkisnoktasioktasi(String kalkıs_noktası) {
        this.kalkıs_noktası = kalkıs_noktası;
    }

    public String getKalkisNoktasi() {
        return kalkıs_noktası;
    }

    public void setVarisnoktasioktasi(String varisnoktasi) {
        this.varıs_noktası = varisnoktasi;
    }
    public String getVarisNoktasi() {
        return varıs_noktası;
    }
    @Override
    public String toString() {
        return "BiletDetaylariModel{" +
                "Uçuş_Numarası=" + ucus_no +
                ", Bilet_Numarası='" + bilet_no + '\'' +
                ", Ucus_Tarihi='" + ucus_tarihi + '\'' +
                ", Kalkıs_Noktası='" + kalkıs_noktası + '\'' +
                ", Varıs_Noktası='" + varıs_noktası + '\'' +
                '}';
    } }
