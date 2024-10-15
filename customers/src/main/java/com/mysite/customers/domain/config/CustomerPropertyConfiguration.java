package com.mysite.customers.domain.config;

public class CustomerPropertyConfiguration {
    public static class FirstName {
        public static final int MIN_SIZE = 1;
        public static final int MAX_SIZE = 75;
    }

    public static class LastName {
        public static final int MIN_SIZE = 1;
        public static final int MAX_SIZE = 50;
    }

    public static class Email {
        public static final int MIN_SIZE = 5;
        public static final int MAX_SIZE = 320;
    }
}
