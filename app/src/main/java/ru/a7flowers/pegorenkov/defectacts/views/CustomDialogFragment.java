package ru.a7flowers.pegorenkov.defectacts.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;

public class CustomDialogFragment extends AppCompatDialogFragment {

    private String title;
    private String errorMessage;

    public CustomDialogFragment() {
      //  super();
    }

    @SuppressLint("ValidFragment")
    public CustomDialogFragment(String title, String errorMessage) {
    //    super();
        this.title = title;
        this.errorMessage = errorMessage;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(errorMessage);
        return builder.create();
    }


}