package com.project.comuhavayollari;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OdemeAdapter extends RecyclerView.Adapter<OdemeAdapter.OdemeViewHolder> {

    private Context  context;
    private List<OdemeList> OdemeList;

    public OdemeAdapter(Context context, List<OdemeList> OdemeList){
        this.context = context;
        this.OdemeList = OdemeList;
    }

    @Override
    @NonNull
    public OdemeViewHolder onCreateViewHolder (@NonNull ViewGroup parent , int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.odeme_item, parent ,false);
        return new OdemeViewHolder(view);
    }
    public void onBindViewHolder(@NonNull OdemeAdapter.OdemeViewHolder holder, int position) {
        OdemeList odemeList = OdemeList.get(position);
        
        holder.biletFiyati.setText(odemeList.getBiletFiyati());
        holder.uyelikIndirimi.setText(odemeList.getUyelikIndirimi());
        holder.gidisDonusIndirimi.setText(odemeList.getGidisDonusIndirimi());
        holder.odenecekTutar.setText(odemeList.getToplamFiyat());
        

    }

    @Override
    public int getItemCount() {
        return OdemeList.size();
    }

    public static class OdemeViewHolder extends RecyclerView.ViewHolder {

        TextView biletFiyatiGenelTittle , biletFiyatiTittle ,uyelikIndirimiTittle,gidisDonusIndirimiTittle, odenecekTutarTittle, biletFiyati , uyelikIndirimi, gidisDonusIndirimi, odenecekTutar;

        CardView cardView;

        LinearLayout biletUcretiGenelLayout,biletFiyatiLayout,uyelikIndirimiLayout,gidisDonusIndirimiLayout,odenecekTutarLayout;
        



        @SuppressLint("CutPasteId")
        public OdemeViewHolder(@NonNull View itemView) {
            super(itemView);
            biletFiyatiGenelTittle = itemView.findViewById(R.id.BiletUcretiGenelId);
            biletFiyatiTittle = itemView.findViewById(R.id.biletFiyatiTittleTextView);
            uyelikIndirimiTittle = itemView.findViewById(R.id.UyelikIndirimiTittleTextView);
            gidisDonusIndirimiTittle = itemView.findViewById(R.id.gidisDonusIndirimiTextView);
            odenecekTutarTittle = itemView.findViewById(R.id.OdenecekTutarTittleTextView);
            biletFiyati = itemView.findViewById(R.id.biletFiyatiTextView);
            uyelikIndirimi = itemView.findViewById(R.id.uyelikIndirimiTextView);
            gidisDonusIndirimi = itemView.findViewById(R.id.gidisDonusIndirimiTextView);
            odenecekTutar = itemView.findViewById(R.id.odenecekTutarTextView);
            cardView = itemView.findViewById(R.id.itemCardView);
            biletUcretiGenelLayout = itemView.findViewById(R.id.UcretBilgisiGenelLayout);
            biletFiyatiLayout = itemView.findViewById(R.id.BiletFiyatiLayout);
            uyelikIndirimiLayout = itemView.findViewById(R.id.UyelikIndirimiLayout);
            gidisDonusIndirimiLayout = itemView.findViewById(R.id.GidisDonusIndirimiLayout);
            odenecekTutarLayout = itemView.findViewById(R.id.OdenecekTutarLayout);
        }

    }



}

