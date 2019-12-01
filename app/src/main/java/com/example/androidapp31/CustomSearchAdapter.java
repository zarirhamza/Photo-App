package com.example.androidapp31;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class CustomSearchAdapter extends ArrayAdapter<Photo> {
    private ArrayList<Photo> photoSet;
    Context mContext;

    private static class ViewHolder {
        ImageView thumbnail;
        TextView searchText;
    }

    public CustomSearchAdapter(ArrayList<Photo> photoList, Context context) {
        super(context, R.layout.activity_photo_list_view, photoList);
        this.photoSet = photoList;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.activity_search_list_view, parent, false);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) row.findViewById(R.id.searchThumbNail);
            holder.searchText = (TextView) row.findViewById(R.id.searchText);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Photo photo = photoSet.get(position);
        System.out.println(photo.getLocation());
        File imgFile = new File(photo.getLocation());
        if (imgFile.exists()) {
            Bitmap bmImg = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap bMapScaled = Bitmap.createScaledBitmap(bmImg, 200, 200, true);
            holder.thumbnail.setImageBitmap(bMapScaled);
        } else {
            Drawable myDrawable = mContext.getApplicationContext().getDrawable(R.drawable.one);
        }
        holder.searchText.setText(imgFile.getName());

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, displayPhoto.class);
                    intent.putExtra("state", -1);
                    intent.putExtra("location", photo.getLocation());
                    intent.putStringArrayListExtra("locationTags", photo.getLocationTags());
                    intent.putStringArrayListExtra("peopleTags", photo.getPersonTags());
                    ((Activity) mContext).startActivity(intent);
                }
            });


        return row;
    }
}
