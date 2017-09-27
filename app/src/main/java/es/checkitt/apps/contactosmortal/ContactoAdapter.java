package es.checkitt.apps.contactosmortal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apps on 25/09/2017.
 */

public class ContactoAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<Contacto> agenda;

    public ContactoAdapter(Context context, ArrayList<Contacto> agenda) {
        this.context = context;
        this.agenda = agenda;
    }
    public ArrayList<Contacto> getData(){
        return agenda;
    }

    @Override
    public int getCount() {
        return agenda.size();
    }

    @Override
    public Object getItem(int i) {
        return agenda.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view= inflater.inflate(R.layout.list_item_view, viewGroup, false);

        }
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        TextView company = (TextView) view.findViewById(R.id.tv_company);
        ImageView image = (ImageView) view.findViewById(R.id.iv_avatar);

        Contacto contacto= this.agenda.get(i);
        name.setText(contacto.getNombre().toString());
        title.setText(contacto.getEmail().toString());
        company.setText(contacto.getTelefono().toString());
        if (contacto.getImage()!=null)

        Picasso.with(view.getContext()).load(contacto.getImage()).transform(new RoundedTransformation(100,0)).fit().into(image);


        return view;
    }


}