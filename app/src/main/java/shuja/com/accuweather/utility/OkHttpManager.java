package shuja.com.accuweather.utility;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpManager {
    private static final long TIME_OUT_IN_SECONDS = 40;
    private static OkHttpManager _instance = null;

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private final OkHttpClient mOkHttpClient;

    public static OkHttpManager instance() {
        if (_instance == null) {
            synchronized (OkHttpManager.class) {
                if (_instance == null) {
                    _instance = new OkHttpManager();
                }
            }
        }
        return _instance;
    }

    public static Response performRequest(Request request) throws WebException, NoNetworkException, SocketTimeoutException {
        Response response = null;
        try {
            response = OkHttpManager.instance().getOkHttpClient().newCall(request).execute();
            checkResponse(response);
        } catch (UnknownHostException e) {
            // this happens when there is no internet.
            e.printStackTrace();
            throw new NoNetworkException();
        } catch (SocketTimeoutException e) {
            // if there was a time out
            // message title: [An Error Occurred!] body: [We've run into a problem.  Please try again.]
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            // anything else
            e.printStackTrace();
            throw new WebException(response.code(), response.request().url().toString(), "IOException: " + e.getMessage());
        }
        return response;
    }

    private static void checkResponse(Response response) throws WebException, IOException {
        if (response.isSuccessful()) {
            return;
        } else {
            String body = "";
            if (response.body() != null) {
                body = response.body().string();
            }
            throw new WebException(response.code(), response.request().url().toString(), body);
        }
    }

    private OkHttpManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);
        builder.readTimeout(TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }
}
