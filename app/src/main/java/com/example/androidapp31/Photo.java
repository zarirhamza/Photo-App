package com.example.androidapp31;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {
    private String location;
    public ArrayList<String> personTags;
    public ArrayList<String> locationTags;


    public Photo(String location){
        this.setLocation(location);
        this.personTags = new ArrayList<String>();
        this.locationTags = new ArrayList<String>();
    }

    public Photo(String location, ArrayList<String> personTags, ArrayList<String> locationTags){
        this.setLocation(location);
        this.personTags = personTags;
        this.locationTags = locationTags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getPersonTags() {
        return personTags;
    }

    public void setPersonTags(String personTags) {
        this.personTags.add(personTags);
    }

    public ArrayList<String> getLocationTags() {
        return locationTags;
    }

    public void setLocationTags(String locationTags) {
        this.locationTags.add(locationTags);
    }
}
