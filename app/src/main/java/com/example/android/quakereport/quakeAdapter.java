package com.example.android.quakereport;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class quakeAdapter extends ArrayAdapter<Quake> {

    private static final String LOG_TAG = quakeAdapter.class.getSimpleName();


    public quakeAdapter(Activity context, ArrayList<Quake> earthquakes) {
        super(context, 0, earthquakes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_quake_list, parent, false);
        }

        final Quake quake = getItem(position);

        TextView magTextView = (TextView) listItemView.findViewById(R.id.mag_id);
        magTextView.setText(quake.getMag());

        TextView placeTextView = (TextView) listItemView.findViewById(R.id.place_id);
        placeTextView.setText(quake.getPlace());

        TextView placeHTextView = (TextView) listItemView.findViewById(R.id.placeHading_id);
        placeHTextView.setText(quake.getPlaceH());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_id);
        dateTextView.setText(quake.getDate());

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_id);
        timeTextView.setText(quake.getTime());

        float magnitude = Float.valueOf(quake.getMag());

        if (magnitude>6.5)
        {
            TextView magView = (TextView) listItemView.findViewById(R.id.mag_id);
            magView.setBackgroundResource(R.drawable.circularviewred);
        }
        else
        {
            TextView magView = (TextView) listItemView.findViewById(R.id.mag_id);
            magView.setBackgroundResource(R.drawable.circularvieworange);
        }

        RelativeLayout itemid = (RelativeLayout) listItemView.findViewById(R.id.list_id);
        itemid.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(quake.getUrl()));
                getContext().startActivity(i);
            }
        });
        return listItemView;
    }

}
