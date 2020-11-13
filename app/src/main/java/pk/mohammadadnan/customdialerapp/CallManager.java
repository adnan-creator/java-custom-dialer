package pk.mohammadadnan.customdialerapp;

import android.telecom.Call;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class CallManager {

    private static BehaviorSubject subject;
    private static Call currentCall = null;
    public static CallManager INSTANCE;

    public static Observable updates() {
        BehaviorSubject behaviorSubject = subject;
        return (Observable)behaviorSubject;
    }

    public static void updateCall(@Nullable Call call) {
        currentCall = call;
        if (call != null) {
            subject.onNext(MappersJava.toGsmCall(call));
        }

    }

    public static void cancelCall() {
        Call call = currentCall;
        if (call != null) {
            if (call.getState() == Call.STATE_RINGING) {
                INSTANCE.rejectCall();
            } else {
                INSTANCE.disconnectCall();
            }
        }

    }

    public static void acceptCall() {
        Call call = currentCall;
        if (call != null) {
            call.answer(call.getDetails().getVideoState());
        }

    }

    private static void rejectCall() {
        Call call = currentCall;
        if (call != null) {
            call.reject(false, "");
        }

    }

    private static void disconnectCall() {
        Call call = currentCall;
        if (call != null) {
            call.disconnect();
        }

    }

    static {
        CallManager var0 = new CallManager();
        INSTANCE = var0;
        subject = BehaviorSubject.create();
    }
}
