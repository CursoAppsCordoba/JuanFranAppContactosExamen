package es.checkitt.apps.contactosmortal;

import android.graphics.drawable.Drawable;
import android.media.Image;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Apps on 21/09/2017.
 */

public class Contacto implements Serializable, Comparator<Contacto>{
    private String nombre;
    private String email;
    private Integer telefono;
    private Drawable image;

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Contacto(String nombre){
        this.nombre=nombre;
    }

    public Contacto(String nombre, String email, Integer telefono, Drawable image) {
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
        return 0;
    }
}
