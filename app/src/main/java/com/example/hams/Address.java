package com.example.hams;

public class Address {

    private String addressLine;
    private String postalCode;
    private String country;
    private String province;
    private String city;

    public Address(){
        addressLine = "";
        postalCode = "";
        country = "";
        province = "";
        city = "";
    }

    public Address(String addressLine, String postalCode, String country, String province, String city) {
        this.addressLine = addressLine;
        this.postalCode = postalCode;
        this.country = country;
        this.province = province;
        this.city = city;
    }


    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressLine='" + addressLine + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}



