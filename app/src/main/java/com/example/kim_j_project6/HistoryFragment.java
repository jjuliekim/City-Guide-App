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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private DatabaseReference placesDatabase;
    private HistoryAdapter historyAdapter;
    private ArrayList<Place> placeList;
    private String userId;

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        placesDatabase = FirebaseDatabase.getInstance().getReference("places");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        historyAdapter = new HistoryAdapter(getContext(), new ArrayList<>());
        RecyclerView recyclerView = view.findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(historyAdapter);

        fetchHistory();
        return view;
    }

    // fetch history of places visited by the user
    private void fetchHistory() {
        placesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                placeList = new ArrayList<>();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Place place = snapshot.getValue(Place.class);
                        if (place.getVisited().containsKey(userId)) {
                            placeList.add(place);
                        }
                    }
                    // sort by recent visit date
                    placeList.sort((p1, p2) -> {
                        String date1 = p1.getVisited().get(userId);
                        String date2 = p2.getVisited().get(userId);
                        if (date1 == null) return 1;
                        if (date2 == null) return -1;
                        return date2.compareTo(date1);
                    });
                    historyAdapter.updatePlaces(placeList);
                } catch (Exception e) {
                    Log.i("HERE HISTORY", "fetching e: " + e.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("HERE HISTORY", "failed to load places");
                Toast.makeText(getContext(), "failed to load places", Toast.LENGTH_SHORT).show();
            }
        });
    }
}