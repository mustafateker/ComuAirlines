package com.project.comuhavayollari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UcusAraAdaptorItem extends BaseAdapter {
    private Context context;
    private List<UcusAraItem> ucusList;
    private LayoutInflater inflater;

    public UcusAraAdaptorItem(Context context, int ucus_ara_item, List<UcusAraItem> ucusList) {
        this.context = context;
        this.ucusList = ucusList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ucusList.size();
    }

    @Override
    public Object getItem(int position) {
        return ucusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ucus_ara_item, parent, false);
        }

        ImageView ucusLogo = convertView.findViewById(R.id.ucuslogoitem);
        TextView ucusNo = convertView.findViewById(R.id.ucusnoitem);
        TextView ucusSaati = convertView.findViewById(R.id.ucussaatiitem);
        TextView kalkisYeri = convertView.findViewById(R.id.kalkisyeriitem);
        ImageView yon = convertView.findViewById(R.id.yönitem);
        TextView varisYeri = convertView.findViewById(R.id.varisyeriitem);
        TextView fiyat = convertView.findViewById(R.id.fiyatitem);

        UcusAraItem ucus = ucusList.get(position);

        ucusLogo.setImageResource(ucus.getUcusLogo());
        ucusNo.setText(ucus.getUcusNo());
        ucusSaati.setText(ucus.getUcusSaati());
        kalkisYeri.setText(ucus.getKalkisYeri());
        yon.setImageResource(ucus.getYon());
        varisYeri.setText(ucus.getVarisYeri());
        fiyat.setText(ucus.getFiyat());

        return convertView;
    }
}
