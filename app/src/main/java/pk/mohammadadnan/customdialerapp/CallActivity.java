package pk.mohammadadnan.customdialerapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import pk.mohammadadnan.customdialerapp.GsmCall.Status;

import static pk.mohammadadnan.customdialerapp.GsmCall.Status.ACTIVE;
import static pk.mohammadadnan.customdialerapp.GsmCall.Status.CONNECTING;
import static pk.mohammadadnan.customdialerapp.GsmCall.Status.DIALING;
import static pk.mohammadadnan.customdialerapp.GsmCall.Status.DISCONNECTED;
import static pk.mohammadadnan.customdialerapp.GsmCall.Status.RINGING;
import static pk.mohammadadnan.customdialerapp.GsmCall.Status.UNKNOWN;

public class CallActivity extends AppCompatActivity {
    private Disposable updatesDisposable;
    private Disposable timerDisposable;
    private TextView textDuration;
    private TextView textStatus;
    private ImageView buttonAnswer;
    private ImageView buttonHangup;
    private TextView textDisplayName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        hideBottomNavigationBar();


        textDuration = findViewById(R.id.textDuration);
        textStatus = findViewById(R.id.textStatus);
        buttonAnswer = findViewById(R.id.buttonAnswer);
        buttonHangup = findViewById(R.id.buttonHangup);
        textDisplayName = findViewById(R.id.textDisplayName);

        buttonHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallManager.cancelCall();
            }
        });
        buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallManager.acceptCall();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatesDisposable = CallManager.updates()
        .doOnError (throwable ->{ Log.e("LOG_TAG", "Error processing call");})
        .subscribe (it->{ updateView((GsmCall)it); });
    }

    private void updateView(GsmCall gsmCall) {
        if(!gsmCall.getStatus().equals(ACTIVE)){
            textStatus.setVisibility(View.VISIBLE);
            textDuration.setVisibility(View.GONE);
        }
        switch (gsmCall.getStatus()){
            case CONNECTING:textStatus.setText("Connecting…");break;
            case DIALING:textStatus.setText("Calling…");break;
            case RINGING:
                textStatus.setText("Incoming call");
                buttonAnswer.setVisibility(View.VISIBLE);
                break;
            case ACTIVE:
                textStatus.setText("");
                textStatus.setVisibility(View.GONE);
                textDuration.setVisibility(View.VISIBLE);
                startTimer();
                break;
            case DISCONNECTED:
                textStatus.setText("Finished call");
                buttonHangup.setVisibility(View.GONE);
                buttonHangup.postDelayed(() -> finish(), 3000);
                stopTimer();
                break;
            case UNKNOWN:textStatus.setText("");break;
        }

        if(!gsmCall.getStatus().equals(DISCONNECTED)){
            buttonHangup.setVisibility(View.VISIBLE);
        }
        if(!gsmCall.getStatus().equals(RINGING)){
            buttonAnswer.setVisibility(View.GONE);
        }
        if(gsmCall.getDisplayName().equals(null)){
            textDisplayName.setText("Unknown");
        }else{
            textDisplayName.setText(gsmCall.getDisplayName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void hideBottomNavigationBar(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // only for gingerbread and newer versions
            getWindow().setDecorFitsSystemWindows(false);
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void startTimer() {
        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(longTimeInterval -> {
                    textDuration.setText(toDurationString(longTimeInterval ));
                });
    }

    private void stopTimer() {
        timerDisposable.dispose();
    }

    private String toDurationString(Long l){
        return String.format("%02d:%02d:%02d", l / 3600, (l % 3600) / 60, (l % 60));
    }
}
