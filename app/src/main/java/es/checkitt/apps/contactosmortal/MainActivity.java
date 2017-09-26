package es.checkitt.apps.contactosmortal;

import android.content.Intent;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    public final static int ALTA= 100;
    public final static int BAJA= 200;
    public final static int VOLVER= 300;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    ArrayList<Contacto> agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agenda= new ArrayList<>();
        if (agenda.size()!=0) {
            recycler = (RecyclerView) findViewById(R.id.reciclador);
            recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(lManager);
            adapter = new ContactoAdapter(agenda);
            recycler.setAdapter(adapter);
            recycler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position= (int) view.getTag();
                    Intent envio = new Intent(view.getContext(), EditActivity.class);
                    envio.putExtra("contacto", agenda);
                    envio.putExtra("posicion", (Integer) position);
                    startActivityForResult(envio, VOLVER);
                }
            });


        }else{
            Toast.makeText(this, "No hay ningún contacto para mostrar, puedes añadir uno desde aquí.", Toast.LENGTH_SHORT).show();
            Intent inte= new Intent(this, AddActivity.class);
            startActivityForResult(inte, ALTA);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recycler.setAdapter(adapter);
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
                Toast.makeText(this, "Contacto añadido con exito", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode==BAJA){
            if(data.hasExtra("contacto")) {
                String buscar =  data.getStringExtra("busqueda");
                for (Contacto contacto:agenda) {
                    if (contacto.getNombre().toLowerCase().equals(buscar.toLowerCase())){
                        agenda.remove(contacto);
                        Toast.makeText(this, "Contacto eliminado con exito", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "No se ha encontrado el contacto", Toast.LENGTH_SHORT).show();
                    }
                }
                
            }
            
        }
        if (requestCode==VOLVER){
            if (resultCode==400) {
                agenda = (ArrayList<Contacto>) data.getSerializableExtra("array");
            }

        }
        if(requestCode==RESULT_CANCELED){
            Toast.makeText(this, "Cancelado con éxito", Toast.LENGTH_SHORT).show();
        }
    }



}
