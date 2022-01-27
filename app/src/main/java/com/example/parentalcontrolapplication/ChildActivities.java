package com.example.parentalcontrolapplication;

public class ChildActivities {

    private String childActivityName;
    private String activityTime;
    private String activityDate;
    private String activityDescription;
    private String childEmail;
    private String activityStatus;
    private String completedDate = "None";
    private String activityId;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getChildEmail() {
        return childEmail;
    }

    public void setChildEmail(String childEmail) {
        this.childEmail = childEmail;
    }

    @Override
    public String toString() {
        return "ChildActivities{" +
                "childActivityName='" + childActivityName + '\'' +
                ", activityTime='" + activityTime + '\'' +
                ", activityDate='" + activityDate + '\'' +
                ", activityDescription='" + activityDescription + '\'' +
                ", childEmail='" + childEmail + '\'' +
                '}';
    }


    ChildActivities(){}

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
