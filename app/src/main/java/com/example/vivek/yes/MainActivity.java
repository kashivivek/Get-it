    package com.example.vivek.yes;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.location.LocationManager;
    import android.os.Bundle;
    import android.preference.PreferenceManager;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.Toast;

    public class MainActivity extends Activity {
        public static final String PREFS_NAME = "MyPrefs";
        DBHelper db = new DBHelper(this);
        SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        statusCheck();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db.onUpgrade(db.getWritableDatabase(), 1, 2);
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = saved_values.edit();
        editor.putString("tablename", "validation");
        editor.putString("username", "vivek");
        editor.putString("password", "vivek123");
        editor.commit();
        db.generateTable();
        Button login_button= (Button) findViewById(R.id.submit_button);
        login_button.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                if (db.validateUser() == true) {
                                                Intent intent = new Intent(MainActivity.this, MainHomeActivity.class);
                                                    startActivity(intent);
                                                } else
                                                    Toast.makeText(MainActivity.this, "Login Failed, Please use valid credentials picheswar!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }


        );

    }
        public void statusCheck()
        {
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();

            }


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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
