package com.mysite.customers;

public class ExpectedErrorMessages {
    public static class FirstName {
        public static final String REQUIRED = "First name must have a value.";
        public static final String SIZE = "First name must be between 1 and 75 characters long.";
    }

    public static class LastName {
        public static final String REQUIRED = "Last name must have a value.";
        public static final String SIZE = "Last name must be between 1 and 50 characters long.";
    }

    public static class Email {
        public static final String REQUIRED = "E-mail must have a value.";
        public static final String SIZE = "E-mail must be between 5 and 320 characters long.";
        public static final String FORMAT = "E-mail has an incorrect format. See the API documentation for the correct format.";
    }
}
