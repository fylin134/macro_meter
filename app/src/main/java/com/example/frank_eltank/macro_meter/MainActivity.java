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
import android.widget.TextView;
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
    private String mBMR = "mBMR";

    // UI Elements
    EditText mEditText_Age;
    CheckBox mCheckBoxMale;
    CheckBox mCheckBoxFemale;
    EditText mEditText_Weight;
    EditText mEditText_HeightFeet;
    EditText mEditText_HeightInch;
    Spinner mSpinner_Activity;
    Button mConfirmButton;

    private final double LB_TO_KG = 0.453592;
    private final double IN_TO_CM = 2.54;
    private final double MAINT_TO_LOSE1 = 0.788;
    private final double MAINT_TO_LOSE2 = 0.576;
    private final double MAINT_TO_GAIN1 = 1.212;
    private final double MAINT_TO_GAIN2 = 1.424;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadFirstLaunchPage();

        // If mFirstTimeLaunch doesn't exist, then we get a default true value
        // otherwise it will be false in SharedPrefs
       /* SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        if(settings.getBoolean(mFirstTimeLaunch, true)){

            editor.putBoolean(mFirstTimeLaunch, false);
            editor.commit();

            loadFirstLaunchPage();
        }
        // Render default main activity layout
        else{
            loadMainPage();
        }
        */
    }

    private void displayMissingFieldToast(){
        Toast.makeText(getApplicationContext(), "Please fill in the missing fields", Toast.LENGTH_SHORT).show();
    }

    private void loadFirstLaunchPage(){
        // Render first time launch layout
        setContentView(R.layout.oob_launch);

        mSpinner_Activity = (Spinner) findViewById(R.id.spinner_activity);
        mEditText_Age = (EditText) findViewById(R.id.editText_age);
        mCheckBoxMale = (CheckBox) findViewById(R.id.checkBox_male);
        mCheckBoxFemale = (CheckBox) findViewById(R.id.checkBox_female);
        mConfirmButton = (Button) findViewById(R.id.button_dataconfirm);
        mEditText_Weight = (EditText) findViewById(R.id.editText_weight);
        mEditText_HeightFeet = (EditText) findViewById(R.id.editText_feet);
        mEditText_HeightInch = (EditText) findViewById(R.id.editText_inches);

        // Render Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.activity_level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_Activity.setAdapter(adapter);
        mSpinner_Activity.setSelection(2);

        // Storing user data on confirm button click
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFormComplete = true;

                // Check for age value set
                if (mEditText_Age.getText().toString().equals("")) {
                    TextView textView_age = (TextView) findViewById(R.id.textView_age);
                    textView_age.setTextColor(Color.RED);
                    isFormComplete = false;
                }
                if (!(mCheckBoxMale.isChecked() ^ mCheckBoxFemale.isChecked())) {
                    TextView textView_gender = (TextView) findViewById(R.id.textView_gender);
                    textView_gender.setTextColor(Color.RED);
                    isFormComplete = false;
                }

                if (mEditText_Weight.getText().toString().equals("")) {
                    TextView textView_weight = (TextView) findViewById(R.id.textView_weight);
                    textView_weight.setTextColor(Color.RED);
                    isFormComplete = false;
                }

                if (mEditText_HeightFeet.getText().toString().equals("")) {
                    TextView textView_heightfeet = (TextView) findViewById(R.id.textView_height);
                    textView_heightfeet.setTextColor(Color.RED);
                    isFormComplete = false;
                }

                if (mEditText_HeightInch.getText().toString().equals("")) {
                    TextView textView_heightinch = (TextView) findViewById(R.id.textView_height);
                    textView_heightinch.setTextColor(Color.RED);
                    isFormComplete = false;
                }

                if (isFormComplete) {
                    double weightKG = (Integer.parseInt(mEditText_Weight.getText().toString()) * LB_TO_KG);
                    int heightIN = (Integer.parseInt(mEditText_HeightFeet.getText().toString())) * 12;
                    double heightCM = ((Integer.parseInt(mEditText_HeightInch.getText().toString())) + heightIN) * IN_TO_CM;
                    int age = Integer.parseInt(mEditText_Age.getText().toString());
                    int constant;
                    if(mCheckBoxMale.isChecked()){
                        constant = 5;
                    }
                    else{
                        constant = -161;
                    }

                    int BMR = (int) (10* weightKG + 6.25 * heightCM - 5 * age + constant);
                    int maintenanceCals = (int) (BMR * Double.parseDouble(getResources().getStringArray(R.array.activity_value)[mSpinner_Activity.getSelectedItemPosition()]));
                    loadMainPage(BMR, maintenanceCals);
                }
                else{
                    displayMissingFieldToast();
                }
            }
        });

        // Mutually Exclusive Gender Checkboxes
        mCheckBoxMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckBoxFemale.isChecked()){
                    mCheckBoxFemale.setChecked(false);
                    mCheckBoxMale.setChecked(true);
                }
            }
        });

        mCheckBoxFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckBoxMale.isChecked()){
                    mCheckBoxMale.setChecked(false);
                    mCheckBoxFemale.setChecked(true);
                }
            }
        });
    }

    private void loadMainPage(int BMR, int maintenanceCal){
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(mBMR, BMR);

        setContentView(R.layout.actviity_main);
        TextView textView_BMR = (TextView) findViewById(R.id.textView_oob);
        String caloriesText = String.format("BMR: " + BMR +
                "\n"+ "You need %d calories/day to maintain your weight" +
                "\nYou need %d calories/day to lose 1 lb per week" +
                "\nYou need %d calories/day to lose 2 lb per week" +
                "\nYou need %d calories/day to gain 1 lb per week" +
                "\nYou need %d calories/day to gain 2 lb per week",

                new Object[]{new Integer(maintenanceCal),
                            new Integer((int) (maintenanceCal*MAINT_TO_LOSE1)),
                            new Integer((int) (maintenanceCal*MAINT_TO_LOSE2)),
                            new Integer((int) (maintenanceCal*MAINT_TO_GAIN1)),
                            new Integer((int) (maintenanceCal*MAINT_TO_GAIN2))
                            }
        );
        textView_BMR.setText(caloriesText);
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
