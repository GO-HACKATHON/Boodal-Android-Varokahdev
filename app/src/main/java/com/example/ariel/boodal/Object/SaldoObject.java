package com.example.ariel.boodal.Object;

/**
 * Created by dickyeka on 3/26/17.
 */

public class SaldoObject {
    private String jenis;
    private String tgl;
    private int jumlah;

    public SaldoObject(String jenis, String tgl, int jumlah) {
        this.jenis = jenis;
        this.tgl = tgl;
        this.jumlah = jumlah;
    }

    public SaldoObject(String jenis) {
        this.jenis = jenis;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
