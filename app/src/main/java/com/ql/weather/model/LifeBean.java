package com.ql.weather.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class LifeBean implements Serializable {
    private String chuanyi;
    private String ganmao;
    private String kongtiao;
    private String xiche;
    private String yundong;
    private String ziwaixian;

    public LifeBean() {
    }

    public LifeBean(String chuanyi, String ganmao, String kongtiao, String xiche, String yundong, String ziwaixian) {
        this.chuanyi = chuanyi;
        this.ganmao = ganmao;
        this.kongtiao = kongtiao;
        this.xiche = xiche;
        this.yundong = yundong;
        this.ziwaixian = ziwaixian;
    }

    public String getChuanyi() {
        return chuanyi;
    }

    public void setChuanyi(String chuanyi) {
        this.chuanyi = chuanyi;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public String getKongtiao() {
        return kongtiao;
    }

    public void setKongtiao(String kongtiao) {
        this.kongtiao = kongtiao;
    }

    public String getXiche() {
        return xiche;
    }

    public void setXiche(String xiche) {
        this.xiche = xiche;
    }

    public String getYundong() {
        return yundong;
    }

    public void setYundong(String yundong) {
        this.yundong = yundong;
    }

    public String getZiwaixian() {
        return ziwaixian;
    }

    public void setZiwaixian(String ziwaixian) {
        this.ziwaixian = ziwaixian;
    }
}
