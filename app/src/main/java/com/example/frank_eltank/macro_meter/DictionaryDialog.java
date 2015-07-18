package com.example.frank_eltank.macro_meter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Frank on 7/17/2015.
 */
public class DictionaryDialog extends DialogFragment {

    private boolean mHasSelected = false;
    private int mSelectedRow = 0;
    private TableRow mSelectedTableRow = null;

    public interface DictionaryDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String name,int quant, int cals, int fat, int carbs, int protein);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    DictionaryDialogListener mListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try{
            mListener = (DictionaryDialogListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Activity does not implement DictionaryDialog!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_dictionary, null);
        JournalReadWriter reader = new JournalReadWriter(getActivity());
        List<String> dictionaryRows  = reader.readDictionary();
        final TableLayout dictable = (TableLayout) v.findViewById(R.id.dialog_dictionary_table);
        final NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);

        for(int n=0;n<dictionaryRows.size();n++){
            Toast.makeText(getActivity(), ""+dictionaryRows.get(n), Toast.LENGTH_SHORT).show();
            final TableRow row = (TableRow) inflater.inflate(R.layout.dictionary_row, null);
            String[] entryValues = dictionaryRows.get(n).split(",");
            for(int i=0;i<entryValues.length;i++){
                ((TextView)row.getChildAt(i)).setText(entryValues[i]);
            }
            row.setOnClickListener(getTableRowClickListener(row));

            dictable.addView(row);
        }

        builder.setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mHasSelected && numberPicker.getValue() > 0) {
                            mListener.onDialogPositiveClick(DictionaryDialog.this, ((TextView) mSelectedTableRow.getChildAt(0)).getText().toString(), numberPicker.getValue(),
                                    Integer.parseInt(((TextView) mSelectedTableRow.getChildAt(1)).getText().toString()),
                                    Integer.parseInt(((TextView) mSelectedTableRow.getChildAt(2)).getText().toString()),
                                    Integer.parseInt(((TextView) mSelectedTableRow.getChildAt(3)).getText().toString()),
                                    Integer.parseInt(((TextView) mSelectedTableRow.getChildAt(4)).getText().toString()));
                        } else {
                            Toast.makeText(getActivity(), "Please select a food.", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DictionaryDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private View.OnClickListener getTableRowClickListener(final TableRow self){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                self.setBackgroundColor(Color.CYAN);
                mHasSelected = true;
                mSelectedTableRow = self;
            }
        };
    }
}


