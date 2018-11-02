package com.devdelhi.crypto.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devdelhi.crypto.Data.CurrencyHistory;

import com.devdelhi.crypto.R;
import java.text.ParseException;
import java.util.Date;


public class Adapter_History_Price extends RecyclerView.Adapter<Adapter_History_Price.ViewHolder> {

    CurrencyHistory[] currencyHistoryArray;
    Context mContext;
    String symbol;

    public Adapter_History_Price(CurrencyHistory[] currencyFutureArray, Context mContext, String toSymbol) {
        this.currencyHistoryArray = currencyFutureArray;
        this.mContext = mContext;
        symbol = toSymbol;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create Views
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_past_data,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Bind The Data Here

        CurrencyHistory temp = currencyHistoryArray[position];
        holder.close.setText( symbol + " " + temp.getClose());
        try {
            holder.date.setText(getDate(temp.getTime()));
            Log.d("DATE", getDate(temp.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.high.setText("High\n " + symbol + " " + temp.getHigh());
        holder.low.setText("Low\n" + symbol + " " + temp.getLow());
        holder.open.setText("Open\n" + symbol + " " + temp.getOpen());
    }

    public String getDate(String s) throws ParseException {

        long epochTimeInSeconds = Long.valueOf(s);
        Date date = new Date(epochTimeInSeconds * 1000);
        String finalValue = date+"";
        finalValue = finalValue.substring(0,10);
        return finalValue;
    }

    @Override
    public int getItemCount() {
        return currencyHistoryArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Define The Layouts Here To Which Binding Is To Be Done

        public TextView open;
        public TextView date;
        public TextView high;
        public TextView low;
        public TextView close;

        public ViewHolder(View itemView) {
            super(itemView);

            Context context = itemView.getContext();
            Typeface bold = Typeface.createFromAsset(context.getAssets(), "fonts/mediumbold.ttf");
            Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/semibold.ttf");

            close = itemView.findViewById(R.id.list_item_price);
            date = itemView.findViewById(R.id.list_item_timeTV);
            open = itemView.findViewById(R.id.list_item_openPriceTV);
            high = itemView.findViewById(R.id.list_item_highPriceTV);
            low = itemView.findViewById(R.id.list_item_lowPriceTV);

            close.setTypeface(bold);
            date.setTypeface(regular);
            open.setTypeface(regular);
            high.setTypeface(regular);
            low.setTypeface(regular);
        }
    }
}
