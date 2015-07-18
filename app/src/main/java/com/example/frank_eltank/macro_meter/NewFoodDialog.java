package com.example.frank_eltank.macro_meter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Frank_Lin2 on 7/13/2015.
 */
public class NewFoodDialog extends DialogFragment {

    public interface NewFoodDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String name, int quant, int cals, int fat, int carbs, int protein);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NewFoodDialogListener mListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try{
            mListener = (NewFoodDialogListener) activity;
        }
        catch (ClassCastException e){
            // The activity doesn't interface the interface
            throw new ClassCastException("Activity does not implement NewFoodDialog!");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_new_food, null);
        final TextView name = (TextView) v.findViewById(R.id.food_name);
        final TextView quantity = (TextView) v.findViewById(R.id.food_quantity);
        final TextView calories = (TextView) v.findViewById(R.id.food_calories);
        final TextView fats = (TextView) v.findViewById(R.id.food_fat);
        final TextView carbs = (TextView) v.findViewById(R.id.food_carbs);
        final TextView proteins = (TextView) v.findViewById(R.id.food_protein);

        builder.setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(NewFoodDialog.this, name.getText().toString(), Integer.parseInt(quantity.getText().toString()),
                                Integer.parseInt(calories.getText().toString()), Integer.parseInt(fats.getText().toString()),
                                Integer.parseInt(carbs.getText().toString()), Integer.parseInt(proteins.getText().toString()));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewFoodDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
