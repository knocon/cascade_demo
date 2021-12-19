package com.demo.resy;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Job {
    private String jobtitle;

    public String getJobtitle() {
        return jobtitle;
    }

    public Job(String jobtitel, String company, String location, String experience, String salary, String jobdescription) {
        this.jobtitle = jobtitel;
        this.company = company;
        this.location = location;
        this.experience = experience;
        this.salary = salary;
        this.jobdescription = jobdescription;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobdescription() {
        return jobdescription;
    }

    public void setJobdescription(String jobdescription) {
        this.jobdescription = jobdescription;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    private String jobid;
    private String company;
    private String location;
    private String experience;
    private String salary;
    private String jobdescription;
}
