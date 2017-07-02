package com.endeavour.ojasva.thebasketballapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout input_layout_email_id,input_layout_password;
    private EditText input_email_id,input_password;
    private boolean isValidEmail,isValidPswd;
    private String name="";
    private String query="";
    private int fav_team_id=-1;
    private String pswd="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String ur="jdbc:aceql:http://"+"192.168.157.1"+":9090/ServerSqlManager";
        AceQLDBManager.initialize(ur, getString(R.string.username), getString(R.string.password));

        input_layout_email_id  = (TextInputLayout) findViewById(R.id.input_layout_email_id);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        input_email_id = (EditText) findViewById(R.id.input_email_id);
        input_password = (EditText) findViewById(R.id.input_password);

        input_email_id.addTextChangedListener(new MyTextWatcher(input_email_id));
        input_password.addTextChangedListener(new MyTextWatcher(input_password));
    }



    private boolean validateEmail() {
        String email = input_email_id.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            input_layout_email_id.setError("Enter valid email address");
            requestFocus(input_email_id);
            return false;
        } else {
            input_layout_email_id.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (input_password.getText().toString().trim().isEmpty()) {
            input_layout_password.setError("Enter valid password");
            requestFocus(input_password);
            return false;
        } else {
            input_layout_password.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email_id:
                    isValidEmail=validateEmail();
                    break;
                case R.id.input_password:
                    isValidPswd=validatePassword();
                    break;
            }
        }
    }


    public void openHome(View view)
    {
        if(isValidEmail && isValidPswd) {
            checkExistingUser();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid Email or Password",Toast.LENGTH_LONG).show();
        }
    }

    public void openCreateAccount(View view)
    {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    public void checkExistingUser()
    {
        String email = input_email_id.getText().toString().trim();
        pswd = input_password.getText().toString();
        query ="SELECT NAME, FAV_TEAM_ID,PASSWORD FROM USER1 WHERE EMAIL=" + "'" + email + "'";
        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background

    }
    private LoaderManager.LoaderCallbacks<List<Object>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Object>>()
    {
        @Override
    public Loader<List<Object>> onCreateLoader(int id, Bundle args) {
        return new CheckExistingUser(getApplicationContext(),query);
    }

    @Override
    public void onLoadFinished(Loader<List<Object>> loader, List<Object> ans) {

        String password = (String)ans.get(0);
        String name = (String)ans.get(1);
        int fav_team_id = (Integer)ans.get(2);

        if(password.equals(pswd))
        {
            Toast.makeText(getApplicationContext(),"Welcome " + name,Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Incorrect Email ID or Password",Toast.LENGTH_LONG).show();
        }
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public void onLoaderReset(Loader<List<Object>> loader) {


    }
};


}
