package com.example.hucha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hucha.BBDD.Modelo.Usuario;
import com.example.hucha.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.identity.Identity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    Context context;
    View actualView;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 123;
    private boolean showOneTapUI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        actualView = binding.getRoot();
        context=this;

        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(this);

        signInRequest = new BeginSignInRequest.Builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(false)
                                .build())
                .setAutoSelectEnabled(true)
                .build();

        binding.btnCrearCuenta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToPantallaCrearCuenta(v);
            }
        });

        binding.btnIniciarSesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Usuario usuarioObtenido = Auxiliar.getAppDataBaseInstance(context).usuarioDao().getUsuarioByEmail(binding.etEmailInicioSesion.getText().toString());

                            if(usuarioObtenido != null && usuarioObtenido.password.equals(Auxiliar.encriptarContrasenha(binding.etEmailInicioSesion.getText().toString(),binding.etPasswordInicioSesion.getText().toString())))
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("usuario", String.valueOf(usuarioObtenido.id));
                                        editor.apply();
                                        goToPantallaPrincipal(v);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context,"El usuario o la contraseña son incorrectos.",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }catch(Exception e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,"Ha ocurrido un error al iniciar sesión. Inténtelo más tarde.",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client)) // Asegúrate de usar el correcto
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.btnIniciarSesionGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                inicioSesionGoogle();
            }
        });

        SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
        String usuario = sharedPreferences.getString("usuario", null);

        if(usuario != null)
        {
            goToPantallaPrincipal(actualView);
        }

        checkAndRequestPermissions();
    }

    public void goToPantallaPrincipal(View v)
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToPantallaCrearCuenta(View v)
    {
        Intent intent = new Intent(LoginActivity.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            boolean shouldRequest = false;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    shouldRequest = true;
                    break;
                }
            }

            if (shouldRequest) {
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        }
    }

    private void inicioSesionGoogle() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(result.getPendingIntent().getIntentSender(),
                                REQ_ONE_TAP, null, 0, 0, 0);
                    } catch (Exception e) {
                        Log.e("OneTapSignIn", "Error lanzando One Tap", e);
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.e("OneTapSignIn", "No se pudo iniciar sesión", e);
                    Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();

                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken);
                } else {
                    Toast.makeText(this, "Error: No se recibió ID Token", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("OneTapSignIn", "Error en el resultado de One Tap", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        assert user != null;
                        editor.putString("usuario", String.valueOf(user.getUid()));
                        editor.apply();
                        goToPantallaPrincipal(actualView);
                    } else {
                        Log.e("FirebaseAuth", "Error en autenticación", task.getException());
                    }
                });
    }
    

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            boolean allGranted = true;

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    allGranted = false;
                }
            }

            if (!allGranted) {
                Toast.makeText(this, "Faltan permisos para que la app funcione correctamente. Es posible que tengas errores.", Toast.LENGTH_LONG).show();
            }
        }
    }

}