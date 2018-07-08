package com.example.abdessalem.realtimetrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    private FirebaseAuth mAuth;
    private final static int LOGIN_PERMISSION=100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnSignIn);
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
/*                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false, true).build(),LOGIN_PERMISSION
               );*/

                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setAvailableProviders(getSelectedProviders())
                                .setIsSmartLockEnabled(false, true)
                                .build(),
                        LOGIN_PERMISSION);
            }


        });
    }

    public List<AuthUI.IdpConfig> getSelectedProviders() {
        List<IdpConfig> selectedProviders = new ArrayList<>();

        selectedProviders.add(new IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .build());

        return selectedProviders;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==LOGIN_PERMISSION)
        {
            StartNewActivity(resultCode);
        }
    }

    private void StartNewActivity(int resultCode) {
        if(resultCode==RESULT_OK)
        {
            Intent intent=new Intent(MainActivity.this,ListeOnline.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Login failed!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
