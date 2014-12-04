package com.glorygrow.glancecartoons;

import android.support.v7.appcompat.*;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.FrameStats;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.*;

public class WebViewActivity extends Activity {

	WebView mWebView;
	ProgressBar mProgressBar;
	private InterstitialAd interstitial;
	private AdView adView;
	CountDownTimer mCDTimer;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_webview);

        mCDTimer = new CountDownTimer(300000, 60000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				Log.i("", "onTick sec : " + millisUntilFinished);
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Log.i("", "onFinsh");
				AdRequest adRequest2 = new AdRequest.Builder().build();
				interstitial.loadAd(adRequest2);
				mCDTimer.start();
			}
		};
		mCDTimer.start();
        
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-1060814147242762/8065001337");
        interstitial.setAdListener(new AdListener() {
        	@Override
        	public void onAdLoaded() {
        		displayInterstitial();
        		super.onAdLoaded();
        	}
		});
        
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-1060814147242762/9541734539");
        adView.setAdSize(AdSize.BANNER);
        FrameLayout layoutAd = (FrameLayout)findViewById(R.id.webview_ad);
        layoutAd.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);        
        
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        
        Intent intent = getIntent();
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.setWebChromeClient(new ProgressBarWebChromeClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.loadUrl(intent.getStringExtra("url"));
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	mCDTimer.cancel();
    	if((keyCode==KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
    		mCDTimer.start();
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
    
    @Override
    public void onPause() {
    	Log.i("onPause", "onPause!!");
    	adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
    	Log.i("onResume", "onResume!!");
    	super.onResume();
    	adView.resume();
    }

    @Override
    public void onDestroy() {
    	Log.i("onDestroy", "onDestroy!!");
    	adView.destroy();
    	super.onDestroy();
    }
    
    @Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		mCDTimer.cancel();
		mCDTimer.start();
		return super.dispatchTouchEvent(ev);
	}
    
    public boolean displayInterstitial() {
    	Log.i("", "displayInterstitial!!");
    	if (interstitial.isLoaded()) {
    		Log.i("","show ad");
    		interstitial.show();
    		return true;
    	}
    	return false;
    }
}