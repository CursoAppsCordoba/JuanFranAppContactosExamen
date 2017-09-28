package es.checkitt.apps.contactosmortal;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import data.ContactDbHelper;


public class MainActivity extends AppCompatActivity{
    public final static int ALTA = 100;
    public final static int BAJA = 200;
    public final static int VOLVER = 300;
    ArrayList<Contacto> agenda;
    private ListView listview;
    private ContactoAdapter contactoAdapter;
    private SwipeRefreshLayout swipeContainer;
    private ContactDbHelper db;
    private Contacto contacto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contacto= new Contacto();
        db= new ContactDbHelper(this);
        agenda = (ArrayList<Contacto>) db.getAgendaContactos();
        listview = (ListView) findViewById(R.id.listar_contactos_listview);
        Refrescar(agenda);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(inte, ALTA);
            }
        });
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refrescar(agenda);
                swipeContainer.setRefreshing(false);

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        int permissionCheck = ContextCompat.checkSelfPermission(
                                view.getContext(), Manifest.permission.CALL_PHONE);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 225);
                        } else {
                            Log.i("Mensaje", "Se tiene permiso!");
                        }
                        return;
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + agenda.get(i).getTelefono().toString())));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                abridDialog(agenda, i);
                
                return true;
            }
        });
        if (agenda.size()==0){
            Toast.makeText(this, "No hay ningun contacto, añade uno primero", Toast.LENGTH_SHORT).show();
            Intent inte= new Intent(this, AddActivity.class);
            inte.putExtra("boolean", false);
            startActivityForResult(inte, ALTA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Refrescar(agenda);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode==ALTA) {
            if (data.hasExtra("contacto")) {
                agenda.add((Contacto) data.getSerializableExtra("contacto"));
                db.volcarArray(agenda);
                Refrescar(agenda);
                Toast.makeText(this, "Contacto añadido con exito", Toast.LENGTH_SHORT).show();

            }
        }
        if (requestCode==VOLVER){
                if (data.hasExtra("array")){
                    agenda = (ArrayList<Contacto>) data.getSerializableExtra("array");
                    db.volcarArray(agenda);
                    Refrescar(agenda);
                }
        }
        if(requestCode==RESULT_CANCELED){
            Toast.makeText(this, "Cancelado con éxito", Toast.LENGTH_SHORT).show();
        }
    }
    public void Refrescar(ArrayList<Contacto> agenda){
        agenda= (ArrayList<Contacto>) db.getAgendaContactos();
        Collections.sort(agenda, new Contacto(true));
        contactoAdapter= new ContactoAdapter(this, agenda);
        listview.setAdapter(contactoAdapter);
    }

    public void abridDialog(final ArrayList<Contacto> agenda, final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Qué desea hacer con "+ agenda.get(position).getNombre().toString() +"?")

        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                contacto = agenda.get(position);
                agenda.remove(contacto);
                db.volcarArray(agenda);
                Refrescar(agenda);
            }
        })
        .setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
        .setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent envio = new Intent(getApplicationContext(), EditActivity.class);
                        envio.putExtra("contacto", agenda);
                        envio.putExtra("posicion", (Integer) position);
                        startActivityForResult(envio, VOLVER);
                    }
                });
        builder.create().show();
    }

}
