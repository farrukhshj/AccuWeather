package shuja.com.accuweather.entity.weather5days;


import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("3h")
    private Double _3h;

    public Double get3h() {
        return _3h;
    }

    public void set3h(Double _3h) {
        this._3h = _3h;
    }

}
