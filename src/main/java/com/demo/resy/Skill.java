package com.demo.resy;


public class Skill {
    private String skillname;
    private String description;

    public String getSkillname() {
        return skillname;
    }

    public void setSkillname(String skillname) {
        this.skillname = skillname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Skill(String skillname, String description) {
        this.skillname = skillname;
        this.description = description;

    }
}
