package com.demo.resy;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Job {
    private String jobname;
    private String durationOfactivity;
    private String jobdescription;
    private ObservableList<String> jobskills;
    private ObservableList<String> categorys;
    private String companyname;
    private String name;

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    private String jobid;

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getDurationOfactivity() {
        return durationOfactivity;
    }

    public void setDurationOfactivity(String durationOfactivity) {
        this.durationOfactivity = durationOfactivity;
    }

    public String getJobdescription() {
        return jobdescription;
    }

    public ObservableList<String> getJobskills() {
        return jobskills;
    }

    public void setJobskills(ObservableList<String> jobskills) {
        this.jobskills = jobskills;
    }

    public ObservableList<String> getCategorys() {
        return categorys;
    }

    public void setCategorys(ObservableList<String> categorys) {
        this.categorys = categorys;
    }

    public void setJobdescription(String jobdescription) {
        this.jobdescription = jobdescription;
    }



    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelnr() {
        return telnr;
    }

    public void setTelnr(String telnr) {
        this.telnr = telnr;
    }

    public String getStrnr() {
        return strnr;
    }

    public void setStrnr(String strnr) {
        this.strnr = strnr;
    }

    public String getPlzort() {
        return plzort;
    }

    public void setPlzort(String plzort) {
        this.plzort = plzort;
    }

    private String vorname;

    public Job(String jobname, String durationOfactivity, String jobdescription, ObservableList<String> jobskills, ObservableList<String> categorys, String companyname, String name, String vorname, String email, String telnr, String strnr, String plzort) {
        this.jobname = jobname;
        this.durationOfactivity = durationOfactivity;
        this.jobdescription = jobdescription;
        this.jobskills = jobskills;
        this.categorys = categorys;
        this.companyname = companyname;
        this.name = name;
        this.vorname = vorname;
        this.email = email;
        this.telnr = telnr;
        this.strnr = strnr;
        this.plzort = plzort;
    }

    private String email;
    private String telnr;
    private String strnr;
    private String plzort;
}
