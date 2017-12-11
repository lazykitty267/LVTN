package bk.lvtn;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bk.lvtn.data.EventFromServer;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteActivity extends Fragment {

    CalendarView calendarView;
    private List<EventDay> mEventDays = new ArrayList<>();
    public NoteActivity() {
        // Required empty public constructor
    }
    FloatingActionButton addEventBtn;
    Dialog eventDetailDialog ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ghi chú");
        final View view = inflater.inflate(R.layout.fragment_note, container, false);
        calendarView = (CalendarView)view.findViewById(R.id.calendar_note);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if(eventDay instanceof EventFromServer){
                    eventDetailDialog = new Dialog(getActivity());
                    eventDetailDialog.setContentView(R.layout.event_detail_dialog);
                    eventDetailDialog.setTitle("Chi tiết");
                    Button okDialogButton = (Button) eventDetailDialog.findViewById(R.id.event_ok_btn);
                    okDialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eventDetailDialog.cancel();
                        }
                    });
                    eventDetailDialog.show();
                }
                else {
                    // Add event
                    EventFromServer myEventDay =
                            new EventFromServer(calendarView.getFirstSelectedDate(), R.drawable.ic_menu_report, "abc", "abc",true);
                    mEventDays.add(myEventDay);
                    calendarView.setEvents(mEventDays);
                }

            }
        });

        return view;
    }

}
