package com.example.androidapp31;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class searchPhoto extends AppCompatActivity {
    ListView simpleList;
    CustomSearchAdapter searchAdapter;

    ArrayList<Album> albumList;
    ArrayList<Photo> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);


        try {
            FileInputStream fis = this.openFileInput("File.ser");
            ObjectInputStream is = new ObjectInputStream(fis);
            albumList = (ArrayList<Album>) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        photoList = new ArrayList<Photo>();
        searchAdapter = new CustomSearchAdapter(photoList, searchPhoto.this);

        Toolbar toolbar = findViewById(R.id.toolbarS);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        simpleList = (ListView) findViewById(R.id.searchList);



        TextInputEditText lText = (TextInputEditText) findViewById(R.id.firstTag);
        TextInputLayout lTLayout = (TextInputLayout) findViewById(R.id.locationTextLayout);

        Spinner spinnerC = (Spinner) findViewById(R.id.spinnerC);
        List<String> list = new ArrayList<String>();
        list.add("N/A");
        list.add("AND");
        list.add("OR");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC.setAdapter(dataAdapter);

        TextInputEditText pText = (TextInputEditText) findViewById(R.id.secondTag);
        TextInputLayout pTLayout = (TextInputLayout) findViewById(R.id.personTextLayout);

        Button searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!validInput(lText.getText().toString(), pText.getText().toString(), spinnerC.getSelectedItem().toString())){
                    new AlertDialog.Builder(searchPhoto.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Search Error")
                            .setMessage("Invalid input! Valid tags are only person or location in the form person=value or location = value" )
                            .setPositiveButton("Ok", null)
                            .show();
                }else {
                    photoList = getSearchResults(lText.getText().toString(), pText.getText().toString(), spinnerC.getSelectedItem().toString());
                    searchAdapter = new CustomSearchAdapter(photoList, searchPhoto.this);
                    simpleList.setItemsCanFocus(false);
                    simpleList.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                    System.out.println(reString(lText.getText().toString()) + reString(pText.getText().toString()));
                }
            }
        });

        simpleList.setAdapter(searchAdapter);

    }


    public ArrayList<Photo> getSearchResults(String fTag, String sTag, String choice){
        ArrayList<Photo>searchResults = new ArrayList<Photo>();
        String type1 = stringType(fTag);
        String val1  = reString(fTag);

        String type2 = stringType(sTag);
        String val2  = reString(sTag);
        if(choice.equals("N/A")){
            for(Album a: albumList){
                for(Photo p : a.getPhotos()){
                    if(type1.equals("location")){
                        for(String s : p.locationTags){
                            if(s.contains(val1))
                                searchResults.add(p);
                        }
                    }
                    else if(type1.equals("person")){
                        for(String s : p.personTags){
                            if(s.contains(val1))
                                searchResults.add(p);
                        }
                    }
                }
            }

            for(Photo px:searchResults)
                System.out.println(px.getLocation());

            return searchResults;
        }
        else if(choice.equals("OR")){
            for(Album a: albumList){
                for(Photo p : a.getPhotos()){
                    //check for one and check for other and only add if unique
                    if(type1.equals("location")){
                        for(String s : p.locationTags){
                            if(s.contains(val1)) {
                                if(!searchResults.contains(p))
                                    searchResults.add(p);
                            }
                        }
                    }
                    else if(type1.equals("person")){
                        for(String s : p.personTags){
                            if(s.contains(val1)) {
                                if(!searchResults.contains(p))
                                    searchResults.add(p);
                            }
                        }
                    }

                    if(type2.equals("location")){
                        for(String s : p.locationTags){
                            if(s.contains(val2)) {
                                if(!searchResults.contains(p))
                                    searchResults.add(p);
                            }
                        }
                    }
                    else if(type2.equals("person")){
                        for(String s : p.personTags){
                            if(s.contains(val2)) {
                                if(!searchResults.contains(p))
                                    searchResults.add(p);
                            }
                        }
                    }
                }
            }
            for(Photo px:searchResults)
                System.out.println(px.getLocation());

            return searchResults;

        }
        else if(choice.equals("AND")){
            for(Album a: albumList){
                for(Photo p : a.getPhotos()){
                    if(type1.equals("location")){
                        for(String s : p.locationTags){
                            if(s.contains(val1))
                                searchResults.add(p);
                        }
                    }
                    else if(type1.equals("person")){
                        for(String s : p.personTags){
                            if(s.contains(val1))
                                searchResults.add(p);
                        }
                    }
                }
            }
            for(Iterator<Photo> pxi = searchResults.iterator(); pxi.hasNext();) {
                Photo px = pxi.next();
                boolean found = false;
                if(type2.equals("location")){
                    for(String s : px.locationTags){
                        if(s.contains(val2)) {
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        pxi.remove();
                    }
                }
                else if(type2.equals("person")){
                    for(String s : px.personTags){
                        if(s.contains(val2)) {
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        pxi.remove();
                    }
                }
            }

            for(Photo px:searchResults)
                System.out.println(px.getLocation());
            return searchResults;
        }
        return null;
    }

    public String reString(String lpTag){
        String res = "";
        if(lpTag.isEmpty())
            return res;

        String[] lptags = lpTag.split("=");
        res = lptags[1];
        if(res.charAt(0) == ' ')
            res = res.substring(1);

        return res;
    }

    public String stringType(String lpTag){
        String res = "";
        if(lpTag.isEmpty())
            return res;

        String[] lptags = lpTag.split("=");
        res = lptags[0];
        res = res.replaceAll("\\s+", "");
        return res.toLowerCase();
    }

    public boolean validInput(String fTag, String sTag, String choice ){
        if(fTag.isEmpty())
            return false;
        String[] fTagSplit = fTag.split("=");
        if(fTagSplit.length < 2){ //no first argument
            System.out.println("here");
            return false;
        }
        fTagSplit[0] = fTagSplit[0].replaceAll("\\s+", "");
        if(!fTagSplit[0].toLowerCase().equals("location") && !fTagSplit[0].toLowerCase().equals("person")){ //not valid tag
            System.out.println(fTagSplit[0].toLowerCase() + "here2");
            return false;
        }

        if(!choice.equals("N/A") && sTag.isEmpty())//no second input
            return false;

        if(!choice.equals("N/A")) {
            String[] sTagSplit = sTag.split("=");
            if (sTagSplit.length < 2) { //no first argument
                return false;
            }
            sTagSplit[0] = sTagSplit[0].replaceAll("\\s+", "");
            if (!sTagSplit[0].toLowerCase().equals("location") && !sTagSplit[0].toLowerCase().equals("person")) { //not valid tag
                return false;
            }
        }

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("pausing photo");
        //saveFile();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Intent mIntent = new Intent(searchPhoto.this, MainActivity.class);
        searchPhoto.this.startActivity(mIntent);
        return true;
    }

}
