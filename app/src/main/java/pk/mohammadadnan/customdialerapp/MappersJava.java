package pk.mohammadadnan.customdialerapp;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;

import org.jetbrains.annotations.NotNull;

import static android.telecom.Call.STATE_ACTIVE;
import static android.telecom.Call.STATE_CONNECTING;
import static android.telecom.Call.STATE_DIALING;
import static android.telecom.Call.STATE_DISCONNECTED;
import static android.telecom.Call.STATE_RINGING;

public class MappersJava {
    @TargetApi(Build.VERSION_CODES.M)
    public static GsmCall toGsmCall(@NotNull Call call) {
        GsmCall.Status status = toGsmCallStatus(call.getState());
        String displayName = call.getDetails().getHandle().getSchemeSpecificPart();
        return new GsmCall(status, displayName);
    }

    private static final GsmCall.Status toGsmCallStatus(int callState) {
        GsmCall.Status gsmCallState;
        switch(callState) {
            case STATE_DIALING:
                gsmCallState = GsmCall.Status.DIALING;
                break;
            case STATE_RINGING:
                gsmCallState = GsmCall.Status.RINGING;
                break;
            case STATE_ACTIVE:
                gsmCallState = GsmCall.Status.ACTIVE;
                break;
            case STATE_DISCONNECTED:
                gsmCallState = GsmCall.Status.DISCONNECTED;
                break;
            case STATE_CONNECTING:
                gsmCallState = GsmCall.Status.CONNECTING;
                break;
            default:
                gsmCallState = GsmCall.Status.UNKNOWN;
                break;
        }

        return gsmCallState;
    }
}
