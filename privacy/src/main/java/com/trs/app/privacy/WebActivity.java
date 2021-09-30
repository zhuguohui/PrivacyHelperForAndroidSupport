package com.trs.app.privacy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.Serializable;

public class WebActivity extends AppCompatActivity {

    private static class WebArg implements Serializable {
        final String url;
        final String title;

        private WebArg(String url, String title) {
            this.url = url;
            this.title = title;
        }
    }

    public static void open(Context ctx, String url, String title) {
        WebArg webArg=new WebArg(url,title);
        Intent intent=new Intent(ctx,WebActivity.class);
        intent.putExtra(WebArg.class.getName(),webArg);
        ctx.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView webView = findViewById(R.id.webView);
        WebArg webArg= (WebArg) getIntent().getSerializableExtra(WebArg.class.getName());
        setWebView(webView);
        webView.loadUrl(webArg.url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        TextView tvTitle=findViewById(R.id.tv_title);
        tvTitle.setText(webArg.title);
        findViewById(R.id.iv_back).setOnClickListener(v->finish());
    }
    private void setWebView(WebView webView){
        WebSettings webSetting = webView.getSettings();
        webSetting.setSupportZoom(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
    }
}