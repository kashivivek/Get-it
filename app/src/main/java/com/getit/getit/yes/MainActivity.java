    package com.getit.getit.yes;

    import android.Manifest;
    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;
    import android.location.LocationManager;
    import android.os.Bundle;
    import android.support.v4.app.ActivityCompat;
    import android.support.v4.content.ContextCompat;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.getit.getit.utils.SessionManager;

    public class MainActivity extends Activity {
        public static final String PREFS_NAME = "MyPrefs";
        DBHelper db = new DBHelper(this);
        //DatabaseCopier dbc = new DatabaseCopier(getApplication().getApplicationContext());
        SharedPreferences sharedpreferences;
        SessionManager session;
        Context urContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        buildAlertMessageNoGps();
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*try {
            dbc.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       final TestAdapter mDbHelper = new TestAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

       /* db.onUpgrade(db.getWritableDatabase(), 1, 2);
        db.generateTable();*/
        session = new SessionManager(getApplicationContext());
        Button login_button= (Button) findViewById(R.id.submit_button);
        login_button.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                EditText text_uname = (EditText) findViewById(R.id.username);
                                                EditText text_pwd = (EditText) findViewById(R.id.password);
                                                String uname = text_uname.getText().toString();
                                                System.out.print("@@MAin Activity"+uname);
                                                String pwd = text_pwd.getText().toString();
                                                System.out.print("@@MAin Activity"+pwd);
                                                String users= "users";
                                                if (uname.trim().length() > 0 && pwd.trim().length() > 0) {
                                                    if (mDbHelper.validateUser(uname, pwd,users)) {
                                                        System.out.println("entered validate user main activity");
                                                        session.createLoginSession("uname", "pwd");
                                                        Intent intent = new Intent(MainActivity.this, MainHomeActivity.class);
                                                       // mDbHelper.close();
                                                        startActivity(intent);
                                                    } else
                                                        Toast.makeText(MainActivity.this, "Login Failed, Please use valid credentials picheswar!!", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(MainActivity.this, "Picheswar!! Type something, You Son of a B**** ", Toast.LENGTH_SHORT).show();
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
            /*Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();*/
            finishAffinity();
        }
        private void buildAlertMessageNoGps() {
            /*final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            alert.show();*/
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            126);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
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
