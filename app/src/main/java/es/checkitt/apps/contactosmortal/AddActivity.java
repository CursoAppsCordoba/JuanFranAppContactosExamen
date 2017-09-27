package es.checkitt.apps.contactosmortal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.MediaStore.AUTHORITY;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String APP_TAG = "PictureApp";
    private static String APP_DIRECTORY= "MyPictureApp/";
    private static String MEDIA_DIRECTORY= APP_DIRECTORY +"PictureApp";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private RelativeLayout mRelative;
    private ImageView mSetImage;
    private Button add_i;
    private String mPath;
    private Bitmap bitmap;
    private String ubicacion;


    private TextView nombre, email, telefono;
    private Contacto contacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mSetImage= (ImageView) findViewById(R.id.setPicture);
        nombre= (TextView)findViewById(R.id.add_name);
        email= (TextView)findViewById(R.id.add_email);
        telefono= (TextView)findViewById(R.id.add_phone);
        Button add_c = (Button)findViewById(R.id.add_add);
        add_c.setOnClickListener(this);
        Button add_v = (Button)findViewById(R.id.volver_add);
        add_v.setOnClickListener(this);
        add_i= (Button)findViewById(R.id.add_image_button);
        mRelative= (RelativeLayout)findViewById(R.id.mRelative);
        

        if (myRequestStoragePermission())
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                add_i.setEnabled(true);
            }else{
                Toast.makeText(this, "Necesitas android superior a Nougat para añadir una imagen de la cámara.", Toast.LENGTH_SHORT).show();
                add_i.setEnabled(false);}
        else
            add_i.setEnabled(false);
        

        add_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    openCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

   /* private void showOptions() {

        final CharSequence[] option = {"Tomar foto", *//*"Elegir de galería",*//* "Cancelar" };
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setTitle("¿Cómo deseas añadir una imagen?");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (option[i]== "Tomar foto"){
                    try {
                        openCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //No funciona bien lo de Elegir de galería.
                *//*}else if (option[i]== "Elegir de galería"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image*//**//*");
                    startActivityForResult(intent.createChooser(intent, "Seleccionar banco de imagenes"), SELECT_PICTURE);*//*
                }else{
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }*/

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


            Uri uri = FileProvider.getUriForFile(AddActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile());

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, PHOTO_CODE);
            }
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    Uri imageUri = Uri.parse(mPath);
                    File file = new File(imageUri.getPath());
                    try {
                        InputStream ims = new FileInputStream(file);
                        mSetImage.setImageBitmap(BitmapFactory.decodeStream(ims));
                    } catch (FileNotFoundException e) {
                        return;
                    }

                    MediaScannerConnection.scanFile(AddActivity.this,
                            new String[]{imageUri.getPath()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                }
                            });
                    break;
                //No muestra la imagen pero si guarda el path.
/*                case SELECT_PICTURE:

                    Uri path = data.getData();
                    mSetImage.setImageURI(path);
                    mPath= data.getData().getPath();
                    String prueba= mSetImage.getDrawable().toString();

                    Toast.makeText(this, ".", Toast.LENGTH_SHORT).show();
                    break;*/
            }
        }
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
    public void onClick(View view) {
        Intent inte = new Intent();
        switch (view.getId()) {

            case R.id.add_add:
                contacto = new Contacto();
                contacto.setNombre(nombre.getText().toString());
                contacto.setEmail(email.getText().toString());
                contacto.setTelefono(Integer.parseInt(telefono.getText().toString()));

                if (null!=mSetImage.getDrawable()){
                    contacto.setImage(mPath);
                }
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

    //https://github.com/neelk07/SampleContactAndroidApp/blob/master/src/com/example/SampleContactApp/ContactList.java

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode== MY_PERMISSIONS){
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                add_i.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder= new AlertDialog.Builder(AddActivity.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar la funcionalidad de añadir imagen necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Intent inte= new Intent();
        setResult(AddActivity.RESULT_CANCELED,inte);
        finish();
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
}
