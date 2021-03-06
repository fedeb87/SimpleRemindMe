package com.federicoberon.simpleremindme.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "milestone_types")
public class MilestoneType {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String color;

    @Ignore
    public MilestoneType() {
    }

    @Ignore
    public MilestoneType(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public MilestoneType(long id, String title, String color) {
        this.id = id;
        this.title = title;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MilestoneType that = (MilestoneType) o;
        return title.equals(that.title) && color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, color);
    }
}
