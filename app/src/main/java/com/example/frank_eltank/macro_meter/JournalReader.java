package com.example.frank_eltank.macro_meter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * This class is used by JournalActivity to read saved journal entries per day that
 * are saved as Comma Separated Values (CSV) files.
 * Then it prepares the entry data to be loaded into JournalActivity
 *
 *
 * CSV Representation Schema:
 *
 * FOOD1, CALORIES1, QUANTITY1, FAT1, CARBS1, PROTEIN1,
 * FOOD2, CALORIES2, etc...
 * ...
 * ...
 *
 * Each CSV file is saved as YEARMONTHDAY eg: 20150708
 *
 *
 * Created by Frank on 7/8/2015.
 */
public class JournalReader {

    private Context mContext;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private Calendar mCalendar;

    public JournalReader(Context context){
        mContext = context;
    }

    public void readCurrentJournal(){
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSettings.edit();

        mCalendar = Calendar.getInstance();
        String journalFileName = ""+mCalendar.get(Calendar.YEAR) +""+mCalendar.get(Calendar.MONTH)+""+mCalendar.get(Calendar.DATE);

        File journal = new File(mContext.getFilesDir(), journalFileName);
        // Check if the current date journal exists
        if(journal.exists()){
            try {
                FileReader reader = new FileReader(journal);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // Create an empty journal file
        else{
            try {
                journal.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeJournal(List<String> data, String journalFileName){
        File journal = new File(mContext.getFilesDir(), journalFileName);
        try {
            FileWriter writer = new FileWriter(journal);
            for(String s : data){
                writer.write(s);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
