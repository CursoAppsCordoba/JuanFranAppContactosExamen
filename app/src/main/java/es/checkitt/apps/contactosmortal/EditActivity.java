package es.checkitt.apps.contactosmortal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<Contacto> agenda;
    private Integer i;
    private static final String APP_TAG = "PictureApp";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private TextView nombre;
    private TextView email;
    private TextView telefono;
    private Button volver, editar;
    private Boolean bandera= false;
    private ImageView imagen;
    private Contacto contacto;
    private String mPath;
    private RelativeLayout mRelative;
    private String setimageUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editactivity);
        nombre= (TextView)findViewById(R.id.edit_nombre);
        email=(TextView)findViewById(R.id.edit_email);
        telefono=(TextView)findViewById(R.id.edit_telefono);
        imagen= (ImageView)findViewById(R.id.setPicture_edit);
        volver=(Button)findViewById(R.id.volver_edit);
        editar=(Button)findViewById(R.id.edit_edit);
        mRelative= (RelativeLayout)findViewById(R.id.edit_layout);
        volver.setOnClickListener(this);
        editar.setOnClickListener(this);
        Intent inte=getIntent();
        agenda= new ArrayList<>();
        agenda= (ArrayList<Contacto>) inte.getSerializableExtra("contacto");
        contacto = new Contacto();
        i= 0;
        i= inte.getIntExtra("posicion", i);
        contacto = agenda.get(i);
        nombre.setText(contacto.getNombre());
        email.setText(contacto.getEmail());
        telefono.setText(contacto.getTelefono().toString());
        if (contacto.getImage()!=null){
            Glide.with(this).load(contacto.getImage()).transform(new CircleTransform(EditActivity.this)).into(imagen);
        }
        if (myRequestStoragePermission())

            imagen.setEnabled(true);

        else
            imagen.setEnabled(false);


        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showOptions();

            }
        });

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
                    contacto.setNombre(nombre.getText().toString());
                    bandera=true;
                }
                if (!TextUtils.isEmpty(email.getText().toString())){
                    contacto.setEmail(email.getText().toString());
                    bandera=true;
                }
                if (!TextUtils.isEmpty(telefono.getText().toString())){
                    contacto.setTelefono(Integer.parseInt(telefono.getText().toString()));
                    bandera=true;
                }
                contacto.setImage(setimageUri);
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
    public void onBackPressed() {
        Intent inte= new Intent();
        setResult(AddActivity.RESULT_CANCELED,inte);
        finish();
    }

    private void showOptions() {

        final CharSequence[] option = {"Cámara", "Galería", "Cancelar" };
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setTitle("Modificar imagen desde...");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (option[i]== "Cámara"){
                    try {
                        openCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //No funciona bien lo de Elegir de galería.
                }else if (option[i]== "Galería"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Seleccionar banco de imagenes"), SELECT_PICTURE);
                }else{
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }
    private void openCamera() throws IOException {
        File file = new File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        boolean isCreated = file.exists();
        if (!isCreated){
            isCreated = file.mkdirs();
        }
        if (isCreated){
            Long timestamp= System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString()+".jpg";


            Uri uri = FileProvider.getUriForFile(EditActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile());

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, PHOTO_CODE);
            }
        }

    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mPath = "file:" + image.getPath();
        return image;
    }
    private boolean myRequestStoragePermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||(shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRelative , "Los permisos son necesarios para poder usar la aplicacion.", Snackbar.LENGTH_LONG).setAction(android.R.string.ok, new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA}, MY_PERMISSIONS);
                }
            }).show();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    Uri imageUri = Uri.parse(mPath);
                    setimageUri = imageUri.getPath();
                    File file = new File(imageUri.getPath());
                    Glide.with(this).load(setimageUri).transform(new CircleTransform(this)).into(imagen);
                    MediaScannerConnection.scanFile(EditActivity.this,
                            new String[]{imageUri.getPath()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                }
                            });
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    Glide.with(this).load(path).transform(new CircleTransform(this)).into(imagen);
                    setimageUri= path.toString();
                    break;
            }
        }
    }


}
