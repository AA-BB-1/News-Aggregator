package com.example.en;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.en.adapter.RssItemAdapter;
import com.example.en.databinding.ActivityMainBinding;
import com.example.en.model.RssFeed;
import com.example.en.model.RssItem;
import com.example.en.util.RssFeedManager;
import com.example.en.util.RssParser;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RssItemAdapter adapter;
    private RssParser rssParser;
    private List<RssFeed> rssFeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化数据
        rssParser = RssParser.getInstance();
        rssFeeds = RssFeedManager.getPredefinedFeeds();

        // 初始化UI
        setSupportActionBar(binding.toolbar);
        setupRecyclerView();
        setupSwipeRefresh();
        setupTabs();
        
        // 加载第一个RSS源
        if (!rssFeeds.isEmpty()) {
            loadRssFeed(rssFeeds.get(0).getUrl());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        adapter = new RssItemAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            int selectedPosition = binding.tabLayout.getSelectedTabPosition();
            if (selectedPosition >= 0 && selectedPosition < rssFeeds.size()) {
                loadRssFeed(rssFeeds.get(selectedPosition).getUrl());
            }
        });
    }

    private void setupTabs() {
        if (rssFeeds != null && !rssFeeds.isEmpty()) {
            for (RssFeed feed : rssFeeds) {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(feed.getTitle()));
            }

            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    if (position < rssFeeds.size()) {
                        loadRssFeed(rssFeeds.get(position).getUrl());
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        } else {
            Toast.makeText(this, "没有可用的RSS源", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRssFeed(String url) {
        binding.swipeRefreshLayout.setRefreshing(true);
        
        rssParser.parseRssFeed(url, new RssParser.OnRssParseListener() {
            @Override
            public void onSuccess(List<RssItem> items) {
                runOnUiThread(() -> {
                    adapter.setItems(items);
                    binding.swipeRefreshLayout.setRefreshing(false);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, 
                        "加载失败: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                    binding.swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }
}