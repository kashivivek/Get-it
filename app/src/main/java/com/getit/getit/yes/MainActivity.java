    package com.getit.getit.yes;

    import android.Manifest;
    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;
    import android.location.LocationManager;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.v4.app.ActivityCompat;
    import android.support.v4.content.ContextCompat;
    import android.text.TextUtils;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.ProgressBar;
    import android.widget.Toast;

    import com.getit.getit.utils.SessionManager;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;


    public class MainActivity extends Activity {
        public static final String PREFS_NAME = "MyPrefs";
        private EditText inputEmail, inputPassword;
        private FirebaseAuth auth;
        private ProgressBar progressBar;
        private Button btnSignup, btnLogin;
        SessionManager session;
        private String username,password;
        private CheckBox saveLoginCheckBox;
        private SharedPreferences loginPreferences;
        private SharedPreferences.Editor loginPrefsEditor;
        private Boolean saveLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        buildAlertMessageNoGps();
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, MainHomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        final TestAdapter mDbHelper = new TestAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        session = new SessionManager(getApplicationContext());
        inputEmail = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.signup_button);
        btnLogin = (Button) findViewById(R.id.submit_button);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            inputEmail.setText(loginPreferences.getString("username", ""));
            inputPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (v == btnLogin) {
                                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);

                                                    username = inputEmail.getText().toString();
                                                    password = inputPassword.getText().toString();

                                                    if (saveLoginCheckBox.isChecked()) {
                                                        loginPrefsEditor.putBoolean("saveLogin", true);
                                                        loginPrefsEditor.putString("username", username);
                                                        loginPrefsEditor.putString("password", password);
                                                        loginPrefsEditor.commit();
                                                    } else {
                                                        loginPrefsEditor.clear();
                                                        loginPrefsEditor.commit();
                                                    }
                                                }
                                                else {
                                                    final String email = inputEmail.getText().toString();
                                                    final String password = inputPassword.getText().toString();
                                                }
                                                if (TextUtils.isEmpty(username)) {
                                                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                if (TextUtils.isEmpty(password)) {
                                                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                progressBar.setVisibility(View.VISIBLE);

                                                auth.signInWithEmailAndPassword(username, password)
                                                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                progressBar.setVisibility(View.GONE);
                                                                if (!task.isSuccessful()) {
                                                                    Toast.makeText(MainActivity.this, "Login Failed, Please use valid credentials!!", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    session.createLoginSession(username,password);
                                                                    Intent intent = new Intent(MainActivity.this, MainHomeActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                                }
                                            }
        );
        btnSignup.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                                                startActivity(intent);
                                            }
                                        }
        );
    }
        public boolean statusCheck()
        {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
                return true;
            }
            return false;
        }

        @Override
        public void onBackPressed() {
            finishAffinity();
        }
        private void buildAlertMessageNoGps() {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            126);

                }
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
            switch (requestCode) {
                case 126: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
