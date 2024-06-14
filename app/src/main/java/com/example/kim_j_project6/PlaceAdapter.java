package com.example.kim_j_project6;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private final List<Place> placeList;
    private final Context context;

    public PlaceAdapter(Context context, ArrayList<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.placeNameTextView.setText(place.getName());
        holder.addressTextView.setText(String.format("%s°, %s°", place.getLat(), place.getLng()));
        holder.descriptionTextView.setText(place.getDescription());
        ArrayList<Double> ratings = place.getRating();
        if (ratings != null && !ratings.isEmpty()) {
            double averageRating = 0;
            for (double rating : ratings) {
                averageRating += rating;
            }
            averageRating /= ratings.size();
            holder.ratingTextView.setText(String.format("Average Rating: %.2f", averageRating));
        } else {
            holder.ratingTextView.setText("Average Rating: None");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent nextIntent = new Intent(context, DetailsActivity.class);
            nextIntent.putExtra("place", place);
            context.startActivity(nextIntent);
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    // update place list
    public void updatePlaces(List<Place> updatedList) {
        placeList.clear();
        placeList.addAll(updatedList);
        notifyDataSetChanged();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameTextView;
        TextView addressTextView;
        TextView descriptionTextView;
        TextView ratingTextView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = itemView.findViewById(R.id.place_name_text);
            addressTextView = itemView.findViewById(R.id.address_text);
            descriptionTextView = itemView.findViewById(R.id.description_text);
            ratingTextView = itemView.findViewById(R.id.rating_text);
        }
    }
}
