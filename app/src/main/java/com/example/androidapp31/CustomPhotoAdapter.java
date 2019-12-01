package com.example.androidapp31;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomPhotoAdapter extends ArrayAdapter<Photo> {
    private ArrayList<Photo> photoSet;
    private ArrayList<Album> albumList;
    Context mContext;

    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Bitmap bmImg, bMapScaled;
    private int index;


    private static class ViewHolder{
        ImageView thumbnail;
        Button delButton;
        Button openButton;
        Button moveButton;
    }

    public CustomPhotoAdapter(ArrayList<Album> albumlist, int index, Context context) {
        super(context, R.layout.activity_photo_list_view, albumlist.get(index).getPhotos());
        this.photoSet = albumlist.get(index).getPhotos();
        this.albumList = albumlist;
        this.mContext=context;
        this.index = index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.activity_photo_list_view, parent, false);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) row.findViewById(R.id.searchThumbNail);
            holder.delButton = (Button) row.findViewById(R.id.deletePButton);
            holder.openButton = (Button) row.findViewById(R.id.openPButton);
            holder.moveButton = (Button) row.findViewById(R.id.movePButton);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final Photo photo = photoSet.get(position);

        //String path = mContext.getExternalFilesDir(null).getAbsolutePath();
        //String pathPhotos = path +"/test.jpg";
        System.out.println(photo.getLocation());
        File imgFile = new File(photo.getLocation());
        if(imgFile.exists()) {
                bmImg = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                bMapScaled = Bitmap.createScaledBitmap(bmImg, 100, 80, true);
                holder.thumbnail.setImageBitmap(bMapScaled);
        }
        else {
            Drawable myDrawable = mContext.getApplicationContext().getDrawable(R.drawable.one);
        }
        holder.delButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Photo")
                        .setMessage("Are you sure you want to delete this photo?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                photoSet.remove(photo);
                                notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

                v.refreshDrawableState();
                saveFile();
            }
        });


        holder.openButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, displayPhoto.class);
                for(int a = 0; a < albumList.size(); a++){
                    if(albumList.get(a).getName().equals(albumList.get(index).getName())){
                        for(int b= 0; b < albumList.get(index).listOfPhotos.size(); b++){
                            if(albumList.get(index).listOfPhotos.get(b).equals(photo)){
                                mIntent.putExtra("state", a);
                                mIntent.putExtra("photo", b);
                            }
                        }
                        mContext.startActivity(mIntent);
                        ((Activity)mContext).finish();
                    }
                }
                mContext.startActivity(mIntent);
                saveFile();
            }
        });


        holder.moveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                b.setTitle("Move location");
                ArrayList<String> alName = new ArrayList<>();
                for(Album a : albumList){
                    if(!a.getName().equals(albumList.get(index).getName()))
                        alName.add(a.getName());
                }
                String[] types = Arrays.copyOf(alName.toArray(),
                                albumList.size()-1,
                                String[].class);

                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        for(int a = 0; a < alName.size(); a++){
                            if(a  == which) {
                                for(Album ab : albumList){
                                    if(ab.getName().equals(alName.get(which))){
                                        Toast.makeText(mContext, "Moved to " + ab.getName(),
                                                Toast.LENGTH_LONG).show();
                                        Photo temp = new Photo(new String(photo.getLocation()), new ArrayList<String>(photo.getPersonTags()) , new ArrayList<String>(photo.getLocationTags()));
                                        ab.addPhoto(temp);
                                    }
                                }
                            }
                        }
                    }
                });

                b.show();
                saveFile();
            }
        });

        return row;
    }

    public void saveFile(){
        try {
            FileOutputStream fos = mContext.getApplicationContext().openFileOutput("File.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(albumList);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
