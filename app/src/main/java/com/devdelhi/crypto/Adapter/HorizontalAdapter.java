package com.devdelhi.crypto.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.devdelhi.crypto.Data.Currency;
import com.devdelhi.crypto.R;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>{

    private Currency[] items;

    public HorizontalAdapter(Currency[] items) {
        this.items = items;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_present_data, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.shortPriceView);
        }
    }
}
