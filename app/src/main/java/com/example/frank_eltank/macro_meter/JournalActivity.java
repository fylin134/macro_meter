package com.example.frank_eltank.macro_meter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Frank on 7/1/2015.
 */
public class JournalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.journal_activity);

        // Button to check the calories summary
        Button checkSummaryButton = (Button) findViewById(R.id.button_checkSummary);
        checkSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SummaryActivity.class);
                startActivity(intent);;
            }
        });

        TableLayout journal = (TableLayout) findViewById(R.id.journal_table);
        // Create the journal entry rows
        for(int i=0; i<30; i++){
            switch(i){
                // Labels for each meal
                case 0:
                case 7:
                case 14:
                case 21:
                    TextView mealLabel = new TextView(getApplicationContext());
                    mealLabel.setText("Meal " + ((i / 7) + 1));
                    mealLabel.setGravity(Gravity.CENTER);
                    mealLabel.setTextColor(Color.BLACK);
                    journal.addView(mealLabel);
                    break;
                // Total and Requirements calculations row
                case 28:
                    TableRow totalRow = (TableRow) (getLayoutInflater().inflate(R.layout.totals_row, null));
                    TextView totalLabel = (TextView) totalRow.getChildAt(0);
                    totalLabel.setText("Totals: ");
                    journal.addView(totalRow);
                    break;
                case 29:
                    TableRow reqRow = (TableRow) (getLayoutInflater().inflate(R.layout.totals_row, null));
                    TextView requireLabel = (TextView) reqRow.getChildAt(0);
                    requireLabel.setText("Totals: ");
                    journal.addView(reqRow);
                    break;
                // Editable food entry row
                default:
                    TableRow row = (TableRow) (getLayoutInflater().inflate(R.layout.table_row, null));
                    Spinner foodEntry = (Spinner) row.getChildAt(0);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.journal_food_entry, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    foodEntry.setAdapter(adapter);
                    journal.addView(row);
                    break;
            }
        }



    }

    private void loadJournal(){
    }


    private View.OnClickListener getRowOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Adding a new entry", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
