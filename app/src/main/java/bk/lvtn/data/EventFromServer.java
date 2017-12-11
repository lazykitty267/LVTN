package bk.lvtn.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;

/**
 * Created by Phupc on 12/11/17.
 */

public class EventFromServer extends EventDay {
    private String mDes;
    private String mPlace;

    public boolean isUserCreated() {
        return isUserCreated;
    }

    private boolean isUserCreated;
    public String getmPlace() {
        return mPlace;
    }

    public EventFromServer(Calendar day, int imageResource, String des, String place,boolean isUserCreated) {
        super(day, imageResource);
        mDes = des;
        mPlace = place;
        this.isUserCreated = isUserCreated;
    }
    String getmDes() {
        return mDes;
    }

}
