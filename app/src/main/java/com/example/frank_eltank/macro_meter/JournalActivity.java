package com.example.frank_eltank.macro_meter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

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

        //DEBUG BUTTON
        Button deleteButton = (Button) findViewById(R.id.deleteLog);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCalendar = Calendar.getInstance();
                String journalFileName = ""+mCalendar.get(Calendar.YEAR) +""+mCalendar.get(Calendar.MONTH)+""+mCalendar.get(Calendar.DATE);
                File journal = new File(mContext.getFilesDir(), journalFileName);
                journal.delete();
            }
        });

        loadJournal();
    }

    private void loadJournal(){
        JournalReadWriter reader = new JournalReadWriter(getApplicationContext());
        List<String> journalData = reader.readCurrentJournal();
        int journalIndex = 0;

        mJournal = (TableLayout) findViewById(R.id.journal_table);

        for(String s : journalData){
            Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
        }

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
                    // Generate the empty rows
                    if(journalData.isEmpty() || journalIndex > journalData.size()){
                        ((TextView) row.getChildAt(0)).setOnClickListener(getCellOnClickListener(i));
                    }
                    else{
                        for(int j=0; j<6; j++){
                            ((TextView) row.getChildAt(j)).setText(journalData.get(journalIndex));
                            journalIndex++;
                        }
                    }
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
    private View.OnClickListener getCellOnClickListener(final int row) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFoodDialog = new NewFoodDialog();
                newFoodDialog.show(mContext.getFragmentManager(), "newfood");
                editRow = row + 1;
            }
        };
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name, int quant, int cals, int fat, int carbs, int protein){
        // Populating the journal table
        TableRow row = (TableRow) mJournal.getChildAt(editRow);
        ((TextView) row.getChildAt(0)).setText(name);
        ((TextView) row.getChildAt(0)).setBackgroundResource(R.drawable.table_border);
        ((TextView) row.getChildAt(1)).setText("" + quant);
        ((TextView) row.getChildAt(2)).setText("" + cals*quant);
        ((TextView) row.getChildAt(3)).setText("" + fat*quant);
        ((TextView) row.getChildAt(4)).setText("" + carbs*quant);
        ((TextView) row.getChildAt(5)).setText("" + protein*quant);

        mJournal.requestLayout();

        // Write data to file
        List<String> entryData = new ArrayList<String>();
        entryData.add(name);
        entryData.add(""+quant);
        entryData.add(""+cals);
        entryData.add(""+fat);
        entryData.add(""+carbs);
        entryData.add(""+protein);
        JournalReadWriter writer = new JournalReadWriter(getApplicationContext());
        writer.writeJournal(entryData);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        TableRow row = (TableRow) mJournal.getChildAt(editRow);
        Spinner spinner = (Spinner) row.getChildAt(0);
        spinner.setSelection(0);
    }
}
