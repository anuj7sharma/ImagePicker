package com.imagepicker.ui.loginform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.widget.EditText;

import com.imagepicker.R;

/**
 * author by Anuj Sharma on 10/30/2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginViewPresenter.LoginView {
    private LoginViewPresenter presenter;
    private EditText etUserName, etPassword;
    private AppCompatButton btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        presenter = new LoginPresenterImpl(this, this);
    }

    @Override
    public EditText getUserName() {
        return etUserName;
    }

    @Override
    public EditText getPassword() {
        return etPassword;
    }

    @Override
    public AppCompatButton getLoginBtn() {
        return btnLogin;
    }

    @Override
    public String userName() {
        return etUserName.getText().toString().trim();
    }

    @Override
    public String password() {
        return etPassword.toString();
    }
}
