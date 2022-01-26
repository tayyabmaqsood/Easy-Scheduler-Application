package com.example.parentalcontrolapplication;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class Child {




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

    public Child(){}

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }










    public List<ChildActivities> getActivities() {
        return activities;
    }

    public void setActivities(List<ChildActivities> activities) {
        this.activities = activities;
    }

    private String childName;

    @Override
    public String toString() {
        return "Child{" +
                "childName='" + childName + '\'' +
                ", childAge='" + childAge + '\'' +
                ", childDOB='" + childDOB + '\'' +
                ", parentId='" + parentId + '\'' +
                ", childGender='" + childGender + '\'' +
                '}';
    }

    private String childAge;
    private String childDOB;
    private String parentId;
    private String childGender;
    private List<ChildActivities>activities =  new ArrayList<ChildActivities>();

    public Child(String childName, String childAge, String childDOB,  String childGender, String parentId, List<ChildActivities> activities) {
        this.childName = childName;
        this.childAge = childAge;
        this.childDOB = childDOB;
        this.parentId = parentId;
        this.childGender = childGender;
        this.activities = activities;
    }

    public String getChildGender() {
        return childGender;
    }

    public void setChildGender(String childGender) {
        this.childGender = childGender;
    }
}
