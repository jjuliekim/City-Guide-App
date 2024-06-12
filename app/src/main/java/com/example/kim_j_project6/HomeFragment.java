package com.example.kim_j_project6;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {
    private FirebaseUser user;
    private PlaceAdapter placeAdapter;
    private EditText addPlaceText;

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
        // set recycler view
        placeAdapter = new PlaceAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(placeAdapter);
        // set add place button actions
        Button addPlaceButton = view.findViewById(R.id.addPlaceButton);

        return view;
    }
}