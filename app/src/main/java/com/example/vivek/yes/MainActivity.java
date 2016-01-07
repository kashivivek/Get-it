    package com.example.vivek.yes;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.location.LocationManager;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.example.vivek.utils.SessionManager;

    public class MainActivity extends Activity {
        public static final String PREFS_NAME = "MyPrefs";
        DBHelper db = new DBHelper(this);
        SharedPreferences sharedpreferences;
        SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        statusCheck();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db.onUpgrade(db.getWritableDatabase(), 1, 2);
        db.generateTable();
        session = new SessionManager(getApplicationContext());
        Button login_button= (Button) findViewById(R.id.submit_button);
        login_button.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                EditText text_uname = (EditText) findViewById(R.id.username);
                                                EditText text_pwd = (EditText) findViewById(R.id.password);
                                                String uname = text_uname.getText().toString();
                                                String pwd = text_pwd.getText().toString();
                                                if (uname.trim().length() > 0 && pwd.trim().length() > 0) {
                                                    if (db.validateUser(uname, pwd)) {
                                                        session.createLoginSession("uname", "pwd");
                                                        Intent intent = new Intent(MainActivity.this, MainHomeActivity.class);
                                                        startActivity(intent);
                                                    } else
                                                        Toast.makeText(MainActivity.this, "Login Failed, Please use valid credentials picheswar!!", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(MainActivity.this, "Picheswar!! Type something, You Son of a B**** ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
        );
    }
        public void statusCheck()
        {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }
        }

        @Override
        public void onBackPressed() {
            /*Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();*/
            finishAffinity();
        }
        private void buildAlertMessageNoGps() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog,  final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
