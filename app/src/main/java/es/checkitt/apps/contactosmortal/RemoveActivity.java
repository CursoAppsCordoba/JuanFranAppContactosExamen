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
import android.widget.Toast;

public class RemoveActivity extends AppCompatActivity implements View.OnClickListener{
    TextView busqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);
        busqueda= (TextView)findViewById(R.id.search_name);
        Button removeS= (Button)findViewById(R.id.remove_search);

        removeS.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remove_search:
                    Intent inte= new Intent();
                    Contacto c= new Contacto(busqueda.getText().toString());
                    if (!TextUtils.isEmpty(busqueda.getText().toString())){
                    inte.putExtra("busqueda", c );
                    setResult(RESULT_OK, inte);
                    finish();
                    }else{
                    Toast.makeText(this, "No has añadido ningún nombre a buscar.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Ya estás borrando un contacto", Toast.LENGTH_SHORT).show();
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
