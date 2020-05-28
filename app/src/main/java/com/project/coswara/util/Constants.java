package com.project.coswara.util;

import android.content.Context;

import com.project.coswara.R;

public class Constants {

    /*
     * PRE-DEFINED STRING CONSTANTS
     */

    /*
        DETAILS SECTION
     */
    public static final String INFO_CONTEXT = "We need some information to continue!";

    /*
        DETAILS : METADATA
     */
    public static final String DEFAULT_GENDER = "Gender*";
    public static final String DEFAULT_COUNTRY_EN = "Country*";
    public static final String DEFAULT_STATE_EN = "State/Province*";
    public static final String[] GENDERS = {DEFAULT_GENDER, "Male", "Female", "Other"};

    /*
        DETAILS : HEALTH STATUS
     */
    public static final String[] HEALTH_STATUSES = {"positive_mild", "positive_moderate", "recovered_full",
            "resp_illness_not_identified", "no_resp_illness_exposed", "healthy"};

    public static final String[] CONDITIONS = {"cold", "cough", "fever", "pneumonia", "smoker", "asthma",
            "cld", "ht", "ihd", "diabetes"};

    /*
        RECORD AUDIO
     */
    public static final String[] VALUES_LIST = {"breathing-shallow", "breathing-deep", "cough-shallow",
            "cough-heavy", "vowel-a", "vowel-e", "vowel-o", "counting-normal", "counting-fast", "done"};

    /*
        SHARED PREF KEYS
     */
    static final String REC_TRACK_COMPLETION_PREFS = "rec_track_complete"; //to track boolean[] status of recordings - to see how many recordings already completed so far
    static final String REC_COMPLETED_PREFS = "rec_completed"; //flag to check if recordings completed

    static final String METADATA_PREFS = "metadata"; //to store Metadata object
    static final String HEALTH_DATA_PREFS = "healthdata"; //to store HealthData object
    static final String DETAILS_PREFS = "details"; //to check if details have been submitted (metadata info)
    static final String HASHMAP_PREFS = "hashmap_data"; //to store hashmaps sent to Firestore (like userAppData)

    static final String FEEDBACK_PREFS = "feedback"; //to check if feedback was submitted

    static final String LANGUAGE_PREFS = "language"; //store language to display text

    /*
        EXTERNAL URLS USED
     */
    public static final String READ_MORE_URL = "https://coswara.iisc.ac.in/about";
    public static final String LEAP_URL = "http://leap.ee.iisc.ac.in/";
    public static final String IISC_URL = "https://www.iisc.ac.in/";
    public static final String COGKNIT_URL = "https://www.cogknit.com/";
    public static final String FB_URL = "https://www.facebook.com/coswara.iisc";
    public static final String TWITTER_URL = "https://twitter.com/coswara_iisc";
    public static final String COSWARA_TERMS_URL = "https://coswara.iisc.ac.in/terms";
}
