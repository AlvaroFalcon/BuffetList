package com.list.buffet.alvaro.buffetlist;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.list.buffet.alvaro.buffetlist.model.RatingList;

import java.util.List;

/**
 * Created by Alvaro on 21/12/2016.
 */

public class RatingListAdapter extends ArrayAdapter<RatingList> {
    private AppCompatActivity activity;
    private List<RatingList> ratingList;
    public RatingListAdapter(AppCompatActivity context, int resource, List<RatingList> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.ratingList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getRating());
        holder.listName.setText(getItem(position).getListName());

        return convertView;
    }
    private static class ViewHolder {
        private RatingBar ratingBar;
        private TextView listName;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_item);
            listName = (TextView) view.findViewById(R.id.textview_item);
        }
    }

}
