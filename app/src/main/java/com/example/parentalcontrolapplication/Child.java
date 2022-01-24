package com.example.parentalcontrolapplication;

public class Child {
    private String parentsId;

    public String getParentsId() {
        return parentsId;
    }

    public void setParentsId(String parentsId) {
        this.parentsId = parentsId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public String getChildDOB() {
        return childDOB;
    }

    public void setChildDOB(String childDOB) {
        this.childDOB = childDOB;
    }



    public Child(String childName, String childAge, String childDOB) {
        this.childName = childName;
        this.childAge = childAge;
        this.childDOB = childDOB;
    }


    public String getChildActivity() {
        return childActivity;
    }

    public void setChildActivity(String childActivity) {
        this.childActivity = childActivity;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    private String childName;
    private String childAge;
    private String childDOB;
    private String childActivity;
    private String activityTime;
    private String activityDate;
    private String activityDescription;
    private String parentId;
}
