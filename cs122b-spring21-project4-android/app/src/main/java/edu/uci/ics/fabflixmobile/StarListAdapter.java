package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class StarListAdapter extends ArrayAdapter<String> {
    private final ArrayList<String> stars;

    public StarListAdapter(ArrayList<String> stars, SingleMoviePage context) {
        super(context, R.layout.starrow, stars);
        this.stars = stars;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.starrow, parent, false);

        String star = stars.get(position);

        TextView starname = view.findViewById(R.id.starname);

        starname.setText(star);
        return view;
    }
}