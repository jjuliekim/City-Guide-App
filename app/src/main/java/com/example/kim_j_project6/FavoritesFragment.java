package com.example.kim_j_project6;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment {
    private DatabaseReference placesDatabase;
    private PlaceAdapter placeAdapter;
    private ArrayList<Place> placeList;

    public FavoritesFragment() {
    }


    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        placesDatabase = FirebaseDatabase.getInstance().getReference("places");
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

    }
}