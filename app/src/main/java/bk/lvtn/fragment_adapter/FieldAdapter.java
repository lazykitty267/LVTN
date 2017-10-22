package bk.lvtn.fragment_adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import bk.lvtn.R;

/**
 * Created by Phupc on 10/22/17.
 */

public class FieldAdapter extends ArrayAdapter<Field> {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Context context;
    ArrayList<Field> listField;
    int resId;
    EditText valueField = null;
    Field field;
    int pos;
    public FieldAdapter(Context context, ArrayList<Field> listField, int resId){
        super(context,resId,listField);
        this.context = context;
        this.listField = listField;
        this.resId = resId;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resId,null);
        TextView keyField = (TextView)convertView.findViewById(R.id.key_field);
        valueField = (EditText) convertView.findViewById(R.id.value_field);
        Button voiceButton = (Button) convertView.findViewById(R.id.voice_button);
        field = listField.get(position);
        keyField.setText(field.getKey_field());
        valueField.setText(field.getValue_field());

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
//                field = listField.get(position);
//                pos = position;
//                field.setValue_field("aaaa");

            }
        });
        return convertView;

    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                context.getString(R.string.speech_prompt));
        Bundle bundle = new Bundle();
        bundle.putInt("pos",pos);
        intent.putExtra("position",bundle);
        try {
            ((Activity)context).startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context,
                    "Sorry! Your device doesn\\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == ((Activity)context).RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                Toast.makeText(context, result.get(0),
                    Toast.LENGTH_SHORT).show();
                    field = listField.get(pos);
                    field.setValue_field("vcvcvcv");
                    valueField.setText("vcvcvcv");
                    valueField.setSelection(valueField.getText().length());
                    notifyDataSetChanged();
                }
                break;
            }

        }
    }
}
