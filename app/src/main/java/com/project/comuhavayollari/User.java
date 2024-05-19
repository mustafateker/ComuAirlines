package com.project.comuhavayollari;

import java.util.Date;

public class User {
    private String ad;
    private String Soyad;
    private String email;
    private String parola;
    private String userid;
    private String yas;
    private String cinsiyet;
    private String ikametgah;
    private String aldigiBiletSayisi;
    private String kaydolduguTarih;
    private String uyeTipi;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Flight.class)
    }
    public User(String ad, String Soyad, String email,
                String userid, String yas, String cinsiyet ,
                String ikametgah, String aldigiBiletSayisi ,
                String kaydolduguTarih ,String uyeTipi) {
        this.ad = ad;
        this.Soyad = Soyad;
        this.email = email;
        this.userid = userid;
        this.yas = yas;
        this.cinsiyet = cinsiyet;
        this.ikametgah = ikametgah;
        this.aldigiBiletSayisi = aldigiBiletSayisi;
        this.kaydolduguTarih = kaydolduguTarih;
        this.uyeTipi = uyeTipi;

    }

    public void BiletAl(){

    }

    public void BiletIadeEt(){

    }

    public void RezerveEt(){

    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return Soyad;
    }

    public void setSoyad(String soyad) {
        Soyad = soyad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getYas() {
        return yas;
    }

    public void setYas(String yas) {
        this.yas = yas;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getIkametgah() {
        return ikametgah;
    }

    public void setIkametgah(String ikametgah) {
        this.ikametgah = ikametgah;
    }

    public String getAldigiBiletSayisi() {
        return aldigiBiletSayisi;
    }

    public void setAldigiBiletSayisi(String aldigiBiletSayisi) {
        this.aldigiBiletSayisi = aldigiBiletSayisi;
    }

    public String getKaydolduguTarih() {
        return kaydolduguTarih;
    }

    public void setKaydolduguTarih(String kaydolduguTarih) {
        this.kaydolduguTarih = kaydolduguTarih;
    }

    public String getUyeTipi() {
        return uyeTipi;
    }

    public void setUyeTipi(String uyeTipi) {
        this.uyeTipi = uyeTipi;
    }
}

