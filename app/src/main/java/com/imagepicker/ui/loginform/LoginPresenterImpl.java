package com.imagepicker.ui.loginform;

/**
 * author by Anuj Sharma on 10/30/2017.
 */

public class LoginPresenterImpl implements LoginViewPresenter {
    LoginActivity loginActivity;
    LoginViewPresenter.LoginView loginView;

    public LoginPresenterImpl(LoginActivity activity, LoginViewPresenter.LoginView loginView) {
        this.loginActivity = activity;
        this.loginView = loginView;
    }

    @Override
    public void onLoginClick() {

    }
}
