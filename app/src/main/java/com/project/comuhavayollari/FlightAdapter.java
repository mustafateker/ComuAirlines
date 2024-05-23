package com.project.comuhavayollari;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<Flight> flightList;

    public FlightAdapter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_item, parent, false);
        return new FlightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.fromCityTextView.setText(flight.getFromCity());
        holder.toCityTextView.setText(flight.getToCity());
        holder.flightNumberTextView.setText(flight.getFlightNumber());
        holder.flightDateTextView.setText(flight.getFlightDate());
        holder.flightTimeTextView.setText(flight.getFlightTime());
        holder.ticketPriceTextView.setText(flight.getTicketPrice());
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        public TextView fromCityTextView;
        public TextView toCityTextView;
        public TextView flightNumberTextView;
        public TextView flightDateTextView;
        public TextView flightTimeTextView;
        public TextView ticketPriceTextView;

        public FlightViewHolder(View view) {
            super(view);
            fromCityTextView = view.findViewById(R.id.fromCity);
            toCityTextView = view.findViewById(R.id.toCity);
            flightNumberTextView = view.findViewById(R.id.flightNumber);
            flightDateTextView = view.findViewById(R.id.flightDate);
            flightTimeTextView = view.findViewById(R.id.flightTime);
            ticketPriceTextView = view.findViewById(R.id.ticketPrice);
        }
    }
}
