package com.project.coswara;

public class Constants {

    public static final boolean isProd = false;

    /*
     * PRE-DEFINED STRING CONSTANTS
     */

    /*
        INTRO SECTION
     */
    public static final String INTRO_CONTENT = "Project Coswara by the <a href=\"https://www.iisc.ac.in/\">Indian Institute of Science (IISc)</a> Bangalore " +
            "is an attempt to build a diagnostic tool for Covid–19 based on respiratory, cough and speech sounds. The " +
            "project is in the data collection stage now. It requires the participants to provide a recording of " +
            "breathing sounds, cough sounds, sustained phonation of vowel sounds and a counting exercise which " +
            "takes around 5–7 minutes of your time. No personally identifiable data is collected from the participants.";

    /*
        DETAILS SECTION
     */
    public static final String INFO_CONTEXT = "We need some information to continue!";
    public static final String[] TAB_TITLES = new String[]{"Disclaimer", "Metadata", "Health Status"};

    /*
        DETAILS : DISCLAIMER
     */
    public static final String BODY_1 = "This study builds on other previous studies which have reported a high level of sensitivity " +
            "for identifying respiratory illness from the human voice. Any tools with statistical significance " +
            "in disease detection will also be implemented as part of this app at a later date. The participation " +
            "in this study is purely voluntary and the details collected will not contain any personally " +
            "identifiable information. <br/><br/>";

    public static final String BODY_2 = "The entire recording and data collection should take about 5 minutes only. " +
            "Please keep your device around 20 cm from your face during the recording and perform the " +
            "recording in a quiet environment. The recording device should not be shared with others " +
            "before cleaning/sanitation. <br/><br/>";

    public static final String BODY_3 = "You have accepted our <a href=\"https://coswara.iisc.ac.in/terms\">terms and conditions</a>.";

    public static final String BODY_4 = "For any questions, please contact <a href=\"mailto:sriramg@iisc.ac.in\">sriramg@iisc.ac.in</a>";

    /*
        DETAILS : METADATA
     */
    public static final String DEFAULT_GENDER = "Gender*";
    public static final String DEFAULT_COUNTRY = "Country*";
    public static final String DEFAULT_STATE = "State/Province*";
    public static final String[] COUNTRIES = {DEFAULT_COUNTRY, "India", "China", "France", "Italy", "Spain", "United Kingdom", "United States", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua And Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas The", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Republic Of The Congo", "Democratic Republic Of The Congo", "Cook Islands", "Costa Rica", "Cote DIvoire (Ivory Coast)", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "External Territories of Australia", "Falkland Islands", "Faroe Islands", "Fiji Islands", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia The", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey and Alderney", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and McDonald Islands", "Honduras", "Hong Kong S.A.R.", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea North", "Korea South", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau S.A.R.", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Man (Isle of)", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands Antilles", "Netherlands The", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territory Occupied", "Panama", "Papua new Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Island", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Saint Helena", "Saint Kitts And Nevis", "Saint Lucia", "Saint Pierre and Miquelon", "Saint Vincent And The Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Smaller Territories of the UK", "Solomon Islands", "Somalia", "South Africa", "South Georgia", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard And Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad And Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks And Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City State (Holy See)", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (US)", "Wallis And Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};
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
    public static final String[] OPTIONS_LIST = {"Breathing (shallow)", "Breathing (deep)", "Cough (shallow)",
            "Cough (heavy)", "Vowel /a/", "Vowel /e/", "Vowel /o/", "Counting (normal)", "Counting (fast)",
            "Finished"};

    public static final String[] VALUES_LIST = {"breathing-shallow", "breathing-deep", "cough-shallow",
            "cough-heavy", "vowel-a", "vowel-e", "vowel-o", "counting-normal", "counting-fast", "done"};

    public static final String[] INSTRUCTIONS_LIST = {"Breathe fast 5 times", "Breathe deeply 5 times",
            "Cough mildly 3 times", "Cough deeply 3 times",
            "Say /a/ as in 'made' and sustain as long as possible",
            "Say /e/ as in \'beet\' and sustain as long as possible",
            "Say /o/ as in \'cool\' and sustain as long as possible",
            "Count from 1 to 20", "Count from 1 to 20 faster",
            "Thank you for your participation!"};

    /*
        FEEDBACK
     */
    public static final String FEEDBACK_TEXT = "Thank you for being a part of this!\n" +
            "Please share it among your friends and relatives.\n" +
            "Follow us at <a href=\"https://www.facebook.com/coswara.iisc\">Facebook</a> " +
            "and <a href=\"https://twitter.com/coswara_iisc\">Twitter</a> to get the latest updates.";

    /*
        SHARED PREF KEYS
     */
    public static final String REC_TRACK_COMPLETION_PREFS = "rec_track_complete"; //to track boolean[] status of recordings - to see how many recordings already completed so far
    public static final String REC_COMPLETED_PREFS = "rec_completed"; //flag to check if recordings completed

    public static final String METADATA_PREFS = "metadata"; //to store Metadata object
    public static final String HEALTH_DATA_PREFS = "healthdata"; //to store HealthData object
    public static final String DETAILS_PREFS = "details"; //to check if details have been submitted (metadata info)
    public static final String HASHMAP_PREFS = "hashmap_data"; //to store hashmaps sent to Firestore (like userAppData)

    public static final String FEEDBACK_PREFS = "feedback"; //to check if feedback was submitted

    public static final String GENERAL_PREFS = "general_info"; //store general info in these prefs

    /*
        EXTERNAL URLS USED
     */
    public static final String READ_MORE_URL = "https://coswara.iisc.ac.in/about";
    public static final String LEAP_URL = "http://leap.ee.iisc.ac.in/";
    public static final String IISC_URL = "https://www.iisc.ac.in/";
    public static final String COGKNIT_URL = "https://www.cogknit.com/";
}
