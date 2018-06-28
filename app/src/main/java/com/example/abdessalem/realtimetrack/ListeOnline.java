package com.example.abdessalem.realtimetrack;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListeOnline extends AppCompatActivity {

    //Firebase
    DatabaseReference onlineRef,currentUserRef,counterRef;
    FirebaseRecyclerAdapter<User,ListOnlineViewHolder> adapter;

    //View
    RecyclerView ListeOnline;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_online);

        //init View
        ListeOnline = (RecyclerView)findViewById(R.id.ListeOnline);
        ListeOnline.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        ListeOnline.setLayoutManager(layoutManager);

        //Set toolbar and Logout / Join menu
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitle("realTime Tracker login");
        setSupportActionBar(toolbar);

        //Firebase
        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        //create new child named lastOnline
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");
        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());//Create new child in lastOnline with the key uid
        setupSystem();
    }

    private void setupSystem() {
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Boolean.class))
                {
                    currentUserRef.onDisconnect().removeValue();//delete The old values
                    //Add the Online user in list
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Online"));
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    User user = postSnapshot.getValue(User.class);
                    Log.d("LOG",""+user.getEmail()+" is "+user.getStatus());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
}
