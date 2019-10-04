package com.addresses.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.addresses.R;
import com.addresses.app.addressform.AddressFormActivity;
import com.addresses.datamodels.Address;
import com.addresses.sqlite.AddressesDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.addresses.sqlite.AddressesDbContract.FeedEntry.COLUMN_NAME_CITY;
import static com.addresses.sqlite.AddressesDbContract.FeedEntry.COLUMN_NAME_LOCATION_NAME;
import static com.addresses.sqlite.AddressesDbContract.FeedEntry.COLUMN_NAME_STATE;
import static com.addresses.sqlite.AddressesDbContract.FeedEntry.COLUMN_NAME_STREET_ADDRESS;
import static com.addresses.sqlite.AddressesDbContract.FeedEntry.COLUMN_NAME_ZIP;

public class MainActivity extends AppCompatActivity {

    private List addressList;

    private AddressRecyclerViewAdapter addressRecyclerViewAdapter;

    /* Widgets */
    private Toolbar toolbar;
    private RecyclerView addressListRecyclerView;

    private FloatingActionButton addFloatingButton;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Create a singleton instance for AddressesDbManager
        AddressesDbManager.newInstance(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Weekly weather section
        addressListRecyclerView = findViewById(R.id.addressListRecyclerView);
        addressListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyTextView = findViewById(R.id.emptyTextView);

        addFloatingButton = findViewById(R.id.addFloatingButton);
        addFloatingButton.setOnClickListener(new AddFloatingButtonOnClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        addressList = AddressesDbManager.getAllData();
        populateAddresses(addressList);

        if (addressList.size() == 0) {
            addressListRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);

        } else {
            addressListRecyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    private void populateAddresses(List addressList) {

        // Specify an adapter
        addressRecyclerViewAdapter = new AddressRecyclerViewAdapter(addressList, new AddressListRecyclerViewOnItemClickListener());
        addressListRecyclerView.setAdapter(addressRecyclerViewAdapter);
    }

    public class AddressListRecyclerViewOnItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int itemPosition = addressListRecyclerView.indexOfChild(view);
            Log.d("MainActivity", "" + itemPosition);

            Address address = (Address) addressList.get(itemPosition);

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AddressFormActivity.class);
            intent.putExtra(AddressFormActivity.OPERATION, AddressFormActivity.EDITING);
            intent.putExtra(_ID, address.getId());
            intent.putExtra(COLUMN_NAME_LOCATION_NAME, address.getLocationNameString());
            intent.putExtra(COLUMN_NAME_STREET_ADDRESS, address.getStreetAddressString());
            intent.putExtra(COLUMN_NAME_CITY, address.getCityString());
            intent.putExtra(COLUMN_NAME_STATE, address.getStateString());
            intent.putExtra(COLUMN_NAME_ZIP, address.getZipString());

            startActivity(intent);
        }
    }

    private class AddFloatingButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AddressFormActivity.class);
            intent.putExtra(AddressFormActivity.OPERATION, AddressFormActivity.CREATING);

            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {

        // Destroy AddressesDbManager
        AddressesDbManager.destroy();

        super.onDestroy();
    }
}
