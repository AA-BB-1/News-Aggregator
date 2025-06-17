package com.example.en;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.en.adapter.RssItemAdapter;
import com.example.en.databinding.ActivitySearchBinding;
import com.example.en.model.RssItem;
import com.example.en.util.RssFeedManager;
import com.example.en.util.RssParser;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private RssItemAdapter adapter;
    private List<RssItem> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupRecyclerView();
        setupSearchInput();
        loadAllFeeds();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new RssItemAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupSearchInput() {
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch(binding.searchEditText.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void loadAllFeeds() {
        RssParser parser = RssParser.getInstance();
        List<String> loadedFeeds = new ArrayList<>();

        for (int i = 0; i < RssFeedManager.getPredefinedFeeds().size(); i++) {
            String url = RssFeedManager.getPredefinedFeeds().get(i).getUrl();
            parser.parseRssFeed(url, new RssParser.OnRssParseListener() {
                @Override
                public void onSuccess(List<RssItem> items) {
                    runOnUiThread(() -> {
                        allItems.addAll(items);
                        loadedFeeds.add(url);
                        if (loadedFeeds.size() == RssFeedManager.getPredefinedFeeds().size()) {
                            // 所有源加载完成，执行初始搜索
                            performSearch(binding.searchEditText.getText().toString());
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    loadedFeeds.add(url);
                }
            });
        }
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            adapter.setItems(allItems);
            binding.emptyView.setVisibility(View.GONE);
            return;
        }

        List<RssItem> searchResults = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase();

        for (RssItem item : allItems) {
            if (item.getTitle().toLowerCase().contains(lowercaseQuery) ||
                item.getDescription().toLowerCase().contains(lowercaseQuery)) {
                searchResults.add(item);
            }
        }

        adapter.setItems(searchResults);
        binding.emptyView.setVisibility(searchResults.isEmpty() ? View.VISIBLE : View.GONE);
    }
} 