package shuja.com.accuweather.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeFunctions {

    public static String unixTimeToNormalTime(Integer unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        String formattedDate = simpleDateFormat.format(date);
        return formattedDate;
    }
}
