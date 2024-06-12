package com.example.kim_j_project6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> placeList;

    public PlaceAdapter(List<Place> placeList) {
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
        holder.addressTextView.setText(String.format("%s, %s", place.getLat(), place.getLng()));
        holder.ratingTextView.setText(place.getRating());
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

    // edit place details
    public void editPlace(int position, int rating, boolean visited, boolean favorited) {
        Place place = placeList.get(position);
        place.setRating(rating);
        place.setVisited(visited);
        place.setFavorited(favorited);
        notifyItemChanged(position);
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameTextView;
        TextView addressTextView;
        TextView ratingTextView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = itemView.findViewById(R.id.place_name_text);
            addressTextView = itemView.findViewById(R.id.address_text);
            ratingTextView = itemView.findViewById(R.id.rating_text);
        }
    }
}
