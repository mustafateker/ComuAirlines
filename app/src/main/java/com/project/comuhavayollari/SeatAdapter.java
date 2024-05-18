package com.project.comuhavayollari;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<Seat> seatList;
    private int selectedPosition = -1;

    public SeatAdapter(List<Seat> seatList) {
        this.seatList = seatList;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.bind(seat, position);
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    class SeatViewHolder extends RecyclerView.ViewHolder {
        ImageView seatImage;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatImage = itemView.findViewById(R.id.seat_image);
        }

        void bind(Seat seat, int position) {
            switch (seat.getStatus()) {
                case AVAILABLE:
                    seatImage.setImageResource(R.drawable.bos_koltuk);
                    seatImage.setClickable(true);
                    break;
                case OCCUPIED:
                    seatImage.setImageResource(R.drawable.dolu_koltuk);
                    seatImage.setClickable(false);
                    break;
                case RESERVED:
                    seatImage.setImageResource(R.drawable.rezerve_koltuk);
                    seatImage.setClickable(false);
                    break;
                case SELECTED:
                    seatImage.setImageResource(R.drawable.secili_koltuk);
                    seatImage.setClickable(true);
                    break;
                case VIP:
                    seatImage.setImageResource(R.drawable.vip_koltuk);
                    seatImage.setClickable(false);
                    break;
            }

            seatImage.setOnClickListener(v -> {
                if (seat.getStatus() == SeatStatus.AVAILABLE) {
                    if (selectedPosition != -1) {
                        seatList.get(selectedPosition).setStatus(SeatStatus.AVAILABLE);
                        notifyItemChanged(selectedPosition);
                    }
                    seat.setStatus(SeatStatus.SELECTED);
                    selectedPosition = position;
                    notifyItemChanged(position);
                }
            });
        }
    }
}

