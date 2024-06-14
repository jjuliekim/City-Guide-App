package com.example.kim_j_project6;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private Place place;
    private DatabaseReference placesDatabase;
    private TextView placeNameText;
    private TextView descriptionText;
    private TextView addressText;
    private TextView ratingsText;
    private String userId;
    private Button favoriteButton;
    private Button markVisitedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        placesDatabase = FirebaseDatabase.getInstance().getReference("places");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        place = getIntent().getParcelableExtra("place");
        placeNameText = findViewById(R.id.details_name);
        descriptionText = findViewById(R.id.details_description);
        addressText = findViewById(R.id.details_address);
        ratingsText = findViewById(R.id.details_rating);

        loadPlaceData();

        // button actions
        favoriteButton = findViewById(R.id.addToFavoritesButton);
        if (place.getFavorited().contains(userId)) {
            favoriteButton.setText("Remove from Favorites");
            favoriteButton.setOnClickListener(v -> removeFavorite());
        } else {
            favoriteButton.setText("Add to Favorites");
            favoriteButton.setOnClickListener(v -> addFavorite());
        }
        Button addRatingButton = findViewById(R.id.addRatingButton);
        addRatingButton.setOnClickListener(v -> addRatingDialog());
        markVisitedButton = findViewById(R.id.markVisitedButton);
        if (place.getVisited().containsKey(userId)) {
            markVisitedButton.setText(R.string.already_visited);
        } else {
            markVisitedButton.setText("Mark as Visited");
            markVisitedButton.setOnClickListener(v -> markVisited());
        }
    }

    // reload data on this page
    private void loadPlaceData() {
        placesDatabase.child(place.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                place = dataSnapshot.getValue(Place.class);
                if (place != null) {
                    updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailsActivity.this, "Failed to load place details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        placeNameText.setText(place.getName());
        descriptionText.setText(place.getDescription());
        addressText.setText(String.format("Location: %s°, %s°", place.getLat(), place.getLng()));
        ArrayList<Double> ratings = place.getRating();
        if (ratings == null || ratings.isEmpty()) {
            ratingsText.setText("Average Rating: None");
            return;
        }
        double averageRating = 0;
        for (double rating : ratings) {
            averageRating += rating;
        }
        averageRating /= ratings.size();
        ratingsText.setText(String.format("Average Rating: %.2f", averageRating));
        if (place.getFavorited().contains(userId)) {
            favoriteButton.setText("Remove from Favorites");
            favoriteButton.setOnClickListener(v -> removeFavorite());
        } else {
            favoriteButton.setText("Add to Favorites");
            favoriteButton.setOnClickListener(v -> addFavorite());
        }
        if (place.getVisited().containsKey(userId)) {
            markVisitedButton.setText(R.string.already_visited);
        } else {
            markVisitedButton.setText(R.string.already_visited);
        }
        Log.i("HERE DETAILS", "updated UI");
    }

    private void addRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_rating, null);
        builder.setView(dialogView);
        EditText ratingText = dialogView.findViewById(R.id.rating_input);
        Button submitButton = dialogView.findViewById(R.id.submit_rating_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_rating_button);

        AlertDialog dialog = builder.create();
        submitButton.setOnClickListener(v -> {
            String ratingInput = ratingText.getText().toString();
            if (ratingInput.isEmpty() || Double.parseDouble(ratingInput) < 0 || Double.parseDouble(ratingInput) > 10) {
                Toast.makeText(this, "Invalid Rating", Toast.LENGTH_SHORT).show();
                return;
            }
            double rating = Double.parseDouble(ratingInput);
            addRating(rating);
            dialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    private void addRating(double rating) {
        ArrayList<Double> currRatings = place.getRating();
        if (currRatings == null) {
            currRatings = new ArrayList<>();
        }
        currRatings.add(rating);
        place.setRating(currRatings);
        placesDatabase.child(place.getId()).setValue(place).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Rating added successfully", Toast.LENGTH_SHORT).show();
                loadPlaceData();
            } else {
                Toast.makeText(this, "Failed to add rating", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFavorite() {
        ArrayList<String> favoritedBy = place.getFavorited();
        if (favoritedBy == null) {
            favoritedBy = new ArrayList<>();
        }
        favoritedBy.add(userId);
        place.setFavorited(favoritedBy);
        placesDatabase.child(place.getId()).setValue(place).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                loadPlaceData();
            } else {
                Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFavorite() {
        ArrayList<String> favoritedBy = place.getFavorited();
        if (favoritedBy != null) {
            favoritedBy.remove(userId);
            place.setFavorited(favoritedBy);
            placesDatabase.child(place.getId()).setValue(place).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    loadPlaceData();
                } else {
                    Toast.makeText(this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void markVisited() {
        HashMap<String, String> visitedBy = place.getVisited();
        if (visitedBy == null) {
            visitedBy = new HashMap<>();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        visitedBy.put(userId, currentDate);
        place.setVisited(visitedBy);
        placesDatabase.child(place.getId()).setValue(place).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                markVisitedButton.setText(R.string.already_visited);
                Toast.makeText(this, "Marked as visited", Toast.LENGTH_SHORT).show();
                loadPlaceData();
            } else {
                Toast.makeText(this, "Failed to mark as visited", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
