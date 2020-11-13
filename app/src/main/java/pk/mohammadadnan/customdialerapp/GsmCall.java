package pk.mohammadadnan.customdialerapp;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class GsmCall {

    @NotNull
    private final GsmCall.Status status;
    @org.jetbrains.annotations.Nullable
    private final String displayName;

    @NotNull
    public final GsmCall.Status getStatus() {
        return this.status;
    }

    @org.jetbrains.annotations.Nullable
    public final String getDisplayName() {
        return this.displayName;
    }

    public GsmCall(@NotNull GsmCall.Status status, @Nullable String displayName) {
        super();
        this.status = status;
        this.displayName = displayName;
    }

    public enum  Status {
        CONNECTING,
        DIALING,
        RINGING,
        ACTIVE,
        DISCONNECTED,
        UNKNOWN
    }
}
