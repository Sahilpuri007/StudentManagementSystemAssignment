package com.system.studentmanagement.util;

import android.text.TextUtils;
import android.widget.EditText;

public class Validator {


    private static final String NAME_REGEX = "^[\\p{L} .'-]+$";
    private final static String ID_REGEX="^[1-9][0-9]*$";
    /*
     * Method isEmpty - to check if input is null
     * @param EditText editText - input from fields
     * @return boolean true or false accordingly
     */
    public boolean isEmpty(final EditText editText) {
        CharSequence str = editText.getText().toString();
        return TextUtils.isEmpty(str);
    }

    /*
     * Method isValidName - to check if input is a valid name
     * @param EditText editText - input from field
     * @return boolean true or false accordingly
     */
    public boolean isValidName(final EditText editText) {
        return !editText.getText().toString().trim().matches(NAME_REGEX);
    }

    public boolean isValidRollNo(final EditText editText) {
        return !editText.getText().toString().trim().matches(ID_REGEX);
    }

}
