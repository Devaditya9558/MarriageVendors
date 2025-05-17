package com.example.marriagevendors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import com.example.marriagevendors.viewmodel.LandingViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> listGroupTitles;
    private HashMap<String, List<String>> listChildData;
    private LandingViewModel viewModel;

    // User info
    private String userName = "Devaditya Sharma";
    private String userRole = "super_admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Drawer & Toggle
        drawer = findViewById(R.id.drawer_layout);
        expandableListView = findViewById(R.id.expandableListView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Header data
        View headerView = expandableListView.getRootView().findViewById(R.id.user_name);
        TextView userNameTextView = drawer.findViewById(R.id.user_name);
        TextView userRoleTextView = drawer.findViewById(R.id.user_role);
        if (userNameTextView != null) {
            userNameTextView.setText(userName);
            // Make the user name clickable to navigate to UserProfileActivity
            userNameTextView.setOnClickListener(v -> {
                Intent intent = new Intent(LandingActivity.this, UserProfileActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            });
        }
        if (userRoleTextView != null) {
            userRoleTextView.setText(userRole);
        }

        // ViewModel
        viewModel = new ViewModelProvider(this).get(LandingViewModel.class);

        // Prepare data
        prepareMenuData();
        expandableListAdapter = new ExpandableListAdapter(this, listGroupTitles, listChildData);
        expandableListView.setAdapter(expandableListAdapter);

        // Handle group (top-level) clicks
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            String group = listGroupTitles.get(groupPosition);
            // Only navigate if the group has no children (i.e., it's a top-level item like "Blog")
            if (!listChildData.containsKey(group)) {
                Intent intent = getNavigationIntent(group, null);
                if (intent != null) {
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return true; // Consume the click event for top-level items
            }
            return false; // Allow ExpandableListView to handle expandable groups
        });

        // Handle child clicks (for expandable groups like "Venue")
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String group = listGroupTitles.get(groupPosition);
            String child = listChildData.get(group).get(childPosition);
            Intent intent = getNavigationIntent(group, child);
            if (intent != null) startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void prepareMenuData() {
        listGroupTitles = new ArrayList<>();
        listChildData = new HashMap<>();

        listGroupTitles.add("Home");
        listGroupTitles.add("Invitation");
        listGroupTitles.add("Blog");
        listGroupTitles.add("User Login");
        if (userRole.equals("super_admin")) {
            listGroupTitles.add("Admin");
        }

        listGroupTitles.add("Venue");
        listChildData.put("Venue", List.of("Wedding Lawns", "Farmhouse", "Hotel", "Banquet Halls"));

        listGroupTitles.add("Vendor");
        listChildData.put("Vendor", List.of("Caterers", "Wedding Decor", "Wedding Photographers", "Wedding DJ"));

        listGroupTitles.add("Brides");
        listChildData.put("Brides", List.of("Bridal Lehenga", "Bridal Jewellery", "Bridal Makeup Artist"));

        listGroupTitles.add("Grooms");
        listChildData.put("Grooms", List.of("Sherwani", "Mens Grooming", "Mens Accessories"));
    }

    private Intent getNavigationIntent(String group, String child) {
        switch (group) {
            case "Home":
                return null; // already on home
            case "Invitation":
                return new Intent(this, InvitationActivity.class);
            case "Blog":
                return new Intent(this, BlogActivity.class);
            case "User Login":
                return new Intent(this, UserLogin.class);
            case "Admin":
                return new Intent(this, AdminActivity.class);
            case "Venue":
                switch (child) {
                    case "Wedding Lawns": return new Intent(this, WeddingLawnsActivity.class);
                    case "Farmhouse": return new Intent(this, FarmhouseActivity.class);
                    case "Hotel": return new Intent(this, HotelActivity.class);
                    case "Banquet Halls": return new Intent(this, BanquetHallsActivity.class);
                }
                break;
            case "Vendor":
                switch (child) {
                    case "Caterers": return new Intent(this, CaterersActivity.class);
                    case "Wedding Decor": return new Intent(this, WeddingDecorActivity.class);
                    case "Wedding Photographers": return new Intent(this, WeddingPhotographersActivity.class);
                    case "Wedding DJ": return new Intent(this, WeddingDJActivity.class);
                }
                break;
            case "Brides":
                switch (child) {
                    case "Bridal Lehenga": return new Intent(this, BridalLehengaActivity.class);
                    case "Bridal Jewellery": return new Intent(this, BridalJewelleryActivity.class);
                    case "Bridal Makeup Artist": return new Intent(this, BridalMakeupArtistActivity.class);
                }
                break;
            case "Grooms":
                switch (child) {
                    case "Sherwani": return new Intent(this, SherwaniActivity.class);
                    case "Mens Grooming": return new Intent(this, MensGroomingActivity.class);
                    case "Mens Accessories": return new Intent(this, MensAccessoriesActivity.class);
                }
                break;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}