package shuja.com.accuweather.utility;

import android.support.annotation.Nullable;

public interface Callback<T> {
    /**
     * @param data      - the data supplied; if any, may be nullCallback
     * @param exception - the exception raised; if any, may be null
     */
    void onCallback(@Nullable T data, @Nullable Exception exception);
}
