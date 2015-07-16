package com.example.frank_eltank.macro_meter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();

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

        initJournal();
    }

    /***
     * This function initializes the table layout and all the UI logic associated with it
     */
    private void initJournal(){
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
                // Totals Row
                case 28:
                    TableRow totalRow = (TableRow) (getLayoutInflater().inflate(R.layout.totals_row, null));
                    TextView totalLabel = (TextView) totalRow.getChildAt(0);
                    totalLabel.setText("Totals: ");
                    mJournal.addView(totalRow);
                    break;
                // Requirements Row
                case 29:
                    TableRow reqRow = (TableRow) (getLayoutInflater().inflate(R.layout.totals_row, null));
                    TextView requireLabel = (TextView) reqRow.getChildAt(0);
                    requireLabel.setText("Requirements: ");
                    mJournal.addView(reqRow);
                    break;
                // Editable empty food data row
                default:
                    TableRow row = (TableRow) (getLayoutInflater().inflate(R.layout.table_row, null));
                    // We do +1 because the table has a first child that is the table header
                    ((TextView) row.getChildAt(0)).setOnClickListener(getCellOnClickListener(i+1));
                    mJournal.addView(row);
                    break;
            }
        }
        loadJournal();
    }

    /***
     * This function actually reads from the saved journal file
     * and populates the table data
     */
    private void loadJournal(){
        JournalReadWriter reader = new JournalReadWriter(mContext);
        List<String> foodData = reader.readCurrentJournal();

        for(String entry : foodData){
            String[] entryArray = entry.split(",");
            int rowNum = Integer.parseInt(entryArray[0]);
            String name = entryArray[1];
            String quantity = entryArray[2];
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            Object[] entryData = settings.getStringSet(name, null).toArray();

            TableRow row = (TableRow) mJournal.getChildAt(rowNum);
            ((TextView) row.getChildAt(0)).setText(""+name);
            ((TextView) row.getChildAt(1)).setText(""+quantity);
            ((TextView) row.getChildAt(2)).setText(""+entryData[3]);
            ((TextView) row.getChildAt(3)).setText(""+entryData[2]);
            ((TextView) row.getChildAt(4)).setText(""+entryData[1]);
            ((TextView) row.getChildAt(5)).setText(""+entryData[0]);

        }
    }

    /***
     * Gets an onclicklistener that will pop up the newFoodDialog
     * @param row: the row# of the cell wishing to attach an onclicklistener
     * @return: the onclicklistener used by each journal entry row
     */
    private View.OnClickListener getCellOnClickListener(final int row) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFoodDialog = new NewFoodDialog();
                newFoodDialog.show(mContext.getFragmentManager(), "newfood");
                editRow = row;
            }
        };
    }

    /***
     * Callback when submit is clicked in the NewFoodDialog
     * @param dialog: The dialogfragment triggering this callback
     * @param name
     * @param quant
     * @param cals
     * @param fat
     * @param carbs
     * @param protein
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name, int quant, int cals, int fat, int carbs, int protein){
        // Populating the journal table
        TableRow row = (TableRow) mJournal.getChildAt(editRow);
        ((TextView) row.getChildAt(0)).setText(name);
        ((TextView) row.getChildAt(0)).setBackgroundResource(R.drawable.table_border);
        ((TextView) row.getChildAt(1)).setText("" + quant);
        ((TextView) row.getChildAt(2)).setText("" + cals * quant);
        ((TextView) row.getChildAt(3)).setText("" + fat * quant);
        ((TextView) row.getChildAt(4)).setText("" + carbs * quant);
        ((TextView) row.getChildAt(5)).setText("" + protein * quant);

        mJournal.requestLayout();


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        Set<String> foodData = new HashSet<String>();
        foodData.add(""+cals);
        foodData.add(""+fat);
        foodData.add(""+carbs);
        foodData.add(""+protein);
        editor.putStringSet(name, foodData);
        editor.commit();

        // Write data to file
        JournalReadWriter writer = new JournalReadWriter(getApplicationContext());
        writer.writeJournal(editRow, name, quant);
    }

    /***
     * Callback to handle a cancel click in the NewFoodDialog
     * Dismisses the dialog
     * @param dialog: The dialogfragment triggering this callback
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        TableRow row = (TableRow) mJournal.getChildAt(editRow);
        Spinner spinner = (Spinner) row.getChildAt(0);
        spinner.setSelection(0);
    }

    private void toastMe(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
