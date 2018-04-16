package shuja.com.accuweather.utility;

public class NoNetworkException extends Exception {
    public NoNetworkException() {
        super("There is no network connection");
    }
}
