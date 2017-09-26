package es.checkitt.apps.contactosmortal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Apps on 25/09/2017.
 */

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {
    private List<Contacto> items;
    private Context context;

    private static RecyclerViewClickListener mListener;

    public ContactoAdapter(List<Contacto> items, final Context context, RecyclerViewClickListener itemClickListener) {
        this.items = items;
        this.context = context;

        mListener = itemClickListener;
    }


    public static class ContactoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imagen;
        public TextView nombre;
        public TextView telefono;
        public TextView email;


        public ContactoViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            telefono = (TextView) v.findViewById(R.id.telefono);
            email= (TextView) v.findViewById(R.id.view_email);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
    public ContactoAdapter(List<Contacto> items) {
        this.items = items;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_cardlist, parent, false);
        return new ContactoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactoViewHolder viewHolder, int i) {

        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.telefono.setText(String.valueOf(items.get(i).getTelefono()));
        viewHolder.email.setText(items.get(i).getEmail());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}