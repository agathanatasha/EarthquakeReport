package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter {

    public static final String datePattern = "MMM DD, yyyy";
    public static final String timePattern = "h:mm a";
    public static final String SEPARATOR = "of";
    public static final String magnitudePattern = "0.0";
    private String displayDirection;
    private String displayCity;

    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview = convertView;
        if (listview == null) {
            listview =
                LayoutInflater.from(getContext()).inflate(R.layout.earthquake_item, parent, false);
        }
        Earthquake currentItem = (Earthquake) getItem(position);
        TextView magnitude = (TextView) listview.findViewById(R.id.earthquake_magnitude);
        double currentMagnitude = currentItem.getMagnitude();
        magnitude.setText(formatMagnitude(currentMagnitude));

        GradientDrawable magnitudeCircle =
            (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(currentMagnitude);
        magnitudeCircle.setColor(magnitudeColor);

        TextView city = (TextView) listview.findViewById(R.id.earthquake_city);
        TextView direction = (TextView) listview.findViewById(R.id.earthquake_direction);
        String locationString = currentItem.getLocation();
        if (locationString.contains("of")) {
            String[] splitString = locationString.split(SEPARATOR);
            displayDirection = splitString[0] + SEPARATOR;
            displayCity = splitString[1].trim();
        } else {
            displayCity = locationString;
            displayDirection = getContext().getString(R.string.near_the);
        }
        city.setText(displayCity);
        direction.setText(displayDirection.toUpperCase());

        TextView date = (TextView) listview.findViewById(R.id.earthquake_date);
        TextView time = (TextView) listview.findViewById(R.id.earthquake_time);
        long timeInMS = currentItem.getTime();
        Date dateObject = new Date(timeInMS);
        date.setText(formatDate(dateObject));
        time.setText(formatTime(dateObject));

        return listview;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColor;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColor =  R.color.magnitude1;
                break;
            case 2:
                magnitudeColor =  R.color.magnitude2;
                break;
            case 3:
                magnitudeColor = R.color.magnitude3;
                break;
            case 4:
                magnitudeColor = R.color.magnitude4;
                break;
            case 5:
                magnitudeColor = R.color.magnitude5;
                break;
            case 6:
                magnitudeColor = R.color.magnitude6;
                break;
            case 7:
                magnitudeColor = R.color.magnitude7;
                break;
            case 8:
                magnitudeColor = R.color.magnitude8;
                break;
            case 9:
                magnitudeColor = R.color.magnitude9;
            default:
                magnitudeColor = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), magnitudeColor);
    }

    private String formatDate(Date timeInMS) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        return dateFormat.format(timeInMS);
    }

    private String formatTime(Date timeInMS) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);
        return timeFormat.format(timeInMS);
    }

    private String formatMagnitude (Double mag) {
        DecimalFormat formatter = new DecimalFormat(magnitudePattern);
        return formatter.format(mag);
    }
}
