package com.project.comuhavayollari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class biletDetayiAdapter extends RecyclerView.Adapter<biletDetayiAdapter.BiletDetayViewHolder> {

    private Context context;
    private List<Bilet> biletList;

    public biletDetayiAdapter(Context context, List<Bilet> biletList) {
        this.context = context;
        this.biletList = biletList;
    }

    @NonNull
    @Override
    public BiletDetayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bilet_detaylari_item, parent, false);
        return new BiletDetayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BiletDetayViewHolder holder, int position) {
        Bilet bilet = biletList.get(position);

        holder.ucakLogoImageView.setImageResource(R.drawable.plane_icon_degrade);
        holder.biletNoTextView.setText("Bilet No: " + bilet.getBiletNo());
        holder.ucusNoTextView.setText("Uçuş No: " + bilet.getUcusNo());
        holder.kalkisNoktasiTextView.setText("Kalkış Noktası: " + bilet.getKalkisNoktasi());
        holder.varisNoktasiTextView.setText("Varış Noktası: " + bilet.getVarisNoktasi());
        holder.ucusTarihiTextView.setText("Uçuş Tarihi: " + bilet.getUcusTarihi());
        holder.ucusSaatiTextView.setText("Uçuş Saati: " + bilet.getUcus_saati());
        holder.koltukNoTextView.setText("Koltuk No: " + bilet.getKoltuk_no());
        holder.biletFiyatiTextView.setText("Bilet Fiyatı: " + bilet.getBilet_fiyati());
    }

    @Override
    public int getItemCount() {
        return biletList.size();
    }

    public static class BiletDetayViewHolder extends RecyclerView.ViewHolder {
        ImageView ucakLogoImageView;
        TextView biletNoTextView;
        TextView ucusNoTextView;
        TextView kalkisNoktasiTextView;
        TextView varisNoktasiTextView;
        TextView ucusTarihiTextView;
        TextView ucusSaatiTextView;
        TextView koltukNoTextView;
        TextView biletFiyatiTextView;

        public BiletDetayViewHolder(@NonNull View itemView) {
            super(itemView);
            ucakLogoImageView = itemView.findViewById(R.id.ucakLogoImageView);
            biletNoTextView = itemView.findViewById(R.id.biletNodetayTextView);
            ucusNoTextView = itemView.findViewById(R.id.ucusNodetayTextView);
            kalkisNoktasiTextView = itemView.findViewById(R.id.kalkisNoktasidetayTextView);
            varisNoktasiTextView = itemView.findViewById(R.id.varisNoktasidetayTextView);
            ucusTarihiTextView = itemView.findViewById(R.id.ucusTarihidetayTextView);
            ucusSaatiTextView = itemView.findViewById(R.id.ucusSaatidetayTextView);
            koltukNoTextView = itemView.findViewById(R.id.ucakKoltukNodetayTextview);
            biletFiyatiTextView = itemView.findViewById(R.id.BiletFiyatidetayTextview);
        }
    }
}
