package com.project.comuhavayollari;

import androidx.appcompat.app.AppCompatActivity;

public class Bilet extends AppCompatActivity {
    private String ucus_no;
    private String bilet_no;
    private String ucus_tarihi;
    private String ucus_saati;
    private String kalkis_noktasi;
    private String varis_noktasi;
    private String koltuk_no;
    private String bilet_fiyati;
    private String bilet_ucus_id;
    private String bilet_gidis_donus_id;
    private String koltuk_no_gidis_donus;

    // Boş yapıcı kaldırıldı

    public Bilet(String ucus_no, String bilet_no, String ucus_tarihi, String ucus_saati, String kalkis_noktasi, String varis_noktasi, String koltuk_no, String bilet_fiyati, String bilet_ucus_id ) {
        this.ucus_no = ucus_no;
        this.bilet_no = bilet_no;
        this.ucus_tarihi = ucus_tarihi;
        this.ucus_saati = ucus_saati;
        this.kalkis_noktasi = kalkis_noktasi;
        this.varis_noktasi = varis_noktasi;
        this.koltuk_no=koltuk_no;
        this.bilet_fiyati=bilet_fiyati;
        this.bilet_ucus_id = bilet_ucus_id;

    }

    public Bilet(String ucus_no, String ucus_tarihi, String ucus_saati, String kalkis_noktasi, String varis_noktasi, String koltuk_no, String bilet_fiyati ,String bilet_ucus_id ) {
        this.ucus_no = ucus_no;
        this.ucus_tarihi = ucus_tarihi;
        this.ucus_saati = ucus_saati;
        this.kalkis_noktasi = kalkis_noktasi;
        this.varis_noktasi = varis_noktasi;
        this.koltuk_no=koltuk_no;
        this.bilet_fiyati=bilet_fiyati;
        this.bilet_ucus_id=bilet_ucus_id;
    }
    public Bilet(String ucus_no, String bilet_no, String ucus_tarihi, String ucus_saati, String kalkis_noktasi, String varis_noktasi, String koltuk_no, String bilet_fiyati, String bilet_ucus_id , String bilet_gidis_donus_id , String koltuk_no_gidis_donus) {
        this.ucus_no = ucus_no;
        this.bilet_no = bilet_no;
        this.ucus_tarihi = ucus_tarihi;
        this.ucus_saati = ucus_saati;
        this.kalkis_noktasi = kalkis_noktasi;
        this.varis_noktasi = varis_noktasi;
        this.koltuk_no=koltuk_no;
        this.bilet_fiyati=bilet_fiyati;
        this.bilet_ucus_id = bilet_ucus_id;
        this.bilet_gidis_donus_id = bilet_gidis_donus_id;
        this.koltuk_no_gidis_donus = koltuk_no_gidis_donus;
    }


    public void setUcusNo(String ucus_no) {
        this.ucus_no = ucus_no;
    }

    public String getUcusNo() {
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


    public String getUcusTarihi() {
        return ucus_tarihi;
    }
    public void setUcus_saati(String ucus_saati){
        this.ucus_saati=ucus_saati;
    }
    public String getUcus_saati(){
        return ucus_saati;
    }

    public void setKalkisNoktasi(String kalkis_noktasi) {
        this.kalkis_noktasi = kalkis_noktasi;
    }

    public String getKalkisNoktasi() {
        return kalkis_noktasi;
    }

    public void setVarisNoktasi(String varis_noktasi) {
        this.varis_noktasi = varis_noktasi;
    }

    public String getVarisNoktasi() {
        return varis_noktasi;
    }

    public void setKoltuk_no(String koltuk_no) {this.koltuk_no=koltuk_no; }
    public String getKoltuk_no() {return koltuk_no ;}

    public void setBilet_fiyati(String bilet_fiyati) {this.bilet_fiyati=bilet_fiyati ;}
    public String getBilet_fiyati() {return bilet_fiyati ; }
    public String getBilet_ucus_id() {
        return bilet_ucus_id;
    }

    public void setBilet_ucus_id(String bilet_ucus_id) {
        this.bilet_ucus_id = bilet_ucus_id;
    }

    @Override
     public String toString() {
       return "BiletDetaylari{" +
                "ucus_no='" + ucus_no + '\'' +
                ", bilet_no='" + bilet_no + '\'' +
                ", ucus_tarihi='" + ucus_tarihi + '\'' +
                ",ucus_saati='" + ucus_saati +  '\'' +
                ", kalkis_noktasi='" + kalkis_noktasi + '\'' +
                ", varis_noktasi='" + varis_noktasi + '\'' +
                ", koltuk_no='"    +koltuk_no + '\'' +
                ",bilet_fiyati='" +bilet_fiyati + '\'' +
                ",ucus_id='" +bilet_ucus_id + '\'' +
                '}';
    }


    public String getBilet_gidis_donus_id() {
        return bilet_gidis_donus_id;
    }

    public void setBilet_gidis_donus_id(String bilet_gidis_donus_id) {
        this.bilet_gidis_donus_id = bilet_gidis_donus_id;
    }

    public String getKoltuk_no_gidis_donus() {
        return koltuk_no_gidis_donus;
    }

    public void setKoltuk_no_gidis_donus(String koltuk_no_gidis_donus) {
        this.koltuk_no_gidis_donus = koltuk_no_gidis_donus;
    }
}
