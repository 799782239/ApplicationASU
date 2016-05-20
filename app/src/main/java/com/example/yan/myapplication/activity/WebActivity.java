package com.example.yan.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.yan.myapplication.R;

public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private String WebURL = "http://120.27.122.62:146/main.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitle("数据统计");
        initTitle();
        mWebView = (WebView) findViewById(R.id.web);
        mWebView.loadUrl(WebURL);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

        });
    }
}
