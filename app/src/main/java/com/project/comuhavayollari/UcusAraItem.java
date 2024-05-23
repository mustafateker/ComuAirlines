package com.project.comuhavayollari;

public class UcusAraItem {



        private int ucusLogo;
        private String ucusNo;
        private String ucusSaati;
        private String kalkisYeri;
        private int yon;
        private String varisYeri;
        private String fiyat;

        public UcusAraItem(int ucusLogo, String ucusNo, String ucusSaati, String kalkisYeri, int yon, String varisYeri, String fiyat) {
            this.ucusLogo = ucusLogo;
            this.ucusNo = ucusNo;
            this.ucusSaati = ucusSaati;
            this.kalkisYeri = kalkisYeri;
            this.yon = yon;
            this.varisYeri = varisYeri;
            this.fiyat = fiyat;
        }

        public int getUcusLogo() {
            return ucusLogo;
        }

        public String getUcusNo() {
            return ucusNo;
        }

        public String getUcusSaati() {
            return ucusSaati;
        }

        public String getKalkisYeri() {
            return kalkisYeri;
        }

        public int getYon() {
            return yon;
        }

        public String getVarisYeri() {
            return varisYeri;
        }

        public String getFiyat() {
            return fiyat;
        }
    }

