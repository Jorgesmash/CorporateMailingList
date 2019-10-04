package com.addresses.app.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.addresses.R;
import com.addresses.datamodels.Address;

import java.util.List;

public class AddressRecyclerViewAdapter extends RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder> {

    private List addressList;

    private MainActivity.AddressListRecyclerViewOnItemClickListener addressListRecyclerViewOnItemClickListener;

    AddressRecyclerViewAdapter(List addressList, MainActivity.AddressListRecyclerViewOnItemClickListener addressListRecyclerViewOnItemClickListener) {
        this.addressList = addressList;
        this.addressListRecyclerViewOnItemClickListener = addressListRecyclerViewOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item_cardview, parent, false);
        itemView.setOnClickListener(addressListRecyclerViewOnItemClickListener);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Address address = (Address) addressList.get(position);

        // Populate user interface
        viewHolder.locationNameTextView.setText(address.getLocationNameString());
        viewHolder.streetAddressTextView.setText(address.getStreetAddressString());
        viewHolder.cityTextView.setText(address.getCityString() + ",");
        viewHolder.stateTextView.setText(address.getStateString());
        viewHolder.zipTextView.setText(address.getZipString());
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    /** ViewHolder pattern: Inner class needed to keep the references between widgets and data to improve the performance */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView locationNameTextView;
        TextView streetAddressTextView;
        TextView cityTextView;
        TextView stateTextView;
        TextView zipTextView;

        ViewHolder(View itemView) {
            super(itemView);

            locationNameTextView = itemView.findViewById(R.id.locationNameTextView);
            streetAddressTextView = itemView.findViewById(R.id.streetAddressTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            zipTextView = itemView.findViewById(R.id.zipTextView);
        }
    }
}
