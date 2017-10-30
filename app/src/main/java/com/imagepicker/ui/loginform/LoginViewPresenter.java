package com.imagepicker.ui.loginform;

import android.support.v7.widget.AppCompatButton;
import android.widget.EditText;

/**
 * author by Anuj Sharma on 10/30/2017.
 */

public interface LoginViewPresenter {

    void onLoginClick();

    interface LoginView {
        EditText getUserName();

        EditText getPassword();

        AppCompatButton getLoginBtn();

        String userName();

        String password();
    }

}
