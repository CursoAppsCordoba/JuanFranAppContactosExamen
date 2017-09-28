package es.checkitt.apps.contactosmortal;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;

import java.io.Serializable;
import java.net.URI;
import java.util.Comparator;

/**
 * Created by Apps on 21/09/2017.
 */

public class Contacto implements Serializable, Comparator<Contacto>{
    private String nombre;
    private String email;
    private Integer telefono;
    private String image;
    private boolean asc;

    public Contacto(boolean asc){
        this.asc= asc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Contacto(String nombre){
        this.nombre=nombre;
    }

    public Contacto(String nombre, String email, Integer telefono, String image) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.image = image;
    }

    public Contacto(String nombre, String email, Integer telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    public Contacto() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    @Override
    public int compare(Contacto contacto, Contacto t1) {
        int retVal;
        if (asc){
            retVal= contacto.getNombre().compareTo(t1.getNombre());
        }else{
            retVal= t1.getNombre().compareTo(contacto.getNombre());
        }
        return retVal;

    }
}
