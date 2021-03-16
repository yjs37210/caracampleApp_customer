package com.example.caracample;

public class power {

    private String air;
    private String fire;
    private String sunroof;

    public power(){

    }

    public power(String air, String fire, String sunroof) {
        this.air = air;
        this.fire = fire;
        this.sunroof = sunroof;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }

    public String getFire() {
        return fire;
    }

    public void setFire(String fire) {
        this.fire = fire;
    }

    public String getSunroof() {
        return sunroof;
    }

    public void setSunroof(String sunroof) {
        this.sunroof = sunroof;
    }

    @Override
    public String toString() {
        return "power{" +
                "air='" + air + '\'' +
                ", fire='" + fire + '\'' +
                ", sunroof='" + sunroof + '\'' +
                '}';
    }
}
