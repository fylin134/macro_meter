package com.example.frank_eltank.macro_meter;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    public final static String DICTIONARY_FILE_NAME = "FoodDictionary";

    public JournalReadWriter(Context context){
        mContext = context;
    }

    /***
     * Reads the entire journal file
     * @return: a List<String> containing row data
     *      each String item is equivalent to data in a row -> row#, foodName, quantity
     *      the journal activity handles the parsing and loading of the read data
     */
    public List<String> readCurrentJournal(){
        mCalendar = Calendar.getInstance();
        String journalFileName = ""+mCalendar.get(Calendar.YEAR) +""+mCalendar.get(Calendar.MONTH)+""+mCalendar.get(Calendar.DATE);

        List<String> tableData = new ArrayList<String>();

        File journal = new File(mContext.getFilesDir(), journalFileName);
        // Check if the current date journal exists
        if(journal.exists()){
            try {
                FileReader reader = new FileReader(journal);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                while((line = bufferedReader.readLine()) != null){
                    tableData.add(line);
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
        return tableData;
    }

    /***
     * Writes the row data to file
     * @param row: the row#
     * @param name: name of the food
     * @param quant: number of servings
     */
    public void writeJournal(int row, String name, int quant, int cals, int fat, int carb, int protein){
        mCalendar = Calendar.getInstance();
        String journalFileName = ""+mCalendar.get(Calendar.YEAR) +""+mCalendar.get(Calendar.MONTH)+""+mCalendar.get(Calendar.DATE);
        File journal = new File(mContext.getFilesDir(), journalFileName);
        try {
            FileWriter writer = new FileWriter(journal, true);
            writer.write(""+row+","+name+","+quant+","+cals+","+fat+","+carb+","+protein+"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDictionary(String name, int cals, int fat, int carb, int protein){
        File journal = new File(mContext.getFilesDir(), DICTIONARY_FILE_NAME);
        try {
            FileWriter writer = new FileWriter(journal, true);
            writer.write(""+name+","+cals+","+fat+","+carb+","+protein+"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readDictionary(){
        List<String> tableData = new ArrayList<String>();

        File journal = new File(mContext.getFilesDir(), DICTIONARY_FILE_NAME);
        // Check if the current date journal exists
        if(journal.exists()){
            try {
                FileReader reader = new FileReader(journal);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                while((line = bufferedReader.readLine()) != null){
                    tableData.add(line);
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
        return tableData;
    }
}
