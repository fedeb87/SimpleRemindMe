/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.federicoberon.simpleremindme.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

/**
 * Immutable model class for a Milestone
 */
@Entity(tableName = "milestones", foreignKeys = {@ForeignKey(entity = MilestoneType.class,
        parentColumns = "id",
        childColumns = "type")}, indices = {@Index(value = {"type"})})
public class Milestone {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String title;
    private String description;
    @ColumnInfo(name = "milestone_date")
    private Date milestoneDate;
    private int type;

    @Ignore
    public Milestone() {
    }

    @Ignore
    public Milestone(@NonNull String title, String description, Date milestoneDate, int type) {
        this.title = title;
        this.description = description;
        this.milestoneDate = milestoneDate;
        this.type = type;
    }

    public Milestone(long id, @NonNull String title, String description, Date milestoneDate, int type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.milestoneDate = milestoneDate;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getMilestoneDate() {
        return milestoneDate;
    }

    public void setMilestoneDate(Date milestoneDate) {
        this.milestoneDate = milestoneDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Milestone milestone = (Milestone) o;
        return type == milestone.type && title.equals(milestone.title) && description.equals(milestone.description) && milestoneDate.equals(milestone.milestoneDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, milestoneDate, type);
    }
}
