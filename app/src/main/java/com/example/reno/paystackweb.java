package com.example.reno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class paystackweb extends AppCompatActivity {
    private WebView webView = null;
    SwipeRefreshLayout myswipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paystackweb);

        this.webView = (WebView)findViewById(R.id.webview);
        myswipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeContainer);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().getDisplayZoomControls();

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);


        webView.loadUrl("https://paystack.com/pay/6mlk5vttvx");

        myswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

    }
    @Override
    public boolean onKeyDown(int Keycode, KeyEvent event){
        if ((Keycode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()){
            this.webView.canGoBack();
        }
        return
                super.onKeyDown(Keycode, event);
    }
}
