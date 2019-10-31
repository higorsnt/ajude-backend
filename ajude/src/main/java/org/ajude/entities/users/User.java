package org.ajude.entities.users;

import javax.persistence.Entity;

@Entity
public class User {

    private String firstName;
    private String lastName;
    @Id
    private String email;
    private String creditCardNumber;
    private String password;

    public User(String firstName, String lastName, String email, String creditCardNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
