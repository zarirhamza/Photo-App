package com.example.androidapp31;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class photomain extends AppCompatActivity {
    public int stateNum;
    ListView simpleList;
    CustomPhotoAdapter photoAdapter;

    ArrayList<Album> albumList;
    ArrayList<Photo> photoList;

    private static int RESULT_LOAD_IMAGE = 1;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photomain); //activity_main

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
       /* albumList = new ArrayList<Album>();
        albumList.add(new Album("Test"));
        albumList.add(new Album("Test1"));
        albumList.add(new Album("Test2"));
        albumList.add(new Album("Tes t3"));
        albumList.add(new Album("Test4"));
        albumList.add(new Album("Test6"));
        albumList.add(new Album("Test7"));
        albumList.add(new Album("Test8"));*/


        simpleList = (ListView) findViewById(R.id.List);

        photoList = albumList.get(getIntent().getExtras().getInt("albumIndex")).getPhotos();
        //photoList.add(new Photo("/storage/emulated/0/Android/data/com.example.androidapp31/files/test.jpg"));
        /*photoList.add(new Photo("./drawable/two.jpg"));
        photoList.add(new Photo("./drawable/three.jpeg"));
        photoList.add(new Photo("./drawable/four.jpeg"));
        photoList.add(new Photo("./drawable/five.jpg"));*/

        photoAdapter = new CustomPhotoAdapter(albumList, getIntent().getExtras().getInt("albumIndex"), photomain.this);
        simpleList = (ListView) findViewById(R.id.PList);
        Button addPButton = (Button) findViewById(R.id.addPButton);

        addPButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                saveFile();
            }
        });
        simpleList.setItemsCanFocus(false);
        simpleList.setAdapter(photoAdapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            Photo p = new Photo(filePath);
            photoList.add(p);
            photoAdapter.notifyDataSetChanged();
            saveFile();
        }
    }

    public void saveFile(){
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("File.ser", Context.MODE_PRIVATE);
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
        System.out.println("pausing photo");
        saveFile();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent mIntent = new Intent(photomain.this, MainActivity.class);
        photomain.this.startActivity(mIntent);
        return true;
    }
}

