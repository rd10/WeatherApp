package com.example.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;


import android.app.DialogFragment;

import com.example.stormy.R;

public class AlertDialogFragment extends DialogFragment  {

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String message = bundle.getString("message_key");

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.error_title)
        .setMessage(message)
        .setPositiveButton(R.string.error_button_ok_test, null);

        return builder.create();
    }

}
