package com.demo.resy;

public class Recommendation extends Job{
    private String debugcode;
    private int rating;

    public Recommendation(String jobtitel, String company, String location, String experience, String salary, String jobdescription, String debugcode, int rating) {
        super(jobtitel, company, location, experience, salary, jobdescription, rating);
        this.debugcode = debugcode;
    }

    public String getDebugcode() {
        return debugcode;
    }

    public void setDebugcode(String debugcode) {
        this.debugcode = debugcode;
    }
}
