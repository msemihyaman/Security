package com.msy.security;

public class tamamlama {
    private String tc;
    private String durum;

    public tamamlama(String tc, String durum) {
        this.tc = tc;
        this.durum = durum;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
