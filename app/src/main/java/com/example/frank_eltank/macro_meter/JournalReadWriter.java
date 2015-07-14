package com.example.frank_eltank.macro_meter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used by JournalActivity to read saved journal entries per day that
 * are saved in files.
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
public class JournalReadWriter {

    private Context mContext;
    private Calendar mCalendar;

    public JournalReadWriter(Context context){
        mContext = context;
    }

    public List<String> readCurrentJournal(){
        mCalendar = Calendar.getInstance();
        String journalFileName = ""+mCalendar.get(Calendar.YEAR) +""+mCalendar.get(Calendar.MONTH)+""+mCalendar.get(Calendar.DATE);

        List<String> foodData = new ArrayList<String>();

        File journal = new File(mContext.getFilesDir(), journalFileName);
        Toast.makeText(mContext, journal.getPath(), Toast.LENGTH_SHORT).show();
        // Check if the current date journal exists
        if(journal.exists()){
            try {
                FileReader reader = new FileReader(journal);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                if(line != null){
                    String[] data = line.split(",");
                    for(String s : data){
                        foodData.add(s);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Create an empty journal file
        else{
            try {
                journal.createNewFile();
                Toast.makeText(mContext, journal.getAbsolutePath()+" created", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return foodData;
    }

    public void writeJournal(List<String> data){
        mCalendar = Calendar.getInstance();
        String journalFileName = ""+mCalendar.get(Calendar.YEAR) +""+mCalendar.get(Calendar.MONTH)+""+mCalendar.get(Calendar.DATE);
        File journal = new File(mContext.getFilesDir(), journalFileName);
        Toast.makeText(mContext, journal.getPath(), Toast.LENGTH_SHORT).show();
        try {
            FileWriter writer = new FileWriter(journal);
            for(String s : data){
                writer.write(s+",");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
