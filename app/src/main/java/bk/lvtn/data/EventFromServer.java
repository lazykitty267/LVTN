package bk.lvtn.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;

import entity.Note;

/**
 * Created by Phupc on 12/11/17.
 */

public class EventFromServer extends EventDay {
    public Note getEvent() {
        return event;
    }

    public void setEvent(Note event) {
        this.event = event;
    }

    private Note event;



    public EventFromServer(Calendar day, int imageResource, Note event) {
        super(day, imageResource);
        this.event = event;

    }

}
