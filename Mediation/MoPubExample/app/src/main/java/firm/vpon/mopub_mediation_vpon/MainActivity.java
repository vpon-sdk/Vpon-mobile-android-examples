package firm.vpon.mopub_mediation_vpon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

public class MainActivity extends AppCompatActivity  implements MoPubView.BannerAdListener{
    //your MoPub PlacementId
    private static final String mBannerPlacementId = "92904dd295fc4f50acb0809ef0ad3218";
    private MoPubView moPubView;
    private Button mNativeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moPubView = (MoPubView) findViewById(R.id.adview);
        moPubView.setAdUnitId(mBannerPlacementId); // Enter your Ad Unit ID from www.mopub.com
        moPubView.setBannerAdListener(this);
        moPubView.loadAd();

        mNativeButton = (Button) findViewById(R.id.nativeButton);
        mNativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NativeActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        moPubView.destroy();
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {
        Toast.makeText(getApplicationContext(),
                "Banner successfully loaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        Toast.makeText(getApplicationContext(),
                "Banner Failed loaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerClicked(MoPubView banner) {

    }

    @Override
    public void onBannerExpanded(MoPubView banner) {

    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {

    }
}
