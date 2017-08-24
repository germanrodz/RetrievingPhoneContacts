package com.blovvme.retrievingphonecontacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {





    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private static final String TAG = "Permision: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.d(TAG, "checkPermissions: Please allow this permission");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                Log.d(TAG, "checkPermissions: requesting permission");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else
        {
            Log.d(TAG, "checkPermissions: permission is already granted");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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

    public void readContacts(){
        Uri ContentURI = ContactsContract.Contacts.CONTENT_URI;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Cursor cursor = getContentResolver().query(ContentURI,null,null,null,null);

        int hasPhone = 0;
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String CONTACT_NAME = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                Log.d(TAG, "readContacts: ");
                //String CONTACT_NAME = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                hasPhone = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhone > 0) {
                    Uri Phone_Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    String number = ContactsContract.CommonDataKinds.Phone.NUMBER;

                    String [] projection = new String[]{number};
                    String  selection = DISPLAY_NAME + "=?";
                    String [] selectionArgs = new String[]{CONTACT_NAME};

                    Cursor phoneCursor = getContentResolver().query(Phone_Uri,projection,selection,selectionArgs,null);


                    while (phoneCursor.moveToNext()){
                        Log.d(TAG, "readContacts: " + phoneCursor.getString(phoneCursor.getColumnIndex(number)));
                    }

                }
            }
        }
    }

    public void readPhoneContacts(View view) {
        readContacts();
    }
}
