package android.bignerdranch.mycheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mWebView = (WebView) findViewById(R.id.webview);
        //mWebView.getSettings().setJavaScriptEnabled(true);

        //mWebView.setWebViewClient(new WebViewClient(){
        //    @Override
        //    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
        //        return false;
        //    }
        //});

        mWebView.loadUrl("https://www.wikihow.com/Check-In-on-Facebook");
    }
}
