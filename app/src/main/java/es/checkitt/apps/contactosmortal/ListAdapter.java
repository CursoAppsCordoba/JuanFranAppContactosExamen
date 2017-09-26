package es.checkitt.apps.contactosmortal;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by bjfem on 26/09/2017.
 */

public class ListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Contacto> agenda;

    public ListAdapter(Context context, ArrayList<Contacto> agenda) {
        this.context=context;
        this.agenda=agenda;
    }


    @Override
    public int getCount() {
        return this.agenda.size();
    }

    @Override
    public Object getItem(int i) {
        return this.agenda.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View agenda_lista = convertView;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            agenda_lista = inflater.inflate(R.layout.list_item_view, parent, false);
        }



        TextView name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView company = (TextView) convertView.findViewById(R.id.tv_company);

        Contacto contacto= this.agenda.get(position);
        name.setText(contacto.getNombre().toString());
        title.setText(contacto.getEmail().toString());
        company.setText(contacto.getTelefono().toString());

        return agenda_lista;
    }
}
