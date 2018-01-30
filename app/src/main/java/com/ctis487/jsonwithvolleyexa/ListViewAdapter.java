package com.ctis487.jsonwithvolleyexa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter  extends ArrayAdapter<HashMap<String,String>>{
    Context context;
    ArrayList<HashMap<String,String>> kitaplarListesi;
    LayoutInflater mInflater;
    String imageaddress="http://ctis.bilkent.edu.tr/ctis487/jsonBook/";

    public ListViewAdapter(Context context, ArrayList<HashMap<String,String>> kitaplarListesi) {
        super(context, R.layout.list_item, kitaplarListesi);
        this.kitaplarListesi = kitaplarListesi;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = mInflater.inflate(R.layout.list_item, parent, false);

        ImageView img = (ImageView)view.findViewById(R.id.bookimg);
        TextView tvYear = (TextView)view.findViewById(R.id.bookyear);

        HashMap<String,String> kitap = kitaplarListesi.get(position);
        tvYear.setText(kitap.get(MainActivity.TAG_YEAR)+"");

        String imagenameAddress=kitap.get(MainActivity.TAG_IMG);
        Log.d("image",imagenameAddress);

        Picasso.with(context)
                .load(imagenameAddress)
                .into(img);
        return view;
    }
}





























