package es.checkitt.apps.contactosmortal;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public final static int ALTA = 100;
    public final static int BAJA = 200;
    public final static int VOLVER = 300;
    ArrayList<Contacto> agenda;
    private ListView listview;
    private ContactoAdapter contactoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agenda = new ArrayList<>();
        agenda.add(new Contacto("pepe", "a@a.es", 65482));
        listview = (ListView) findViewById(R.id.listar_contactos_listview);
        Refrescar(agenda);
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
            Intent inte= new Intent(this, AddActivity.class);
            startActivityForResult(inte, ALTA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        listview.setAdapter(contactoAdapter);
        contactoAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent inte;

        switch (id){
            case R.id.add_menu:
                inte= new Intent(this, AddActivity.class);
                startActivityForResult(inte, ALTA);
                break;

            case R.id.remove_menu:
                inte= new Intent(this, RemoveActivity.class);
                startActivityForResult(inte, BAJA);
                break;

            case R.id.list_menu:
                Toast.makeText(this, "Ya estás viendo todos los contactos.", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode==ALTA) {
            if (data.hasExtra("contacto")) {

                agenda.add((Contacto) data.getSerializableExtra("contacto"));
                Refrescar(agenda);
                Toast.makeText(this, "Contacto añadido con exito", Toast.LENGTH_SHORT).show();

            }
        }
        if (requestCode==BAJA){
            if(data.hasExtra("contacto")) {
                Contacto e = (Contacto) data.getSerializableExtra("busqueda");
                for (Contacto contacto:agenda) {
                    if (contacto.getNombre().toLowerCase().equals(e.getNombre().toLowerCase())){
                        agenda.remove(contacto);
                        Refrescar(agenda);
                        Toast.makeText(this, "Contacto eliminado con exito", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "No se ha encontrado el contacto", Toast.LENGTH_SHORT).show();
                    }
                }
                
            }
            
        }
        if (requestCode==VOLVER){

                if (data.hasExtra("array")){
                    agenda = (ArrayList<Contacto>) data.getSerializableExtra("array");
                    Refrescar(agenda);
                    contactoAdapter.notifyDataSetChanged();
                }
        }
        if(requestCode==RESULT_CANCELED){
            Toast.makeText(this, "Cancelado con éxito", Toast.LENGTH_SHORT).show();
        }
    }
    public void Refrescar(ArrayList<Contacto> agenda){
        contactoAdapter= new ContactoAdapter(this, agenda);
        listview.setAdapter(contactoAdapter);
    }

    public void abridDialog(final ArrayList<Contacto> agenda, final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Qué desea hacer con "+ agenda.get(position).getNombre().toString() +"?")

        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                agenda.remove(agenda.get(position));
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
