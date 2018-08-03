package domain.bryan.tres_enraya;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Bryan Ti on 10/12/2017.
 */

public class GlobalVar {
    //1= claro y 0 = oscuro
    public static int color=1;
    public static SQLiteDatabase db;
    public static  final String nombreBD = "BD_BryanTiban_Historial";

    public static void setBackgroundColor(View view)
    {
        if (color==0){
            view.setBackgroundColor(view.getResources().getColor(R.color.miAzulOscuro));
        }else{
            view.setBackgroundColor(view.getResources().getColor(R.color.miAzulClaro));
        }
    }



}
