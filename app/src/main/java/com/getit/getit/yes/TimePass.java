package com.getit.getit.yes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.getit.getit.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TimePass extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        String currentUID = currentFirebaseUser.getUid();
        mRootRef.child("userinfo").child(currentUID).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        setTitle(user.getName());
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_pass);
    }

}