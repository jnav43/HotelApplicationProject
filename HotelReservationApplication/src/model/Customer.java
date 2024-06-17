package model;

import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    private final String emailRegex = "^(.+)@(.+).com$";
    private final Pattern pattern = Pattern.compile(emailRegex);

    public Customer(String email, String firstName, String lastName) {
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Error, Invalid email");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }

    public String toString(){
        return "Customer{" +
                "Name: " + '\'' + firstName + " " + lastName + '\'' +
                ", Email: '" + email + '\'' + '}';
    }
}
