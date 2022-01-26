package com.federicoberon.simpleremindme.model;

import androidx.room.Embedded;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
/**
 * This class allow us to add typeName and typeColor to MilestoneEntity
 */
public class MilestoneXType {

    @Embedded
    Milestone milestone;

    String typeName;
    String color;

    public MilestoneXType(Milestone milestone, String typeName, String color) {
        this.milestone = milestone;
        this.typeName = typeName;
        this.color = color;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return milestone.getTitle();
    }

    public void setTitle(String title) {
        milestone.setTitle(title);
    }

    public String getDesc() {
        return milestone.getDescription();
    }

    public void setDesc(String desc) {
        milestone.setDescription(desc);
    }

    public int getType() {
        return milestone.getType();
    }

    public void setType(int type) {
        milestone.setType(type);
    }

    public Date getMilestoneDate() {
        return milestone.getMilestoneDate();
    }

    public void setMilestoneDate(Date milestoneDate) {
        milestone.setMilestoneDate(milestoneDate);
    }

    public long getId() {
        return milestone.getId();
    }

    /**
     * Using for testing purpose
     * @param milestoneToCompare second milestone to compare
     * @return
     */
    public boolean equals(MilestoneXType milestoneToCompare){
        if (milestoneToCompare.getTitle().equals(getTitle()) &&
                milestoneToCompare.getDesc().equals(getDesc()) &&
                milestoneToCompare.getType() == getType() &&
                milestoneToCompare.getMilestoneDate().compareTo(getMilestoneDate()) == 0 ) {
            return true;
        }
        return false;
    }
}
