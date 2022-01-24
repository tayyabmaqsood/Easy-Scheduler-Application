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

    private String childName;
    private String childAge;
    private String childDOB;

    public Child(String childName, String childAge, String childDOB) {
        this.childName = childName;
        this.childAge = childAge;
        this.childDOB = childDOB;
    }
}
