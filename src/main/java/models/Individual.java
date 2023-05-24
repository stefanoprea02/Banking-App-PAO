package models;

import java.util.ArrayList;

public class Individual extends Customer{
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;

    public Individual(String id,
                      ArrayList<Account> accounts,
                      String firstName, String lastName,
                      String address,
                      String phoneNumber,
                      String email) {
        super(id, accounts);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "\nIndividual{" +
                "\n    firstName=" + firstName +
                "\n    lastName=" + lastName +
                "\n    address=" + address +
                "\n    phoneNumber=" + phoneNumber +
                "\n    email=" + email +
                "\n}";
    }
}
