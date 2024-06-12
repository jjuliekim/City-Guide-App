package com.example.kim_j_project6;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FirebaseUser user;
    private PlaceAdapter placeAdapter;
    private EditText addPlaceText;
    private DatabaseReference placesDatabase;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
            Log.i("HERE HOME", "user: " + user);
        }
        addPlaceText = view.findViewById(R.id.addPlaceText);
        placesDatabase = FirebaseDatabase.getInstance().getReference("places");
        // set recycler view
        placeAdapter = new PlaceAdapter(new ArrayList<>());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(placeAdapter);
        // add place button action
        Button addPlaceButton = view.findViewById(R.id.addPlaceButton);
        addPlaceButton.setOnClickListener(v -> addPlaceDialog());

        fetchPlaces();
        return view;
    }

    // display dialog to add place
    private void addPlaceDialog() {
        String placeName = addPlaceText.getText().toString();
        if (placeName.isEmpty()) {
            Toast.makeText(getContext(), "Enter Place Name", Toast.LENGTH_SHORT).show();
            return;
        }
        // inflate dialog
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_place, null);
        EditText placeNameText = dialogView.findViewById(R.id.add_place_text);
        EditText descriptionText = dialogView.findViewById(R.id.add_description_text);
        EditText latText = dialogView.findViewById(R.id.lat_text);
        EditText lngText = dialogView.findViewById(R.id.lng_text);
        Button addButton = dialogView.findViewById(R.id.button_add);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
        placeNameText.setText(placeName);
        // create dialog
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(dialogView).create();
        // add button action
        addButton.setOnClickListener(v -> {
            String addPlaceName = placeNameText.getText().toString();
            String description = descriptionText.getText().toString();
            String lat = latText.getText().toString();
            String lng = lngText.getText().toString();
            if (addPlaceName.isEmpty() || description.isEmpty() || lat.isEmpty() || lng.isEmpty()) {
                Toast.makeText(getContext(), "Empty Entries", Toast.LENGTH_SHORT).show();
                return;
            }
            // save to database
            String placeId = placesDatabase.push().getKey();
            Place place = new Place(placeId, addPlaceName, description, lat, lng, 0, false, false);
            placesDatabase.child(placeId).setValue(place).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i("HERE HOME", "place saved");
                    Toast.makeText(getContext(), "Place Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to Add", Toast.LENGTH_SHORT).show();
                    Log.i("HERE HOME", "place failed to add");
                }
            });
            dialog.dismiss();
        });
        // cancel button action
        cancelButton.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    // fetch places from database
    private void fetchPlaces() {
        placesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Place> placeList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    placeList.add(place);
                }
                placeAdapter.updatePlaces(placeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("HERE HOME", "failed to load places");
                Toast.makeText(getContext(), "failed to load places", Toast.LENGTH_SHORT).show();
            }
        });
    }

}