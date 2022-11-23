package com.app.appgreco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppListadoGrecoActivity extends AppCompatActivity {
    private RecyclerView mRecyclerEventoRecomendado;
    private DatabaseReference mDatabase;
    private SharedPreferences prefs_categoria = null;
    private ProgressDialog mProgress;
    private GenericAdapter mAdapter;
    private ImageView fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_listado_greco);

        mProgress = new ProgressDialog(AppListadoGrecoActivity.this);
        mRecyclerEventoRecomendado = findViewById(R.id.mRecyclerEventoRecomendado);
        initFloating();
        initView();
    }

    private void initFloating() {
        fab = findViewById(R.id.fab_foro);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null){
                    //startActivity(new Intent(AppListadoGrecoActivity.this, SubirForoActivity.class));
                }else {
                    modalPermisos();
                    //Toast.makeText(getActivity(), "Para publicar en el foro inicia sesión, sino tienes una cuenta registrate gratis.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void modalPermisos() {
        final Dialog MyDialog = new Dialog(AppListadoGrecoActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.modal_need_grandt);
        MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button iniciar_sesion = MyDialog.findViewById(R.id.modal_iniciar_sesion);
        iniciar_sesion.setText("Agregar");
        Button registrarse = MyDialog.findViewById(R.id.modal_registrarse);
        registrarse.setText("Cancelar");
        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
                //startActivity(new Intent(AppListadoGrecoActivity.this, AccessRelatoLogin.class));
                // finish();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
                //startActivity(new Intent(AppListadoGrecoActivity.this, RegistroLegalicActivity.class));
                //finish();
            }
        });

        MyDialog.show();
    }


    private void initView() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("listadoGreco");

        mDatabase.keepSynced(true);
        mProgress.setTitle("Por favor, espere un momento.");
        mProgress.setMessage("Cargando...");
        mProgress.show();

        IModalExtra listener = new IModalExtra() {
            @Override
            public void modalIniciar(String nombre, String url, String uidUser) {
            }

            @Override
            public void modalIniciarDetail(String id) {
                //viewDetails(id);
                //startActivity(new Intent(getActivity(), DetalleActivity.class));
            }

            @Override
            public void modalAceptar(String id, Category category) {
            }

            @Override
            public void modalMensajito(String id) {
                modalPermisos();
            }

        };

        categoriaEventoRecomendado("recomendado", 1,listener);
        hideOneSecond(1000);
    }
    private void hideOneSecond(int segundo){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress.dismiss();
            }
        },segundo);
    }
    private void categoriaEventoRecomendado(String eventos,
                                            int orientacion, final IModalExtra listener) {
        final List<GenericModel> arrayLists = new ArrayList<>();
        mRecyclerEventoRecomendado.setHasFixedSize(true);
        LinearLayoutManager layoutManagermRecyclerEpisodiosPerdidos
                = new LinearLayoutManager(AppListadoGrecoActivity.this,orientacion, false);
        layoutManagermRecyclerEpisodiosPerdidos.setReverseLayout(true);
        layoutManagermRecyclerEpisodiosPerdidos.setStackFromEnd(true);
        mRecyclerEventoRecomendado.setLayoutManager(layoutManagermRecyclerEpisodiosPerdidos);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayLists.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String ids = eventSnapshot.getKey();
                    GenericModel model = eventSnapshot.getValue(GenericModel.class);
                    model.setId(ids);
                    arrayLists.add(model);
                }
                mAdapter = new GenericAdapter(AppListadoGrecoActivity.this, arrayLists, listener);
                mRecyclerEventoRecomendado.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void viewDetails(String post_key){

        prefs_categoria = getSharedPreferences("com.valdemar.spook.prefs_categoria", MODE_PRIVATE);
        prefs_categoria.edit().putString("blog_id", "evento,"+post_key).commit();
        //startActivity(new Intent(AppListadoGrecoActivity.this, DetalleActivity.class));

        // Intent intent = new Intent(getActivity(), Secundario.class);
        // startActivity(intent);

    }
}