package com.project.comuhavayollari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class DonusUcusAraAdaptor extends BaseAdapter {

    private Context context;
    private List<UcusAraItem> ucusList;
    private LayoutInflater inflater;

    public DonusUcusAraAdaptor(Context context, int donus_ucus_item, List<UcusAraItem> ucusList){
        this.context = context;
        this.ucusList = ucusList;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return ucusList.size();
    }

    public Object getItem(int position) {
        return ucusList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.donus_ucus_item, parent, false);
        }
        ImageView ucusLogo = convertView.findViewById(R.id.donusucuslogoitem);
        TextView ucusNo = convertView.findViewById(R.id.donusucusnoitem);
        TextView ucusSaati = convertView.findViewById(R.id.donusucussaatiitem);
        TextView kalkisYeri = convertView.findViewById(R.id.donuskalkisyeriitem);
        ImageView yon = convertView.findViewById(R.id.donusyonitem);
        TextView varisYeri = convertView.findViewById(R.id.donusvarisyeriitem);
        TextView fiyat = convertView.findViewById(R.id.donusfiyatitem);

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
