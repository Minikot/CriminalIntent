package com.aleksandr.criminalintent.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Aleksandr on 04.11.16.
 */

public class Crime {

    @SerializedName("uuid") @Expose private UUID uuid;
    @SerializedName("title") @Expose private String title;
    @SerializedName("date") @Expose private Date date;
    @SerializedName("solved") @Expose private boolean solved;
    @SerializedName("suspect") @Expose private String suspect;

    public Crime(UUID uuid) {
        this.uuid = uuid;
    }

    public Crime() {
        this.uuid = UUID.randomUUID();
        this.date = new Date();
    }

    public Crime(String title) {
        this.title = title;
        this.date = new Date();
        this.uuid = UUID.randomUUID();
    }

    public Crime(String title, boolean solved) {
        this.title = title;
        this.solved = solved;
        this.date = new Date();
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getDate(@Nullable String format){
        if (format == null || format.length()==0)
            return date.toString();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }
}
