package data;

import android.provider.BaseColumns;

/**
 * Created by bjfem on 27/09/2017.
 */

public class ContactoContract {

    public static abstract class ContactoEntry implements BaseColumns {

        public static final String TABLE_NAME ="contacto";
        public static final String ID = "id";
        public static final String NAME = "nombre";
        public static final String EMAIL = "email";
        public static final String PHONE = "telefono";
        public static final String IMAGE = "imagen";

    }


}
