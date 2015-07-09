package com.example.frank_eltank.macro_meter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This class is used by JournalActivity to read saved journal entries per day that
 * are saved as Comma Separated Values (CSV) files.
 * Then it prepares the entry data to be loaded into JournalActivity
 *
 *
 * CSV Representation Schema:
 *
 * DATE1, FOOD1, CALORIES1, QUANTITY1, FAT1, CARBS1, PROTEIN1,
 * FOOD2, CALORIES2, etc...
 * ...
 * ...
 * DATE2, FOOD1, CALORIES1
 *
 *
 * Created by Frank on 7/8/2015.
 */
public class JournalReader {

    private Context mContext;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;

    public JournalReader(Context context){
        mContext = context;
    }

    public void readCurrentJournal(){
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSettings.edit();
    }
}
