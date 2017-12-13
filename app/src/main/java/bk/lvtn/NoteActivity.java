package bk.lvtn;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bk.lvtn.data.EventFromServer;
import dataService.DataService;
import dataService.DatabaseConnection;
import entity.Note;
import entity.Report;
import entity.User;


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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ghi chú");
        final View view = inflater.inflate(R.layout.fragment_note, container, false);
        calendarView = (CalendarView)view.findViewById(R.id.calendar_note);
        calendarView.setEvents(mEventDays);
        //get event from firebase and add to calendarview
        //connect to note db
        DatabaseConnection databaseConnection = new DatabaseConnection();
        DatabaseReference databaseReference = databaseConnection.connectNoteDatabase();
        //30p break
        DataService dataService = new DataService();
        User user = dataService.getCurrentUser(getActivity());
        databaseReference.child(user.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Note event = postSnapshot.getValue(Note.class);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date date = formatter.parse(event.getDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        if(event.isUserCreated()){
                            EventFromServer myEventDay =
                                    new EventFromServer(cal, R.mipmap.ic_user_event,event );
                            mEventDays.add(myEventDay);
                            calendarView.setEvents(mEventDays);
                        }
                        else {
                            EventFromServer myEventDay =
                                    new EventFromServer(cal, R.mipmap.ic_system_event,event );
                            mEventDays.add(myEventDay);
                            calendarView.setEvents(mEventDays);
                        }
                    }
                    catch (ParseException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if(eventDay instanceof EventFromServer){
                    eventDetailDialog = new Dialog(getActivity());
                    eventDetailDialog.setContentView(R.layout.event_detail_dialog);
                    eventDetailDialog.setTitle("Chi tiết");
                    TextView eventTime = (TextView) eventDetailDialog.findViewById(R.id.event_time);
                    TextView eventPlace = (TextView) eventDetailDialog.findViewById(R.id.event_place);
                    TextView eventDes = (TextView) eventDetailDialog.findViewById(R.id.event_des);
                    eventTime.setText(((EventFromServer) eventDay).getEvent().getTime());
                    eventPlace.setText(((EventFromServer) eventDay).getEvent().getPlace());
                    eventDes.setText(((EventFromServer) eventDay).getEvent().getContent());
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
                    View dialogView = inflater.inflate(R.layout.new_event_dialog,null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(dialogView);
                    String titleText = "Sự kiện mới";
                    // Initialize a new foreground color span instance
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

                    // Initialize a new spannable string builder instance
                    SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

                    // Apply the text color span
                    ssBuilder.setSpan(
                            foregroundColorSpan,
                            0,
                            titleText.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                    builder.setTitle(ssBuilder);
                    final TimePicker eventTime = (TimePicker) dialogView.findViewById(R.id.event_time);
                    eventTime.setIs24HourView(true);
                    final EditText eventPlace = (EditText) dialogView.findViewById(R.id.event_place);
                    final EditText eventDes= (EditText) dialogView.findViewById(R.id.event_des);
                    final EditText eventCreater= (EditText) dialogView.findViewById(R.id.event_creater);
                    eventCreater.setText("admin");
                    eventCreater.setClickable(false);

                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("Tạo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Note event = new Note();
                            event.setUserName(eventCreater.getText().toString());
                            event.setTime(String.valueOf(eventTime.getCurrentHour()+":"+String.valueOf(eventTime.getCurrentMinute())));
                            event.setContent(eventDes.getText().toString());
                            event.setPlace(eventPlace.getText().toString());
                            event.setUserCreated(true);
                            event.setDate(new SimpleDateFormat("dd/MM/yyyy").format(calendarView.getFirstSelectedDate().getTime()));
                            //save to firebase
                            DataService dataService = new DataService();
                            User user = dataService.getCurrentUser(getActivity());
                            event.setUserName(user.getUsername());
                            dataService.saveNote(event);
                            // add event to calendar view
                            EventFromServer myEventDay =
                                    new EventFromServer(calendarView.getFirstSelectedDate(), R.mipmap.ic_user_event,event );
                            mEventDays.add(myEventDay);
                            calendarView.setEvents(mEventDays);
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        return view;
    }

}
