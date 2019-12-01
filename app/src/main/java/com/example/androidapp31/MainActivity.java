package com.example.androidapp31;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView simpleList;
    CustomAlbumAdapter albumAdapter;
    public ArrayList<Album> albumList;

    /*CustomPhotoAdapter photoAdapter;
    ArrayList<Photo> photoList;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //activity_main

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this,new String[] { Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        try{
            FileInputStream fis = this.openFileInput("File.ser");
            ObjectInputStream is = new ObjectInputStream(fis);
            albumList = (ArrayList<Album>) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            albumList = new ArrayList<Album>();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            albumList = new ArrayList<Album>();
            e.printStackTrace();
        }
        /*albumList = new ArrayList<Album>();
        Album al = new Album("Test");
        al.addPhoto(("/storage/emulated/0/Android/data/com.example.androidapp31/files/test.jpg"));
        albumList.add(al);
        /*albumList.add(new Album("Test1"));
        albumList.add(new Album("Test2"));
        albumList.add(new Album("Tes t3"));
        albumList.add(new Album("Test4"));
        albumList.add(new Album("Test6"));
        albumList.add(new Album("Test7"));
        albumList.add(new Album("Test8"));*/

        albumAdapter = new CustomAlbumAdapter(albumList, MainActivity.this);

        simpleList = (ListView) findViewById(R.id.List);
        Button addButton = (Button) findViewById(R.id.addButton);
        Button searchButton = (Button) findViewById(R.id.searchButton);



        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(MainActivity.this,searchPhoto.class);
                MainActivity.this.startActivity(mIntent);
                finish();
            }
        });



        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        System.out.println(albumAdapter.albumSet.size() + " size is");
                                        for ( Album a: albumList) {
                                            if(a.getName().equals(userInput.getText().toString())) {
                                                Toast.makeText(MainActivity.this, "Album Name Already Exists!!!",
                                                        Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        }
                                        albumList.add(new Album(userInput.getText().toString()));
                                        //albumAdapter.notifyDataSetInvalidated();
                                        albumAdapter.notifyDataSetChanged();

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

        simpleList.setItemsCanFocus(false);
        simpleList.setAdapter(albumAdapter);
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

