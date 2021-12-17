package com.demo.resy;


public class Skill {
    private String skillname;
    private String description;
    private String category;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Skill(String skillname, String description, String category) {
        this.skillname = skillname;
        this.description = description;
        this.category = category;
    }
}
