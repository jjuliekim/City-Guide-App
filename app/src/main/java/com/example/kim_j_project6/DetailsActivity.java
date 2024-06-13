package com.example.kim_j_project6;

import android.os.Bundle;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    private Place place;
    private DatabaseReference placesDatabase;
    TextView placeNameText;
    TextView descriptionText;
    TextView addressText;
    TextView ratingsText;

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
        placeNameText = findViewById(R.id.details_name);
        descriptionText = findViewById(R.id.details_description);
        addressText = findViewById(R.id.details_address);
        ratingsText = findViewById(R.id.details_rating);

        placeNameText.setText(place.getName());
        descriptionText.setText(place.getDescription());
        addressText.setText(String.format("Location: %s°, %s°", place.getLat(), place.getLng()));
        ArrayList<Double> ratings = place.getRating();
        if (ratings != null && !ratings.isEmpty()) {
            double averageRating = 0;
            for (double rating : ratings) {
                averageRating += rating;
            }
            averageRating /= ratings.size();
            ratingsText.setText(String.format("Average Rating: %.2f", averageRating));
        } else {
            ratingsText.setText("Average Rating: None");
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
        }*/
        addRatingButton.setOnClickListener(v -> addRatingDialog());
    }

    // display dialog to add rating
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
                dialog.dismiss();
            } else {
                double rating = Double.parseDouble(ratingInput);
                addRating(rating);
                Toast.makeText(this, "Rating Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    // update database
    private void addRating(double rating) {
        ArrayList<Double> currRatings = place.getRating();
        currRatings.add(rating);
        place.setRating(currRatings);
        placesDatabase.child(place.getId()).setValue(place).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Rating added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add rating", Toast.LENGTH_SHORT).show();
            }
        });
    }

}