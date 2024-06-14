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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final List<Place> placeList;
    private final Context context;

    public HistoryAdapter(Context context, ArrayList<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.placeNameTextView.setText(place.getName());
        holder.descriptionTextView.setText(place.getDescription());
        holder.dateVisitedTextView.setText(place.isVisited());

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


    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameTextView;
        TextView descriptionTextView;
        TextView dateVisitedTextView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = itemView.findViewById(R.id.history_name_text);
            descriptionTextView = itemView.findViewById(R.id.history_description_text);
            dateVisitedTextView = itemView.findViewById(R.id.history_date_text);
        }
    }

}
