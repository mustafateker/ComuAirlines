package com.project.comuhavayollari;

import androidx.appcompat.app.AppCompatActivity;

public class BiletDetaylari extends AppCompatActivity {
    private String ucus_no;
    private String bilet_no;
    private String ucus_tarihi;
    private String kalkis_noktasi;
    private String varis_noktasi;

    // Boş yapıcı kaldırıldı

    public BiletDetaylari(String ucus_no, String bilet_no, String ucus_tarihi, String kalkis_noktasi, String varis_noktasi) {
        this.ucus_no = ucus_no;
        this.bilet_no = bilet_no;
        this.ucus_tarihi = ucus_tarihi;
        this.kalkis_noktasi = kalkis_noktasi;
        this.varis_noktasi = varis_noktasi;
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

    @Override
    public String toString() {
        return "BiletDetaylari{" +
                "ucus_no='" + ucus_no + '\'' +
                ", bilet_no='" + bilet_no + '\'' +
                ", ucus_tarihi='" + ucus_tarihi + '\'' +
                ", kalkis_noktasi='" + kalkis_noktasi + '\'' +
                ", varis_noktasi='" + varis_noktasi + '\'' +
                '}';
    }
}
