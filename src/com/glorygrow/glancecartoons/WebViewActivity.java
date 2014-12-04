package com.glorygrow.glancecartoons;

import android.support.v7.appcompat.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;

public class WebViewActivity extends Activity {

	WebView mWebView;
	ProgressBar mProgressBar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_webview);
        
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        
        Intent intent = getIntent();
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.setWebChromeClient(new ProgressBarWebChromeClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		// mWebView.loadUrl("http://aetoys.tumblr.com/phone");
		mWebView.loadUrl(intent.getStringExtra("url"));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if((keyCode==KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
    		mWebView.goBack();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    public class ProgressBarWebChromeClient extends WebChromeClient {
    	
    	@Override
    	public void onProgressChanged(WebView view, int newProgress) {   
    		if (newProgress < 100)
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
            else if (newProgress == 100)
            	mProgressBar.setVisibility(ProgressBar.GONE);
    		
    		mProgressBar.setProgress(newProgress);
        } 
    }

    private class MyWebClient extends WebViewClient {

    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		view.loadUrl(url);
    		return super.shouldOverrideUrlLoading(view, url);
    	}
    }
}