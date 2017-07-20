package com.example.gatech.ulg;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Bind;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String LOGIN_API = "https://unitedlab-171401.appspot.com/api/v1/auth/login/";
    private static final int REQUEST_SIGNUP = 0;
    private boolean LoginResult = false;


    //https://unitedlab-171401.appspot.com/AccountInfo/


    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        // TODO: Implement your own authentication logic here.
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(LOGIN_API, email, password);
        httpAsyncTask.execute(LOGIN_API);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (LoginResult == true)
                            onLoginSuccess();
                        else
                             onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 2000);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

                // Although we've created an account, we need the cookies.
                String signup_email = data.getStringArrayListExtra("AccountPassword").get(0);
                String signup_pwd = data.getStringArrayListExtra("AccountPassword").get(1);

                Log.d(TAG, signup_email);
                Log.d(TAG, signup_pwd);

                HttpAsyncTask signup_login = new HttpAsyncTask(LOGIN_API, signup_email, signup_pwd);
                signup_login.execute(LOGIN_API);

                Intent i = new Intent(LoginActivity.this, showEquipmentInfoActivity.class);
                startActivity(i);
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);


        Intent i = new Intent(LoginActivity.this, MainPageActivity.class);
        startActivity(i);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            _passwordText.setError("between 3 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String url, email, password;
        private HttpHandler httpHandler;

        public HttpAsyncTask(String url, String email, String pwd) {
            this.url = url;
            this.email = email;
            this.password = pwd;
        }

        @Override
        protected String doInBackground(String... urls) {

            // 3. build jsonObject
            JSONObject js = new JSONObject();
            try {
                js.accumulate("email", email);
                js.accumulate("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makePOSTServiceCall(url, js);

            return jsonStr;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result != null){
                LoginResult = true;
                // Set the httpHandler Class's static isLogged to true
                httpHandler.isLogged = true;
            }else {
                LoginResult = false;
                httpHandler.isLogged = false;
            }

        }
    }
}
