package firm.vpon.smaato_mediation_vpon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.BannerView;
import com.smaato.soma.ReceivedBannerInterface;
import com.smaato.soma.bannerutilities.constant.BannerStatus;
import com.smaato.soma.debug.Debugger;
import com.smaato.soma.interstitial.*;

public class MainActivity extends AppCompatActivity implements AdListenerInterface {

    private BannerView smaatoBanner;
    private Interstitial smaatoIS;

    private Button btn_banner;
    private Button btn_interstital;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Debugger.setDebugMode(Debugger.Level_3);

        btn_banner = (Button) findViewById(R.id.btn_banner);
        btn_interstital = (Button) findViewById(R.id.btn_interstitial);
        smaatoIS = new Interstitial(this);
        smaatoIS.getAdSettings().setPublisherId(1100040241); // Enter your Ad Publisher ID from https://spx.smaato.com/
        smaatoIS.getAdSettings().setAdspaceId(130399802); // Enter your Ad space ID from https://spx.smaato.com/
        smaatoIS.asyncLoadNewBanner();

        smaatoIS.setInterstitialAdListener(new InterstitialAdListener() {
            @Override
            public void onReadyToShow() {
                // interstitial is loaded an may be shown
                btn_interstital.setText("show Interstitial");
            }

            @Override
            public void onWillShow() {
                // called immediately before the interstitial is shown
            }

            @Override
            public void onWillOpenLandingPage() {
                // called immediately before the landing page is opened, after the user clicked
            }

            @Override
            public void onWillClose() {
                // called immediately before the interstitial is dismissed
            }

            @Override
            public void onFailedToLoadAd() {
                // loading the interstitial has failed
            }
        });

    }

    public void showBanner(View v){
        smaatoBanner = (BannerView) findViewById(R.id.adview);
        smaatoBanner.getAdSettings().setPublisherId(1100040241); // Enter your Ad Publisher ID from https://spx.smaato.com/
        smaatoBanner.getAdSettings().setAdspaceId(130399801); // Enter your Ad space ID from https://spx.smaato.com/
        smaatoBanner.addAdListener(this);
        smaatoBanner.asyncLoadNewBanner();
    }

    public void showInterstitial(View v){
        if(smaatoIS.isInterstitialReady()) {
            smaatoIS.show();
        }
    }

    @Override
    protected void onDestroy() {
        if (smaatoBanner != null) {
            smaatoBanner.destroy();
        }
        if (smaatoIS != null) {
            smaatoIS.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onReceiveAd(AdDownloaderInterface adDownloaderInterface, ReceivedBannerInterface receivedBannerInterface) {
        if (receivedBannerInterface.getStatus() == BannerStatus.ERROR) {
            Toast.makeText(getApplicationContext(),
                    "Banner Failed loaded.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Banner successfully loaded.", Toast.LENGTH_SHORT).show();
        }
    }

}
