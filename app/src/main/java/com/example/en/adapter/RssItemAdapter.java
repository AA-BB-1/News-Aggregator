package com.example.en.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.en.ArticleDetailActivity;
import com.example.en.R;
import com.example.en.model.RssItem;

import java.util.ArrayList;
import java.util.List;

public class RssItemAdapter extends RecyclerView.Adapter<RssItemAdapter.ViewHolder> {
    private List<RssItem> items = new ArrayList<>();

    public void setItems(List<RssItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RssItem item = items.get(position);
        holder.titleText.setText(item.getTitle());
        holder.descriptionText.setText(item.getDescription());
        holder.dateText.setText(item.getPubDate());

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(holder.imageView.getContext())
                    .load(item.getImageUrl())
                    .into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ArticleDetailActivity.class);
            intent.putExtra(ArticleDetailActivity.EXTRA_URL, item.getLink());
            intent.putExtra(ArticleDetailActivity.EXTRA_TITLE, item.getTitle());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView;
        final TextView titleText;
        final TextView descriptionText;
        final TextView dateText;
        final ImageView imageView;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            titleText = view.findViewById(R.id.titleText);
            descriptionText = view.findViewById(R.id.descriptionText);
            dateText = view.findViewById(R.id.dateText);
            imageView = view.findViewById(R.id.imageView);
        }
    }
} 