
package shuja.com.accuweather.entity.weather5days;

import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("pod")
    private String pod;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

}
