package com.project.comuhavayollari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FlightAdapter extends ArrayAdapter<Flight> {
    public FlightAdapter(Context context, ArrayList<Flight> flights) {
        super(context, 0, flights);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Flight flight = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flight_item, parent, false);
        }
        TextView fromCity = convertView.findViewById(R.id.fromCity);
        TextView toCity = convertView.findViewById(R.id.toCity);
        TextView flightNumber = convertView.findViewById(R.id.flightNumber);
        TextView flightDate = convertView.findViewById(R.id.flightDate);
        TextView flightTime = convertView.findViewById(R.id.flightTime);
        TextView ticketPrice = convertView.findViewById(R.id.ticketPrice);

        fromCity.setText(flight.getFromCity());
        toCity.setText(flight.getToCity());
        flightNumber.setText(flight.getFlightNumber());
        flightDate.setText(flight.getFlightDate());
        flightTime.setText(flight.getFlightTime());
        ticketPrice.setText(flight.getTicketPrice());

        return convertView;
    }
}
