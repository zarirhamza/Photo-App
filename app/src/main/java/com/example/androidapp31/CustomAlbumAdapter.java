package com.example.androidapp31;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.widget.Toast;

public class CustomAlbumAdapter extends ArrayAdapter<Album> {
    public ArrayList<Album> albumSet = new ArrayList<Album>();
    Context mContext;

    private static class ViewHolder{
        TextView albumTxt;
        Button editButton;
        Button delButton;
        Button openButton;
    }

    public CustomAlbumAdapter(ArrayList<Album> data, Context context) {
        super(context, R.layout.activity_list_view, data);
        this.albumSet = data;
        this.mContext=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.activity_list_view, parent, false);
            holder = new ViewHolder();
            holder.albumTxt = (TextView) row.findViewById(R.id.albumText);
            holder.editButton = (Button) row.findViewById(R.id.editButton);
            holder.openButton = (Button) row.findViewById(R.id.openButton);
            holder.delButton = (Button) row.findViewById(R.id.deleteButton);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Album album = albumSet.get(position);
        holder.albumTxt.setText(album.getName());
        holder.editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);


                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(mContext);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        mContext);

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
                                        for ( Album a: albumSet) {
                                            if(a.getName().equals(userInput.getText().toString())) {

                                                Toast.makeText(mContext, "Album Name Already Exists!!!",
                                                        Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        }
                                        album.setName(userInput.getText().toString());
                                        notifyDataSetChanged();
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

        holder.delButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);

                new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Album")
                        .setMessage("Are you sure you want to delete " + album.getName() + "?" )
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("size bfore " + albumSet.size());
                                    albumSet.remove(album);
                                    notifyDataSetChanged();
                                System.out.println("size after " + albumSet.size());
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
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(1000);
                v.startAnimation(animation1);
                saveFile();

                /*// TODO Auto-generated method stub
                Toast.makeText(mContext, "Open button Clicked",
                        Toast.LENGTH_LONG).show();*/

                Intent mIntent = new Intent(mContext, photomain.class);
                for(int a = 0; a < albumSet.size(); a++){
                    if(albumSet.get(a).getName().equals(album.getName())){
                        mIntent.putExtra("albumIndex", a);
                        mContext.startActivity(mIntent);
                        ((Activity)mContext).finish();
                    }
                }
            }
        });
        return row;
    }

    public void saveFile(){
        try {
            FileOutputStream fos = mContext.openFileOutput("File.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            System.out.println(mContext.getFilesDir()+ " adapter photo ");
            os.writeObject(albumSet);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
