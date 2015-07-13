package com.example.frank_eltank.macro_meter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Frank on 7/1/2015.
 */
public class JournalActivity extends Activity implements NewFoodDialog.NewFoodDialogListener{

    private Activity mContext;
    private TableLayout mJournal;
    private int editRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.journal_activity);

        // Button to check the calories summary
        Button checkSummaryButton = (Button) findViewById(R.id.button_checkSummary);
        checkSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SummaryActivity.class);
                startActivity(intent);
            }
        });

        loadJournal();
    }

    private void loadJournal(){
        JournalReadWriter reader = new JournalReadWriter(getApplicationContext());
        List<String> journalData = reader.readCurrentJournal();

        mJournal = (TableLayout) findViewById(R.id.journal_table);
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
                    mJournal.addView(mealLabel);
                    break;
                // Total and Requirements calculations row
                case 28:
                    TableRow totalRow = (TableRow) (getLayoutInflater().inflate(R.layout.totals_row, null));
                    TextView totalLabel = (TextView) totalRow.getChildAt(0);
                    totalLabel.setText("Totals: ");
                    mJournal.addView(totalRow);
                    break;
                case 29:
                    TableRow reqRow = (TableRow) (getLayoutInflater().inflate(R.layout.totals_row, null));
                    TextView requireLabel = (TextView) reqRow.getChildAt(0);
                    requireLabel.setText("Requirements: ");
                    mJournal.addView(reqRow);
                    break;
                // Editable food entry row
                default:
                    TableRow row = (TableRow) (getLayoutInflater().inflate(R.layout.table_row, null));
                    Spinner foodEntry = (Spinner) row.getChildAt(0);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.journal_food_entry, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    foodEntry.setAdapter(adapter);
                    foodEntry.setOnItemSelectedListener(getOnItemSelectedListener(i));
                    mJournal.addView(row);
                    break;
            }
        }
    }

    /***
     * Listener for a row's  add food spinner selection
     * @param row: the row # of the selected spinner
     * @return: OnItemSelectedListener that will call a dialog to add a new food entry
     */
    private AdapterView.OnItemSelectedListener getOnItemSelectedListener(final int row){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    DialogFragment newFoodDialog = new NewFoodDialog();
                    newFoodDialog.show(mContext.getFragmentManager(), "newfood");
                    editRow = row+1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name, int cals, int fat, int carbs, int protein){
        Toast.makeText(getApplicationContext(), "Name: " + name + "\n"
                                                +"Calories: " + cals + "\n"
                                                +"Fats: " + fat + "\n"
                                                +"Carbs: " + carbs + "\n"
                                                +"Proteins: " + protein + "\n"
                                                +"Row:" + editRow, Toast.LENGTH_SHORT).show();
        TableRow row = (TableRow) mJournal.getChildAt(editRow);
        // Remove the spinner
        row.removeViewAt(0);
        // Add the food label
        TextView foodName = new TextView(getApplicationContext());
        foodName.setText(name);
        row.addView(foodName, 0);

        TextView calsCell = (TextView) row.getChildAt(2);
        calsCell.setText("" + cals);

        TextView fatCell = (TextView) row.getChildAt(3);
        fatCell.setText(""+fat);

        TextView carbsCell = (TextView) row.getChildAt(4);
        carbsCell.setText(""+carbs);

        TextView proteinCell = (TextView) row.getChildAt(5);
        proteinCell.setText(""+protein);

        mJournal.requestLayout();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        TableRow row = (TableRow) mJournal.getChildAt(editRow);
        Spinner spinner = (Spinner) row.getChildAt(0);
        spinner.setSelection(0);
    }
}
