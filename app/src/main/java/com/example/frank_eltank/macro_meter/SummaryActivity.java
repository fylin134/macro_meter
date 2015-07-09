package com.example.frank_eltank.macro_meter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Frank on 7/1/2015.
 */
public class SummaryActivity extends Activity {

    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.summary_layout);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        int bmr = mSettings.getInt(OOB_Activity.USER_BMR, -999);
        int maintenanceCal = mSettings.getInt(OOB_Activity.USER_CAL_MAINT, -999);
        TextView textView_BMR = (TextView) findViewById(R.id.textView_calorie_summary);
        String caloriesText = String.format("BMR: " + bmr +
                        "\n"+ "You need %d calories/day to maintain your weight" +
                        "\nYou need %d calories/day to lose 1 lb per week" +
                        "\nYou need %d calories/day to lose 2 lb per week" +
                        "\nYou need %d calories/day to gain 1 lb per week" +
                        "\nYou need %d calories/day to gain 2 lb per week",

                new Object[]{new Integer(maintenanceCal),
                        new Integer((int) (maintenanceCal*OOB_Activity.MAINT_TO_LOSE1)),
                        new Integer((int) (maintenanceCal*OOB_Activity.MAINT_TO_LOSE2)),
                        new Integer((int) (maintenanceCal*OOB_Activity.MAINT_TO_GAIN1)),
                        new Integer((int) (maintenanceCal*OOB_Activity.MAINT_TO_GAIN2))
                }
        );
        textView_BMR.setText(caloriesText);

        Button confirm = (Button) findViewById(R.id.button_summaryConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMainPage();
            }
        });
    }

    private void loadMainPage(){
        Intent intent = new Intent(getBaseContext(), JournalActivity.class);
        startActivity(intent);
    }
}
