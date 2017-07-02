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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.endeavour.ojasva.aceqlapi.AceQLDBManager;
import com.endeavour.ojasva.aceqlapi.BackendConnection;
import com.endeavour.ojasva.aceqlapi.OnGetResultSetListener;
import com.endeavour.ojasva.aceqlapi.OnPrepareStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

    private TextInputLayout input_layout_email_id,input_layout_name,input_layout_password;
    private EditText input_email_id,input_name,input_password,input_confirm_password,input_fav_team;
    private boolean isValidName,isValidPassword,isValidConfirmPassword,isValidEmailId;
    private boolean flag1,flag2;

    private int fav_team_id=-1;

    List<String> queries = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        String ur="jdbc:aceql:http://"+"192.168.157.1"+":9090/ServerSqlManager";
        AceQLDBManager.initialize(ur, getString(R.string.username), getString(R.string.password));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        input_layout_email_id = (TextInputLayout)findViewById(R.id.input_layout_email_id);
        input_layout_name = (TextInputLayout)findViewById(R.id.input_layout_name);
        input_layout_password = (TextInputLayout)findViewById(R.id.input_layout_password);

        input_email_id = (EditText)findViewById(R.id.input_email_id);
        input_password = (EditText)findViewById(R.id.input_password);
        input_name = (EditText)findViewById(R.id.input_name);
        input_confirm_password  = (EditText)findViewById(R.id.input_confirm_password);
        input_fav_team  = (EditText) findViewById(R.id.input_fav_team);

        input_name.addTextChangedListener(new MyTextWatcher(input_name));
        input_password.addTextChangedListener(new MyTextWatcher(input_password));
        input_email_id.addTextChangedListener(new MyTextWatcher(input_email_id));
        input_fav_team.addTextChangedListener(new MyTextWatcher(input_fav_team));

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

    private boolean validateName() {
        if (input_name.getText().toString().trim().isEmpty()) {
            input_name.setError("Enter your full name");
            requestFocus(input_name);
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
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
                    isValidEmailId = validateEmail();
                    break;
                case R.id.input_password:
                    isValidPassword = validatePassword();
                    break;
                case R.id.input_name:
                    isValidName = validateName();
                    break;

            }
        }
    }

        public void accountCreated(View view)
        {
            isValidConfirmPassword  = input_confirm_password.getText().toString().equals(input_password.getText().toString());

            if(isValidEmailId && isValidPassword && isValidName && isValidConfirmPassword)
            {
                checkValidity();
                if(flag1 && flag2)
                {
                    insertAccountDetails();
                    Toast.makeText(getApplicationContext(),"Account Created",Toast.LENGTH_SHORT).show();
                    Log.i("xyz","account created");
                    onBackPressed();
                }
            }
            else if(!isValidName)
            {
                Toast.makeText(getApplicationContext(),"Enter valid full name",Toast.LENGTH_SHORT).show();
            }
            else if(!isValidEmailId)
            {
                Toast.makeText(getApplicationContext(),"Enter valid email address",Toast.LENGTH_SHORT).show();
            }
            else if(!isValidPassword)
            {
                Toast.makeText(getApplicationContext(),"Enter valid password",Toast.LENGTH_SHORT).show();
            }
            else if(!isValidConfirmPassword)
            {
                Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
            }

    }

    public void checkValidity() {
        queries.add("SELECT ID,NAME FROM TEAM");
        queries.add("SELECT EMAIL FROM USER1");
        getLoaderManager().initLoader(0, null, loaderCallbacks);      //loading in background
    }



    private LoaderManager.LoaderCallbacks<List<Object>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Object>>()
    {
        @Override
        public Loader<List<Object>> onCreateLoader(int id, Bundle args) {
            return new CheckDetailsValidity(getApplicationContext(),queries,input_fav_team.getText().toString(),input_email_id.getText().toString());
        }

        @Override
        public void onLoadFinished(Loader<List<Object>> loader, List<Object> ans) {

            flag1 = (boolean)ans.get(0);
            fav_team_id = (int)ans.get(1);
            flag2 = (boolean)ans.get(2);

            if(flag1==false)
            {
                Toast.makeText(getApplicationContext(),"Invalid Team Name",Toast.LENGTH_SHORT).show();
            }
            else if(flag2==false)
            {
                Toast.makeText(getApplicationContext(),"Email already exists",Toast.LENGTH_SHORT).show();
            }
            Log.i("xyz","im done");
            getLoaderManager().destroyLoader(0);

        }

        @Override
        public void onLoaderReset(Loader<List<Object>> loader) {


        }
    };



    public void insertAccountDetails()
    {
        String name = input_name.getText().toString();
        final String email = input_email_id.getText().toString();
        String password = input_password.getText().toString();

        Log.i("xyz","before insert query");
        final String insert_query="INSERT INTO USER1 VALUES (" + "'"+ name + "'" + "," + "'" + email + "'" + "," + fav_team_id + "," + "'" + password + "'" +")";
        Log.i("xyz","after insert query");

        final OnPrepareStatements onPreparedStatementsListener = new OnPrepareStatements() {
            @Override
            public PreparedStatement[] onGetPreparedStatementListener(BackendConnection remoteConnection) {

                try {

                    PreparedStatement[] preparedStatements = new PreparedStatement[1];

                    for (int i = 0; i < 1; i++) {
                        //CallableStatement
                        //Prepare it to an executable statement
                        PreparedStatement preparedStatement = remoteConnection.prepareStatement(insert_query);
                        preparedStatements[i] = preparedStatement;
                    }

                    return preparedStatements;
                } catch (SQLException e) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                    return null;
                }
            }
        };


        //This listener tells the database manager what to do when we receive the result of the query execution
        //We will be using this listener when the execute button is clicked
        final OnGetResultSetListener onGetResultSetListener = new OnGetResultSetListener() {
            @Override
            public void onGetResultSets(ResultSet[] resultSets, SQLException e) {

                if (e != null) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                } else if (resultSets.length > 0) {
                        Log.i("xyz","here");

                } else {
                    //This should never happen but if it does,
                    //log and display it
                    Log.e("Result", "Received no result sets from query");
                }
            }
        };

        AceQLDBManager.executePreparedStatements(onPreparedStatementsListener, onGetResultSetListener);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("xyz","end");

    }





}
