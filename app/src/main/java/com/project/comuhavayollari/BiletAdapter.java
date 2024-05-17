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

public class BiletAdapter extends RecyclerView.Adapter<BiletAdapter.BiletViewHolder> {

    private Context context;
    private List<Bilet> biletList;

    public BiletAdapter(Context context, List<Bilet> biletList) {
        this.context = context;
        this.biletList = biletList;
    }

    @NonNull
    @Override
    public BiletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bilet_listele_item, parent, false);
        return new BiletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BiletViewHolder holder, int position) {
        Bilet bilet = biletList.get(position);

        holder.ucakLogoImageView.setImageResource(R.drawable.plane_icon); // Uçak logosu
        holder.biletNoTextView.setText("Bilet No: " + bilet.getBiletNo());
        holder.ucusNoTextView.setText("Uçuş No: " + bilet.getUcusNo());
        holder.kalkisNoktasiTextView.setText("Kalkış Noktası: " + bilet.getKalkisNoktasi());
        holder.varisNoktasiTextView.setText("Varış Noktası: "   + bilet.getVarisNoktasi());
        holder.ucusTarihiTextView.setText("Uçuş Tarihi: " + bilet.getUcusTarihi());
        holder.ucusSaatiTextView.setText("Uçuş Saati:" + bilet.getUcusSaati());
    }

    @Override
    public int getItemCount() {
        return biletList.size();
    }

    public static class BiletViewHolder extends RecyclerView.ViewHolder {
        ImageView ucakLogoImageView;
        TextView biletNoTextView;
        TextView ucusNoTextView;
        TextView kalkisNoktasiTextView;
        TextView varisNoktasiTextView;
        TextView ucusTarihiTextView;
        TextView ucusSaatiTextView;

        public BiletViewHolder(@NonNull View itemView) {
            super(itemView);
            ucakLogoImageView = itemView.findViewById(R.id.ucakLogoImageView);
            biletNoTextView = itemView.findViewById(R.id.biletNoTextView);
            ucusNoTextView = itemView.findViewById(R.id.ucusNoTextView);
            kalkisNoktasiTextView = itemView.findViewById(R.id.kalkisNoktasiTextView);
            varisNoktasiTextView= itemView.findViewById(R.id.varisNoktasiTextView);
            ucusTarihiTextView = itemView.findViewById(R.id.ucusTarihiTextView);
            ucusSaatiTextView=itemView.findViewById(R.id.ucusSaatiTextView);
        }
    }
}
