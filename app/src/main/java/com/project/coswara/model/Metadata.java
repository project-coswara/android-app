package com.project.coswara.model;

import com.project.coswara.util.Constants;

import java.io.Serializable;

import lombok.Data;

import static com.project.coswara.util.Constants.DEFAULT_COUNTRY_EN;
import static com.project.coswara.util.Constants.DEFAULT_STATE_EN;

@Data
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

    public boolean isComplete(){
        return age > 0 && age < 140 && !gender.isEmpty() && !gender.equalsIgnoreCase(Constants.DEFAULT_GENDER)
                && !country.isEmpty() && !country.equalsIgnoreCase(DEFAULT_COUNTRY_EN) && !state.isEmpty() &&
                !state.equalsIgnoreCase(DEFAULT_STATE_EN);
    }
}
