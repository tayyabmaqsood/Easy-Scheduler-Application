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
                ", childGender='" + childGender + '\'' +
                ", childEmail='" + childEmail + '\'' +
                ", ChildPassword='" + ChildPassword + '\'' +
                '}';
    }

    private String childAge;
    private String childDOB;
    private String parentId;

    public Child(String childName, String childAge, String childDOB, String parentId, String childGender, String childEmail, String childPassword, List<ChildActivities> activities) {
        this.childName = childName;
        this.childAge = childAge;
        this.childDOB = childDOB;
        this.parentId = parentId;
        this.childGender = childGender;
        this.childEmail = childEmail;
        ChildPassword = childPassword;
        this.activities = activities;
    }

    public Child(String childName, String childAge, String childDOB, String childGender, String parentId, String childEmail, String childPassword) {
        this.childName = childName;
        this.childAge = childAge;
        this.childDOB = childDOB;
        this.parentId = parentId;
        this.childGender = childGender;
        this.childEmail = childEmail;
        ChildPassword = childPassword;

    }

    private String childGender;
    private String childEmail;
    private String ChildPassword;

    public String getChildEmail() {
        return childEmail;
    }

    public void setChildEmail(String childEmail) {
        this.childEmail = childEmail;
    }

    public String getChildPassword() {
        return ChildPassword;
    }

    public void setChildPassword(String childPassword) {
        ChildPassword = childPassword;
    }

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
