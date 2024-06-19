package edu.cscc.jpaexercise.jpaexercise.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity(name = "user_addresses")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty private String street;
    @NotEmpty private String city;
    @NotEmpty @Size(min = 2, max = 2) private String state;
    @NotEmpty @Size(min = 5, max=5) private String zip;

    @ManyToOne(
        fetch = FetchType.EAGER,
        optional = false
    )
    @JoinColumn(name = "user_profile_id")
    @JsonIgnoreProperties("userAddresses")
    private UserProfile userProfile;

    public UserAddress() {}

    public UserAddress(UserProfile userProfile, String street, String city, String state, String zip) {
        this.userProfile = userProfile;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public UserAddress(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public UserProfile getUser() {
        return userProfile;
    }

    public void setUser(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAddress that = (UserAddress) o;
        return Objects.equals(id, that.id) && Objects.equals(street, that.street) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(zip, that.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, city, state, zip);
    }

    @Override
    public String toString() {
        return "UserAddress{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
