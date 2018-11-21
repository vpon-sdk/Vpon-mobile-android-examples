package com.vpon.dmp_tutorial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.vpadn.dmp.VpadnAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String LT = "MainActivity";

    private static final String licenseKey = "{your license key}";
    private static final String customerId = "{your customer id}";
    private static VpadnAnalytics analytics;
    private Button sendLaunchEventBtn;
    private Button sendItemViewEventBtn;
    private Button sendPageViewEventBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendLaunchEventBtn = (Button)findViewById(R.id.launchEventBtn);
        sendItemViewEventBtn = (Button)findViewById(R.id.itemViewBtn);
        sendPageViewEventBtn = (Button)findViewById(R.id.pageViewBtn);

        sendLaunchEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics = VpadnAnalytics.getInstance(MainActivity.this, licenseKey);
                VpadnAnalytics.Tracker tracker = analytics.newTracker();
                tracker.sendLaunchEvent(customerId);
            }
        });


        sendItemViewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics = VpadnAnalytics.getInstance(MainActivity.this, licenseKey);
                VpadnAnalytics.Tracker tracker = analytics.newTracker();
                JSONObject payloadJsonObj = new JSONObject();
                try {
                    payloadJsonObj.put("id","payloadJsonObj");
                    payloadJsonObj.put("name","0323新品 甜美彩搭~V領布蕾絲拼接針織外套．8色");
                    payloadJsonObj.put("price",100);
                    payloadJsonObj.put("color","牛藍");
                    payloadJsonObj.put("size","XL");
                    payloadJsonObj.put("tags","OrangeBear,上衣類,針織衫,人造絲纖維");
                    payloadJsonObj.put("currency","NTD");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tracker.sendEvent("item_view",payloadJsonObj,customerId);
            }
        });

        sendPageViewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics = VpadnAnalytics.getInstance(MainActivity.this, licenseKey);
                VpadnAnalytics.Tracker tracker = analytics.newTracker();
                JSONObject payloadJsonObj = new JSONObject();
                try {
                    payloadJsonObj.put("current","http://www.obdesign.com.tw/product.aspx?seriesID=BA1336-&no=158");
                    payloadJsonObj.put("pervious","http://www.obdesign.com.tw/inpage.aspx?no=158");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tracker.sendEvent("page_view",payloadJsonObj,"custom123456-pageView");

            }
        });
    }
}
