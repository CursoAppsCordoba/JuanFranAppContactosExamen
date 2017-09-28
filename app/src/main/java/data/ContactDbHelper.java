package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.checkitt.apps.contactosmortal.Contacto;


public class ContactDbHelper extends SQLiteOpenHelper{
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + ContactoContract.ContactoEntry.TABLE_NAME + " ("
                + ContactoContract.ContactoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactoContract.ContactoEntry.NAME + " TEXT NOT NULL, "
                + ContactoContract.ContactoEntry.EMAIL + " TEXT, "
                + ContactoContract.ContactoEntry.PHONE + " INT, "
                + ContactoContract.ContactoEntry.IMAGE + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ContactoContract.ContactoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contactos.db";

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void close(){
        this.close();
    }
    private ContentValues contactoMapperValues(Contacto contacto){
        ContentValues valor = new ContentValues();
        valor.put(ContactoContract.ContactoEntry.NAME, contacto.getNombre());
        valor.put(ContactoContract.ContactoEntry.EMAIL, contacto.getEmail());
        valor.put(ContactoContract.ContactoEntry.PHONE, contacto.getTelefono());
        valor.put(ContactoContract.ContactoEntry.IMAGE, contacto.getImage());
        return valor;
    }

    public void addContacto(Contacto contacto){
        getWritableDatabase().insert(ContactoContract.ContactoEntry.TABLE_NAME,null,contactoMapperValues(contacto));
    }
    
    public void volcarArray(ArrayList<Contacto> agenda){
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+ ContactoContract.ContactoEntry.TABLE_NAME);
        onCreate(getWritableDatabase());
        for (Contacto contacto: agenda) {
            ContentValues valor = new ContentValues();
            valor.put(ContactoContract.ContactoEntry.NAME, contacto.getNombre());
            valor.put(ContactoContract.ContactoEntry.EMAIL, contacto.getEmail());
            valor.put(ContactoContract.ContactoEntry.PHONE, contacto.getTelefono());
            valor.put(ContactoContract.ContactoEntry.IMAGE, contacto.getImage());
            getWritableDatabase().insert(ContactoContract.ContactoEntry.TABLE_NAME,null,contactoMapperValues(contacto));
        }
        Collections.sort(agenda, new Contacto(true));
    }

    public Cursor getContacto(){
        String columnas[]={
                ContactoContract.ContactoEntry._ID,
                ContactoContract.ContactoEntry.NAME,
                ContactoContract.ContactoEntry.EMAIL,
                ContactoContract.ContactoEntry.PHONE,
                ContactoContract.ContactoEntry.IMAGE
        };
        Cursor c= this.getReadableDatabase().query(ContactoContract.ContactoEntry.TABLE_NAME,columnas,null,null,null,null,null);
        return c;
    }

    public List<Contacto> getAgendaContactos(){
        List<Contacto> list= new ArrayList<>();
        Cursor c= getContacto();
        while (c.moveToNext()){
            Contacto contacto= new Contacto();
            contacto.setNombre(c.getString(1));
            contacto.setEmail(c.getString(2));
            contacto.setTelefono(c.getInt(3));
            contacto.setImage(c.getString(4));
            list.add(contacto);
        }
        Collections.sort(list, new Contacto(true));
        return list;
    }
}
