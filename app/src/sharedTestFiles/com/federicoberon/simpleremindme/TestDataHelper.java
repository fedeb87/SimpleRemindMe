package com.federicoberon.simpleremindme;

import com.federicoberon.simpleremindme.model.Milestone;
import com.federicoberon.simpleremindme.model.MilestoneType;
import com.federicoberon.simpleremindme.model.MilestoneXType;

import java.util.Calendar;

public class TestDataHelper {
    public static final MilestoneType MILESTONETYPE_1 = new MilestoneType(1, "Family", "#F06423");
    public static final MilestoneType MILESTONETYPE_2 = new MilestoneType(2, "Work", "#741873");
    public static final MilestoneXType MILESTONE_1 = new MilestoneXType(new Milestone(1,
            "Visit uncle Bill","Visit uncle Bill and her cats :)",
            getDate1().getTime(), 1),"Family", "#F06423");
    public static final MilestoneXType MILESTONE_2 = new MilestoneXType(new Milestone(2,
            "Stephan birthday","Stephan celebrate his 22 years old in a beach",
            getDate2().getTime(), 2),"Work", "#A20C0C");

    public static final MilestoneXType MILESTONE_3 = new MilestoneXType(new Milestone(3,
            "Only for testing purpose","Visit Zoo with the family",
            getDate1().getTime(), 1),"Family", "#F06423");

    public static Calendar getDate1(){

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, 2021);
        calendar1.set(Calendar.MONTH, 9);
        calendar1.set(Calendar.DAY_OF_MONTH, 10);
        calendar1.set(Calendar.HOUR, 0);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        return calendar1;
    }

    public static Calendar getDate2(){

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2022);
        calendar2.set(Calendar.MONTH, 6);
        calendar2.set(Calendar.DAY_OF_MONTH, 7);
        calendar2.set(Calendar.HOUR, 0);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        return calendar2;
    }
}
