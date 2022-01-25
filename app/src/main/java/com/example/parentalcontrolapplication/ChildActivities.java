package com.example.parentalcontrolapplication;

public class ChildActivities {

    private String childActivityName;
    private String activityTime;
    private String activityDate;
    private String activityDescription;
    private String childName;

    @Override
    public String toString() {
        return
                "childActivityName ='" + childActivityName + ' ' +
                ", activityTime ='" + activityTime + ' ' +
                ", activityDate ='" + activityDate + ' ' +
                ", activityDescription ='" + activityDescription + ' '
                ;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildActivityName() {
        return childActivityName;
    }

    public void setChildActivityName(String childActivity) {
        this.childActivityName = childActivity;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }
}
