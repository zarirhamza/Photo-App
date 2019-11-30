package com.example.androidapp31;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    public ArrayList<Photo> listOfPhotos;
    public String name;

    public Album(String name){
        this.name = name;
        this.listOfPhotos = new ArrayList<Photo>();
    }

    public Album(String name, ArrayList<Photo>listOfPhotos){
        this.name = name;
        this.listOfPhotos = listOfPhotos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Photo> getPhotos(){
        return this.listOfPhotos;
    }

    public void addPhoto(String location){
        listOfPhotos.add(new Photo(location));
    }
    public void addPhoto(Photo p){
        listOfPhotos.add(p);
    }
}
