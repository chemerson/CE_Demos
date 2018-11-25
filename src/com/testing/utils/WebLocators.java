package com.testing.utils;

public class WebLocators {

    public final class Home {
        public static final String ABOUTUS = ".//*[@id='globalnav']/a[1]";
        public static final String HOMEBECOMEMEMBER = ".//*[@id='contmain']/div[2]/a[2]";
    }

    public final class SignUp {
        public static final String BECOMEMEMBER = ".//*[@id='center-rail-nfo-full']/div[4]/div[1]/a";
    }

    public final class SignUpForm {
        public static final String CITIZENSHIPSTATUS = ".//*[@id='memberEligibility:applicant-citizenshipStatus']";
        public static final String FIRSTNAME = ".//*[@id='memberEligibility:applicant-firstName']";
        public static final String LASTNAME = ".//*[@id=\'memberEligibility:applicant-lastName\']";
        public static final String SSN = ".//*[@id='memberEligibility:applicant-ssn']";
        public static final String CONFIRMSSN = ".//*[@id='memberEligibility:applicant-ssn-confirm']";
        public static final String MONTH = ".//*[@id='memberEligibility:applicant-month']";
        public static final String DAY = ".//*[@id='memberEligibility:applicant-day']";
        public static final String YEAR = ".//*[@id='memberEligibility:applicant-year']";
        public static final String GENDER = ".//*[@id='memberEligibility:applicant-gender:0']";
        public static final String CONTINUE = ".//*[@id='memberEligibility:btn-continue']";
        public static final String ELIGIBILITY = ".//*[@id='memberEligibility:applicant-eligibility']";
        public static final String AFFILIATION = ".//*[@id='memberEligibility:applicant-affiliation-civilian']";
        public static final String EMPLOYMENTSTATUS = ".//*[@id='memberEligibility:applicant-employmentStatus-civilian']";

    }

    public final class About {
        public static final String DLPUBS = "//*[@id='center-rail-nfo-full']/div[13]/ul/li[7]/a";
    }

    public final class PublicationsandForms {
        public static final String BALCHECKINGACCNT = ".//*[@id='CHECKING_container']/div[2]/div[1]/ul/li[2]/a";
    }
}
