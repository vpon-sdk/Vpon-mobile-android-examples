package com.adsmogo.adsmogobanner;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.adsmogo.interstitial.AdsMogoInterstitialListener;
import com.adsmogo.interstitial.AdsMogoInterstitialManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements AdsMogoListener,AdsMogoInterstitialListener{
	
	
	private RelativeLayout relativeLayout;
	public Button showBt,cancelBt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		showBt = (Button) findViewById(R.id.main_interstitial_show_bt);
		cancelBt = (Button)findViewById(R.id.main_interstitial_cancel_bt);
		
		relativeLayout=(RelativeLayout) findViewById(R.id.adLayout);
		
		
		AdsMogoLayout adsMogoLayoutCode= new AdsMogoLayout(this,"");
		adsMogoLayoutCode.setAdsMogoListener(this);
		relativeLayout.addView(adsMogoLayoutCode);
		
		
		initInterstitial();
		showBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("", "Here");
				// TODO Auto-generated method stub
				if (AdsMogoInterstitialManager.shareInstance().containDefaultInterstitia()) { //判断是否存在默认全插屏对象
					/**
					 * 展示全插屏
					 * 参数解释：是否等待全插屏广告的展示，true表示等待，false不等待
					 * 等待的逻辑：检查是否有全插屏缓存，若有则直接展示，若没有则等待请求到广告后立即展示(注意：在请求广告期间，没有调用interstitialCancel()取消全插屏等待方法时会展示，反则不会展示)。
					 * 不等待逻辑：检查是否有全插屏缓存，若有则直接展示，若没有则不等待。
					 */
					AdsMogoInterstitialManager.shareInstance().defaultInterstitial().interstitialShow(true);			
				} else {
					Toast.makeText(MainActivity.this, "全屏广告初始化失败！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		cancelBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initInterstitial() {
		// TODO Auto-generated method stub
		//AdsMogoInterstitialManager.setDefaultInitManualRefresh(true);
		
		AdsMogoInterstitialManager.setInitActivity(this);
		
		AdsMogoInterstitialManager.setDefaultInitAppKey("");
		
		AdsMogoInterstitialManager.shareInstance().initDefaultInterstitial();
		AdsMogoInterstitialManager.shareInstance().defaultInterstitial().setAdsMogoInterstitialListener(this);
	}
	
	@Override
	public Class getCustomEvemtPlatformAdapterClass(
			AdsMogoCustomEventPlatformEnum arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClickAd(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCloseAd() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCloseMogoDialog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailedReceiveAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRealClickAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveAd(ViewGroup arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestAd(String arg0) {
		// TODO Auto-generated method stub
		Log.e("请求的广告平台：", arg0);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		AdsMogoInterstitialManager.shareInstance()
				.removeDefaultInterstitialInstance();

	}

	
	
	@Override
	public void onInterstitialClickAd(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onInterstitialClickCloseButton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onInterstitialCloseAd(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View onInterstitialGetView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInterstitialRealClickAd(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onInterstitialStaleDated(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowInterstitialScreen(String arg0) {
		// TODO Auto-generated method stub
		
	};
	
	
}
