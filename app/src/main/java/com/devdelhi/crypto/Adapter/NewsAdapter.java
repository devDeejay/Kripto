package com.devdelhi.crypto.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.devdelhi.crypto.R;

import com.devdelhi.crypto.Data.NewsData;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {

    NewsData[] newsDataArray;
    Context mContext;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public NewsAdapter(NewsData[] newsArray, final Context mContext, OnItemClickListener listener) {
        newsDataArray = newsArray;
        this.mContext = mContext;
        mListener = listener;
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_news_data,
                parent,
                false
        );
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Bind To Views


        NewsData temp = newsDataArray[position];

        String description = temp.getDescription();
        if (description.length() > 120){
            description = description.substring(0,119);
            description += "... (Tap To Read More)";
        }

        String heading = temp.getTitle();
        if (heading.length() > 100){
            heading = heading.substring(0,99);
            heading += "...";
        }


        holder.newsHeading.setText(heading);
        holder.newsDescription.setText(description);
        Picasso.with(mContext).load(temp.getUrlToImage()).into(holder.newsImageView);

    }

    @Override
    public int getItemCount() {
        return newsDataArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Define The Layouts Here To Which Binding Is To Be Done

        public TextView newsDescription;
        public TextView newsHeading;
        public ImageView newsImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            Context context = itemView.getContext();
            Typeface tfc_heading = Typeface.createFromAsset(context.getAssets(), "fonts/news_heading_font.ttf");
            Typeface tfc = Typeface.createFromAsset(context.getAssets(), "fonts/news_description_font.ttf");

            newsDescription = itemView.findViewById(R.id.newsDescriptionTV);
            newsHeading = itemView.findViewById(R.id.newsHeadingTV);
            newsImageView = itemView.findViewById(R.id.newsImageIV);

            newsDescription.setTypeface(tfc);
            newsHeading.setTypeface(tfc_heading);

        }
    }
}
