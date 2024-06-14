package com.example.kim_j_project6;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {
    private DatabaseReference placesDatabase;
    private PlaceAdapter placeAdapter;
    private ArrayList<Place> placeList;

    public ExploreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        placesDatabase = FirebaseDatabase.getInstance().getReference("places");
        // set recycler view
        placeAdapter = new PlaceAdapter(getContext(), new ArrayList<>());
        RecyclerView recyclerView = view.findViewById(R.id.exploreRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(placeAdapter);

        fetchAllPlaces();
        return view;
    }

    // fetch all places from database (sorted by rating)
    private void fetchAllPlaces() {
        placesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                placeList = new ArrayList<>();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Place place = snapshot.getValue(Place.class);
                        placeList.add(place);
                    }
                    // sort by rating (most popular on top)
                    placeList.sort((p1, p2) -> Double.compare(getAverageRating(p2), getAverageRating(p1)));
                    placeAdapter.updatePlaces(placeList);
                    Log.i("HERE EXPLORE", "places loaded and sorted");
                } catch (Exception e) {
                    Log.i("HERE EXPLORE", "fetching e: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("HERE EXPLORE", "failed to load places");
                Toast.makeText(getContext(), "failed to load places", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double getAverageRating(Place place) {
        ArrayList<Rating> ratings = place.getRating();
        if (ratings != null && !ratings.isEmpty()) {
            double averageRating = 0;
            for (Rating rating : ratings) {
                averageRating += rating.getRating();
            }
            averageRating /= ratings.size();
            return averageRating;
        } else {
            return 0.0;
        }
    }
}