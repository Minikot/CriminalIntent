package com.aleksandr.criminalintent.model;

/**
 * Created by Aleksandr on 15.05.17.
 */

public class CrimeTable {
    public static final String NAME = "crimes";

    public static class Cols{
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String SOLVED = "solved";
        public static final String SUSPECT = "suspect";
    }
}
