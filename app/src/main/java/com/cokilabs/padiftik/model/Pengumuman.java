package com.cokilabs.padiftik.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pengumuman {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("dosen_id")
    @Expose
    private String dosenId;
    @SerializedName("dosen_name")
    @Expose
    private String dosenName;
    @SerializedName("pengumuman")
    @Expose
    private String pengumuman;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDosenId() {
        return dosenId;
    }

    public void setDosenId(String dosenId) {
        this.dosenId = dosenId;
    }

    public String getDosenName() {
        return dosenName;
    }

    public void setDosenName(String dosenName) {
        this.dosenName = dosenName;
    }

    public String getPengumuman() {
        return pengumuman;
    }

    public void setPengumuman(String pengumuman) {
        this.pengumuman = pengumuman;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}