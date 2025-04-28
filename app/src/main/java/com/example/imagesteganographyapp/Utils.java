package com.example.imagesteganographyapp;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import static com.example.imagesteganographyapp.HomeFragment.PICK_IMAGE_REQUEST;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Utils {



    static void hideKeyboard(Context context, EditText edt) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
        }
    }


}
