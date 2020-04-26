package com.project.coswara.model;

import com.project.coswara.Constants;

import java.io.Serializable;

import static com.project.coswara.Constants.DEFAULT_COUNTRY;
import static com.project.coswara.Constants.DEFAULT_STATE;

public class Metadata implements Serializable {
    private int age;
    private String gender;
    private int englishProficient; //1 = proficient, 0 = not proficient
    private String country;
    private String state;
    private String locality;
    private int returningUser; //0 = no, 1 = yes

    public Metadata() { //set defaults
        age = 0;
        gender = Constants.DEFAULT_GENDER;
        englishProficient = 1;
        locality = "";
        returningUser = 0;
    }

    public int getReturningUser() {
        return returningUser;
    }

    public void setReturningUser(int returningUser) {
        this.returningUser = returningUser;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getEnglishProficient() {
        return englishProficient;
    }

    public void setEnglishProficient(int englishProficient) {
        this.englishProficient = englishProficient;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public boolean isComplete(){
        return age > 0 && age < 140 && !gender.isEmpty() && !gender.equalsIgnoreCase(Constants.DEFAULT_GENDER)
                && !country.isEmpty() && !country.equalsIgnoreCase(DEFAULT_COUNTRY) && !state.isEmpty() &&
                !state.equalsIgnoreCase(DEFAULT_STATE);
    }
}
