package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "logs")
public class LogEntry {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "period")
    @SerializedName("period")
    private Date period;

    @ColumnInfo(name = "eventName")
    @SerializedName("eventName")
    private String eventName;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    public LogEntry(int id, @NonNull Date period, String eventName, String description) {
        this.id = id;
        this.period = period;
        this.eventName = eventName;
        this.description = description;
    }

    @Ignore
    public LogEntry(Date period, String eventName, String description) {
        this.period = period;
        this.eventName = eventName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Date getPeriod() {
        return period;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }
}
