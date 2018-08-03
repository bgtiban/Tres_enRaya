package domain.bryan.tres_enraya;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class historial extends AppCompatActivity {

    private Button btnLimpiar;
    private boolean HAY_PARTIDAS;
    private View yo;

    private ArrayAdapter<String> adaptador;
    private List<String> lista ;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private ListView lvListaHistorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        lvListaHistorial = (ListView) findViewById(R.id.listaHistorial);
        btnLimpiar =(Button)findViewById(R.id.btnlimpiar_historial);
        yo = findViewById(R.id.actividad_historial);
        GlobalVar.setBackgroundColor(yo);


        lista =  new ArrayList<>();

        listar();
        eventoLimpiar(btnLimpiar);


    }


    private void eventoLimpiar(Button boton){

            boton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                        if (HAY_PARTIDAS==true)
                        {
                            GlobalVar.db = openOrCreateDatabase(GlobalVar.nombreBD, Context.MODE_PRIVATE,null);
                           Cursor c = GlobalVar.db.rawQuery("SELECT * FROM jugadas",null);
                           lista = new ArrayList<String>();
                            adaptador = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lista);
                            while(c.moveToNext())
                            {
                                GlobalVar.db.execSQL("DELETE FROM jugadas WHERE id = " + c.getInt(0));
                            }
                            lvListaHistorial.setAdapter(adaptador);
                            GlobalVar.db.close();
                        }else
                        {
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.sin_registros), Toast.LENGTH_SHORT).show();
                        }


                }
            });


    }


    private void listar()
    {
        GlobalVar.db = openOrCreateDatabase(GlobalVar.nombreBD, Context.MODE_PRIVATE,null);
        Cursor c = GlobalVar.db.rawQuery("SELECT * FROM jugadas", null);
        if (c.getCount() == 0) {
            Toast.makeText(getApplicationContext(), " " + String.valueOf(getResources().getString(R.string.sin_registros)), Toast.LENGTH_SHORT).show();
            HAY_PARTIDAS =false;
        } else {
            while (c.moveToNext()) {
                lista.add(c.getInt(0)+ " " + c.getString(1) + ", " + c.getString(2) + ", " + c.getString(3));
            }
            adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, lista);
            lvListaHistorial.setAdapter(adaptador);
            c.close();
            GlobalVar.db.close();
            HAY_PARTIDAS =true;
    }
    }
}
