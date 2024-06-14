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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment {
    private DatabaseReference placesDatabase;
    private PlaceAdapter placeAdapter;
    private ArrayList<Place> placeList;
    private String userId;

    public FavoritesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        placesDatabase = FirebaseDatabase.getInstance().getReference("places");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        // set recycler view
        placeAdapter = new PlaceAdapter(getContext(), new ArrayList<>());
        RecyclerView recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(placeAdapter);

        fetchFavoritePlaces();
        return view;
    }

    // fetch places that have been favorited by the user
    private void fetchFavoritePlaces() {
        placesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                placeList = new ArrayList<>();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Place place = snapshot.getValue(Place.class);
                        if (place.getFavorited().contains(userId)) {
                            placeList.add(place);
                        }
                    }
                    placeAdapter.updatePlaces(placeList);
                } catch (Exception e) {
                    Log.i("HERE FAVORITES", "fetching e: " + e.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}