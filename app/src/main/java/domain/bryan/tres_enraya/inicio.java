package domain.bryan.tres_enraya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class inicio extends AppCompatActivity {

    private Button btn1Jugador;
    private Button btn2Jugadores;
    private EditText edNombre_O, edNombre_X;
    private TextView tvTitulo;
    private View inicio;
    private RadioGroup optsRadio;
    private RadioButton rbClaro, rbOscuro;

    private int modo;
    private String nombre_O, nombre_X;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        nombre_O = edNombre_O.getText().toString();
        nombre_X = edNombre_X.getText().toString();

        outState.putString("nombre_O",nombre_O);
        outState.putString("nombre_X",nombre_X);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        nombre_X = savedInstanceState.getString("nombre_X");
        nombre_O = savedInstanceState.getString("nombre_O");

        edNombre_X.setText(nombre_X);
        edNombre_O.setText(nombre_O);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        iniciarDB();
        inicio = findViewById(R.id.actividad_inicio);
        GlobalVar.setBackgroundColor(inicio);
        tvTitulo = (TextView)findViewById(R.id.titulo);
        edNombre_O = (EditText)findViewById(R.id.nombre_O);
        edNombre_X = (EditText)findViewById(R.id.nombre_X);
        btn1Jugador = (Button) findViewById(R.id.btnUnJugador);
        btn2Jugadores = (Button) findViewById(R.id.btnDosJugadores);
        optsRadio = (RadioGroup)findViewById(R.id.grupoRadio);
        rbClaro = (RadioButton)findViewById(R.id.rbClaro);
        rbOscuro = (RadioButton)findViewById(R.id.rbOscuro);

        botones();
        radioButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case (R.id.menu_item_historial):
                Intent i = new Intent(getApplicationContext(),historial.class);
                startActivity(i);
                break;
            case (R.id.itemPreferencias):
                Intent in = new Intent(getApplicationContext(), misPreferencias.class);
                startActivity(in);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void iniciarDB()
    {
        GlobalVar.db = openOrCreateDatabase(GlobalVar.nombreBD, Context.MODE_PRIVATE,null);
        GlobalVar.db.execSQL("CREATE TABLE IF NOT EXISTS jugadas( " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "Fecha DATE NOT NULL ," +
                "Ganador_ficha VARCHAR(1) NOT NULL ," +
                "Ganador_nombre VARCHAR(30) NOT NULL " +
                ");");
        GlobalVar.db.close();
    }
    private void validacionCampos(EditText nombreX, EditText nombreO, int modo)
    {
        if ((nombreX.getText().toString().length() < 1) && (nombreO.getText().toString().length() < 1)) {
            nombreX.setError(getResources().getString(R.string.aviso_campos));
            nombreO.setError(getResources().getString(R.string.aviso_campos));
        } else if (nombreX.getText().toString().length() < 1) {
            nombreX.setError(getResources().getString(R.string.aviso_campos));

        } else if (nombreO.getText().toString().length() < 1) {
            nombreO.setError(getResources().getString(R.string.aviso_campos));
        } else {
            if (nombreO.getText().toString().equals(nombreX.getText().toString()))
            {
                nombreO.setError(getResources().getString(R.string.aviso_nombres_iguales));
                nombreX.setError(getResources().getString(R.string.aviso_nombres_iguales));
            }
            iniciarJuego(modo);
        }
    }

    private void iniciarJuego(int modo)
    {
        this.modo = modo;
        MostrarJuego();
    }


    private void botones()
    {
        btn1Jugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validacionCampos(edNombre_X,edNombre_O,1);
            }
        });
        btn2Jugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            validacionCampos(edNombre_X,edNombre_O,2);
            }
        });

    }

    private void radioButtons()
    {
        optsRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                switch (i)
                {
                    case (R.id.rbOscuro):
                        inicio.setBackgroundColor(getResources().getColor(R.color.miAzulOscuro));
                        GlobalVar.color=0;
                        break;
                    case(R.id.rbClaro):
                        inicio.setBackgroundColor(getResources().getColor(R.color.miAzulClaro));
                        GlobalVar.color=1;
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void MostrarJuego()
    {
        nombre_O = edNombre_O.getText().toString();
        nombre_X = edNombre_X.getText().toString();

        Intent i = new Intent(getApplicationContext(), Juego.class);
        i.putExtra("nombre_O", nombre_O);
        i.putExtra("nombre_X", nombre_X);
        i.putExtra("modo",modo);
        startActivity(i);

    }

}
