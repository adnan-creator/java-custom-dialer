package pk.mohammadadnan.customdialerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SET_DEFAULT_DIALER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkDefaultDialer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SET_DEFAULT_DIALER){
            checkSetDefaultDialerResult(resultCode);
        }
    }

    private void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        boolean isAlreadyDefaultDialer = this.getPackageName().equals(telecomManager.getDefaultDialerPackage());
        if (!isAlreadyDefaultDialer) {
            Intent intent = (new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER))
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, this.getPackageName());
            this.startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER);
        }
    }

    private void checkSetDefaultDialerResult(int resultCode){
        String message;
        switch (resultCode){
            case RESULT_OK:message = "User accepted request to become default dialer";break;
            case RESULT_CANCELED:message = "User declined request to become default dialer";break;
            default:message = "Unexpected result code: " + resultCode;break;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}