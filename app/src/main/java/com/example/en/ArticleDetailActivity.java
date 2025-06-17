package com.example.en;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.en.databinding.ActivityArticleDetailBinding;
import com.google.android.material.appbar.AppBarLayout;

public class ArticleDetailActivity extends AppCompatActivity {
    private ActivityArticleDetailBinding binding;
    public static final String EXTRA_URL = "article_url";
    public static final String EXTRA_TITLE = "article_title";
    private int lastScrollY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String url = getIntent().getStringExtra(EXTRA_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);

        setupToolbar(title);
        setupWebView();
        setupScrollBehavior();

        if (url != null) {
            binding.webView.loadUrl(url);
        }
    }

    private void setupToolbar(String title) {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progressIndicator.setVisibility(View.GONE);
            }
        });

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    binding.progressIndicator.setVisibility(View.VISIBLE);
                    binding.progressIndicator.setProgress(newProgress);
                } else {
                    binding.progressIndicator.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupScrollBehavior() {
        binding.scrollView.setOnScrollChangeListener(
            (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY > oldScrollY && scrollY > 0) {
                    // 向下滚动，隐藏工具栏
                    binding.appBarLayout.setExpanded(false, true);
                } else if (scrollY < oldScrollY) {
                    // 向上滚动，显示工具栏
                    binding.appBarLayout.setExpanded(true, true);
                }
            });
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
} 