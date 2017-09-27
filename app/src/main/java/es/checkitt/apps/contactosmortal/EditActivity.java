package es.checkitt.apps.contactosmortal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<Contacto> agenda;
    private Integer i;

    private TextView nombre;
    private TextView email;
    private TextView telefono;
    private Button volver, editar;
    private Boolean bandera= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editactivity);
        nombre= (TextView)findViewById(R.id.edit_nombre);
        email=(TextView)findViewById(R.id.edit_email);
        telefono=(TextView)findViewById(R.id.edit_telefono);
        volver=(Button)findViewById(R.id.volver_edit);
        editar=(Button)findViewById(R.id.edit_edit);
        volver.setOnClickListener(this);
        editar.setOnClickListener(this);
        Intent inte=getIntent();
        agenda= new ArrayList<>();
        agenda= (ArrayList<Contacto>) inte.getSerializableExtra("contacto");
        i= 0;
        i= inte.getIntExtra("posicion", i);
        nombre.setText(agenda.get(i).getNombre());
        email.setText(agenda.get(i).getEmail());
        telefono.setText(agenda.get(i).getTelefono().toString());
    }

    @Override
    public void onClick(View view) {
        Intent inte= new Intent();
        switch (view.getId()){
            case R.id.volver_edit:
                setResult(AddActivity.RESULT_CANCELED,inte);
                finish();
                break;

            case R.id.edit_edit:
                if (!TextUtils.isEmpty(nombre.getText().toString())){
                    agenda.get(i).setNombre(nombre.getText().toString());
                    bandera=true;
                }
                if (!TextUtils.isEmpty(email.getText().toString())){
                    agenda.get(i).setEmail(email.getText().toString());
                    bandera=true;
                }
                if (!TextUtils.isEmpty(telefono.getText().toString())){
                    agenda.get(i).setTelefono(Integer.parseInt(telefono.getText().toString()));
                    bandera=true;
                }
                if (bandera){
                    setResult(AddActivity.RESULT_OK,inte);
                    inte.putExtra("array", agenda);
                    finish();
                }else{
                    setResult(AddActivity.RESULT_OK,inte);
                    finish();
                }
                break;
        }
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
                startActivityForResult(inte, 100);
                finish();
                break;

            case R.id.remove_menu:
                inte= new Intent(this, RemoveActivity.class);
                startActivityForResult(inte, 200);
                finish();
                break;

            case R.id.list_menu:
                inte= new Intent(this, MainActivity.class);
                startActivity(inte);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
