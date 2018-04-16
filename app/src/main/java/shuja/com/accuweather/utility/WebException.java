package shuja.com.accuweather.utility;

import android.annotation.SuppressLint;

public class WebException extends Exception {
    private final int mCode;

    @SuppressLint("DefaultLocale")
    public WebException(int code, String url, String message) {
        super(String.format("Code %d: Received Error in attempting to execute a web call to %s (%s)", code, url, message));
        this.mCode = code;
    }

    public int getCode() {
        return mCode;
    }

}
