package es.checkitt.apps.contactosmortal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    TextView nombre, email, telefono;
    Contacto contacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        nombre= (TextView)findViewById(R.id.add_name);
        email= (TextView)findViewById(R.id.add_email);
        telefono= (TextView)findViewById(R.id.add_phone);
        Button add_c = (Button)findViewById(R.id.add_add);
        add_c.setOnClickListener(this);
        Button add_v = (Button)findViewById(R.id.volver_add);
        add_v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent inte = new Intent();
        switch (view.getId()) {

            case R.id.add_add:
                contacto = new Contacto();
                contacto.setNombre(nombre.getText().toString());
                contacto.setEmail(email.getText().toString());
                contacto.setTelefono(Integer.parseInt(telefono.getText().toString()));
                inte.putExtra("contacto", contacto);
                setResult(AddActivity.RESULT_OK, inte);
                finish();
                break;
            case R.id.volver_add:
                setResult(AddActivity.RESULT_CANCELED, inte);
                finish();
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
                Toast.makeText(this, "Ya estás en añadir, rellena todos los datos para añadir un contacto.", Toast.LENGTH_SHORT).show();
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
