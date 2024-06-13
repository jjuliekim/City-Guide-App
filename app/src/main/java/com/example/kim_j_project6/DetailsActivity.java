package com.example.kim_j_project6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    private Place place;
    private DatabaseReference placesDatabase;

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
        place = getIntent().getParcelableExtra("place");

        TextView placeNameText = findViewById(R.id.details_name);
        TextView descriptionText = findViewById(R.id.details_description);
        TextView addressText = findViewById(R.id.details_address);
        TextView ratingsText = findViewById(R.id.details_rating);
        placeNameText.setText(place.getName());
        descriptionText.setText(place.getDescription());
        addressText.setText(String.format("%s°, %s°", place.getLat(), place.getLng()));
        ArrayList<Double> ratings = place.getRating();
        if (ratings != null && !ratings.isEmpty()) {
            double averageRating = 0;
            for (double rating : ratings) {
                averageRating += rating;
            }
            averageRating /= ratings.size();
            ratingsText.setText(String.format("Average Ratings: %s", averageRating));
        } else {
            ratingsText.setText("Average Ratings: None");
        }

        Button addRatingButton = findViewById(R.id.addRatingButton);
        Button addFavoritesButton = findViewById(R.id.addToFavoritesButton);
        Button markVisitedButton = findViewById(R.id.markVisitedButton);
        /*if (place.isFavorited()) {
            addFavoritesButton.setText("Remove from Favorites");
        }
        addFavoritesButton.setOnClickListener(v -> favoritePlace());
        if (place.isVisited()) {
            markVisitedButton.setText("Visited "); // + place.getDateVisited();
        } else {
            markVisitedButton.setOnClickListener(v -> markAsVisited());
        }
        addRatingButton.setOnClickListener(v -> addRatingDialog());*/
    }
}