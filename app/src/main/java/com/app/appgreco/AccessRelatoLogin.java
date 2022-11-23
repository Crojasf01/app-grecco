package com.app.appgreco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccessRelatoLogin extends AppCompatActivity {


    //progreso
    private ProgressDialog mProgress;

    //Editext
    private EditText mAccesso_login_email;

    //@BindView(R.id.accesso_login_password)
    private EditText mAccesso_login_password;

    //@BindView(R.id.accesso_login_btn)
    private FrameLayout mAccesso_login_btn;

    // @BindView(R.id.acceso_create_register)
    private TextView mAcceso_create_register;

    //@BindView(R.id.acceso_forget_password)
    private TextView mAcceso_forget_password;

    //login
    FirebaseAuth mAuth;

    private TextView splash_text_spook;

    private String TAG = "TAG";

    private SharedPreferences prefs_notificacion = null;

    private DatabaseReference mDatabaseSlides;

    private RelativeLayout btnToLogin;
    private TextView omitir;

    TextView username;
    CircleImageView userimage;
    FirebaseUser user;


    //Login Google
    RelativeLayout mAccessRelatoGoogle;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_relato_login);

        initView();

        mDatabaseSlides = FirebaseDatabase.getInstance().getReference().child("UsersGreco");

        btnToLogin = findViewById(R.id.btnToLogin);
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(AccessRelatoLogin.this, RegistroLegalicActivity.class));
            }
        });

        initTerminos();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void initTerminos() {
        TextView txt_terminos;
        txt_terminos = findViewById(R.id.txt_terminos);

        txt_terminos.setPaintFlags(txt_terminos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txt_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/policies_center/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }


    private void initView() {
        hideSoftKeyboard();
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        mAccesso_login_btn = findViewById(R.id.accesso_login_btn);

        mAcceso_create_register = findViewById(R.id.acceso_create_register);
        mAcceso_forget_password = findViewById(R.id.acceso_forget_password);


        mAccesso_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });


        mAcceso_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(AccessRelatoLogin.this, ForgetPasswordRelato.class));

            }
        });

        mAcceso_create_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(AccessRelatoLogin.this, RegistroLegalicActivity.class));

            }
        });

        checkaccess();

    }

    private void checkaccess() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Log.v("sesion", "Inciado");
            accesoPermitido();
        } else {
            Log.v("sesion", "Sin Iniciar");
        }
    }

    private void startLogin() {
        Log.v("accesoPermitido", "Ingresando");


        mAccesso_login_email = findViewById(R.id.accesso_login_email);
        mAccesso_login_password = findViewById(R.id.accesso_login_password);

        mAccesso_login_email.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        mAccesso_login_password.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        String display_email = mAccesso_login_email.getText().toString();
        String display_password = mAccesso_login_password.getText().toString();

        if (!validarEmail(display_email)) {
            showSnackBar("Email no válido.");
            mAccesso_login_email.setError("Email no válido.");
        } else if (display_password.length() < 6) {
            showSnackBar("La contraseña es muy corta.");
            // mAccesso_login_password.setError("Mínimo 6 dígitos.");
        } else {
            //showSnackBar("Accediendo");
            mProgress.setTitle("Iniciando Sessión...");
            mProgress.setMessage("Por espere mientras verificamos sus credenciales.");
            mProgress.show();
            loginUser(display_email, display_password);
        }
    }

    private boolean validarEmail(String display_email) {
        boolean booleamCheckEmail = Validaciones.validarCorreo(display_email);
        Log.v("checkEmail", "" + booleamCheckEmail);
        return booleamCheckEmail;
    }

    private void loginUser(String display_email, String display_password) {
        hideSoftKeyboard();
        mProgress.setMessage("Accediendo ...");
        mProgress.show();

        mAuth.signInWithEmailAndPassword(display_email, display_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkaccess();
                        }else{
                            Log.v("task_valdemar", "" + task);
                            mProgress.hide();
                            showSnackBar("Usuario y contraseña incorrecto.");
                            mProgress.dismiss();
                        }
                    }
                });


    }

    public void accesoPermitido() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Uri photoUrl = user.getPhotoUrl();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String email2 = user.getEmail();

        }

        mProgress.dismiss();

        /*Intent i = new Intent(AccessRelatoLogin.this, AppListadoGrecoActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();*/

        Log.v("accesoPermitido", "Ingresando");
        prefs_notificacion = getSharedPreferences("com.valdemar.spook.notificacion", MODE_PRIVATE);

        prefs_notificacion.edit().putBoolean("prefs_notificacion", true).commit();

    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.linearAccesso), msg, Snackbar.LENGTH_LONG)
                .show();
    }
    //sirve para esconder el teclado
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

                mProgress.dismiss();
        }
    }
}