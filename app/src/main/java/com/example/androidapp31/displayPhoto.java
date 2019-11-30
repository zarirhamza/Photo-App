package com.example.androidapp31;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class displayPhoto extends AppCompatActivity {
    public int state;
    public int photoNum;

    TextView fileText, locationText, peopleText;
    ImageView imgView;

    ArrayList<Album> albumList;
    ArrayList<Photo> photoList;

    Bitmap bmImg, bMapScaled;

    //ListIterator<Photo> pIter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);

        try{
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        state = getIntent().getExtras().getInt("state");
        photoNum = getIntent().getExtras().getInt("photo");

        fileText = (TextView) findViewById(R.id.fileText);
        locationText = (TextView) findViewById(R.id.locationTag);
        peopleText = (TextView) findViewById(R.id.peopleTag);
        imgView = (ImageView) findViewById(R.id.imgView);

        initForm(state, photoNum);

        Button preButton = (Button)findViewById(R.id.prevButton);
        Button nextButton = (Button)findViewById(R.id.nextButton);
        Button locButton = (Button)findViewById(R.id.addLocation);
        Button pplButton = (Button)findViewById(R.id.addPeople);


        ArrayList<Photo> photos = albumList.get(state).getPhotos();

        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoNum > 0){
                    photoNum--;

                }
                else{
                    photoNum = albumList.get(state).listOfPhotos.size()-1;
                }
                initForm(state, photoNum );
                System.out.println(photoNum);
            }

        });

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println(photoNum);
                if(photoNum < albumList.get(state).listOfPhotos.size() - 1){
                    photoNum++;

                }
                else{
                    photoNum = 0;
                }
                initForm(state, photoNum );
                System.out.println(photoNum);
            }
        });

        locButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);


                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        v.getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                TextView promptText = (TextView) promptsView.findViewById(R.id.textView1);
                promptText.setText("If multiple tag values, separate by commas\nEntered tags will replace current tags\nType nothing to clear tags");

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String in = userInput.getText().toString();
                                        String[] values = in.split(",");
                                        albumList.get(state).getPhotos().get(photoNum).locationTags.clear();
                                        for(String s: values){
                                            albumList.get(state).getPhotos().get(photoNum).setLocationTags(s);
                                        }
                                        initForm(state,photoNum);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                v.refreshDrawableState();
                saveFile();
            }
        });

        pplButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);


                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        v.getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                TextView promptText = (TextView) promptsView.findViewById(R.id.textView1);
                promptText.setText("If multiple tag values, separate by commas\nEntered tags will replace current tags\nType nothing to clear tags");

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String in = userInput.getText().toString();
                                        String[] values = in.split(",");
                                        albumList.get(state).getPhotos().get(photoNum).personTags.clear();
                                        for(String s: values){
                                            albumList.get(state).getPhotos().get(photoNum).setPersonTags(s);
                                        }
                                        initForm(state,photoNum);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                v.refreshDrawableState();
                saveFile();
            }
        });

        //location people buttons



    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        if(state > -1){
            Intent mIntent = new Intent(displayPhoto.this, photomain.class);
            mIntent.putExtra("albumIndex", getIntent().getExtras().getInt("state"));
            displayPhoto.this.startActivity(mIntent);
            finish();
        }
        else{
            super.onSupportNavigateUp();
            finish();
        }
        return true;
    }

    public void initForm(int sp, int pn){

        photoList = albumList.get(sp).getPhotos();
        Photo p = photoList.get(pn);
        File photoFile = new File(p.getLocation());
        fileText.setText(photoFile.getName()); //filename

        if(photoFile.exists()) { //imageview
            bmImg = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            bMapScaled = Bitmap.createScaledBitmap(bmImg, 400, 300, true);
            imgView.setImageBitmap(bMapScaled);
        }
        else {
            Drawable myDrawable = getApplicationContext().getDrawable(R.drawable.one);
        }

        String locationT = ""; //location tags
        for(String s: p.getLocationTags()){
            locationT  = locationT + s + " // ";
        }
        locationText.setText("Location = " + locationT);

        String peopleT = ""; //people tags
        for(String s: p.getPersonTags()){
            peopleT  = peopleT + s + " // ";
        }
        peopleText.setText("People = " + peopleT);
    }

    public void saveFile(){
        try {
            //File f = new File("File.ser");
            FileOutputStream fos = getApplicationContext().openFileOutput("File.ser", Context.MODE_PRIVATE);
            System.out.println(getApplicationContext().getFilesDir()+ " main photo ");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(albumList);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("pausing");
        saveFile();
    }

}
