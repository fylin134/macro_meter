package com.example.frank_eltank.macro_meter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    // User data
    private String mFirstTimeLaunch = "mFirstTimeLaunch";
    private String mAge = "mAge";
    private String mGender = "mGender";
    private String mWeight = "mWeight";
    private String mHeightFeet = "mHeightFeet";
    private String mHeightInch = "mHeightInch";
    private String mActivity = "mActivity";

    // UI Elements
    EditText mEditText_Age = (EditText) findViewById(R.id.editText_age);
    CheckBox mCheckBoxMale = (CheckBox) findViewById(R.id.checkBox_male);
    CheckBox mCheckBoxFemale = (CheckBox) findViewById(R.id.checkBox_female);
    EditText mEditText_Weight;
    EditText mEditText_HeightFeet;
    EditText mEditText_HeightInch;
    Spinner mSpinner_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // If mFirstTimeLaunch doesn't exist, then we get a default true value
        // otherwise it will be false in SharedPrefs
        if(settings.getBoolean(mFirstTimeLaunch, true)){
            editor.putBoolean(mFirstTimeLaunch, false);
            editor.commit();

            // Render first time launch layout
            setContentView(R.layout.oob_launch);
            mSpinner_Activity = (Spinner) findViewById(R.id.spinner_activity);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.activity_level, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner_Activity.setAdapter(adapter);
        }
        // Render default main activity layout
        else{
            setContentView(R.layout.actviity_main);
        }

        // Storing user data on confirm button click
        Button confirm = (Button) findViewById(R.id.button_dataconfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for age value set
                if(mEditText_Age.getText().toString() == "0"){
                    mEditText_Age.setBackgroundColor(Color.RED);
                    displayMissingFieldToast();
                }
                else if(mCheckBoxMale.isChecked() ^ mCheckBoxFemale.isChecked()){

                }

                // Check for spinner value set
                mSpinner_Activity = (Spinner) findViewById(R.id.spinner_activity);
                if(mSpinner_Activity.getSelectedItemPosition() == 0){
                    mSpinner_Activity.setBackgroundColor(Color.RED);
                    displayMissingFieldToast();
                }

            }
        });

    }

    private void displayMissingFieldToast(){
        Toast.makeText(getApplicationContext(), "Please fill in the missing fields", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
