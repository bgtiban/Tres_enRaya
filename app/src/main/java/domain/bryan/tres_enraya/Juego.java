package domain.bryan.tres_enraya;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Juego extends AppCompatActivity {

    private static final String COLOR_FONDO_BOTON = "#FCCF4D" ;
    private static final String COLOR_GANADOR_BOTON = "#EF255F";
    private static final String COLOR_LETRA_BOTON = "#FFFFFF";

    private int  modo;
    private TextView tvTurno;
    private GridLayout tablero;
    private TextView tvNombre_O, tvNombre_X;
    private TextView tvPuntos_O, tv_puntosX;
    private View juego;
    private Button btnSalir, btnVolver, btnJugarOtra;

    private ArrayList<Button> IDs;
    private ArrayList<Integer> botones_marcados_ID;
    private ArrayList<String> botones_marcados_PIEZA;

    private int contador_movimientos_X =0;
    private int contador_movimientos_O=0;
    private static int puntos_O = 0;
    private static  int puntos_X = 0;
    private String nombreX, nombreO;
    private String turno;
    private static boolean HAY_GANADOR;

    private static CountDownTimer cuenta_atras;



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        puntos_O = Integer.parseInt(tvPuntos_O.getText().toString());
        puntos_X = Integer.parseInt(tv_puntosX.getText().toString());
        turno = tvTurno.getText().toString();


        outState.putInt("modo", modo);
        outState.putIntegerArrayList("botones_marcados",botones_marcados_ID);
        outState.putStringArrayList("botones_marcados_pieza",botones_marcados_PIEZA);
        outState.putInt("contador_movimientos_X",contador_movimientos_X);
        outState.putInt("contador_movimientos_O",contador_movimientos_O);
        outState.putInt("puntos_O",puntos_O);
        outState.putInt("puntos_X",puntos_X);
        outState.putString("turno",turno);
        outState.putBoolean("hay_ganador",HAY_GANADOR);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        modo = savedInstanceState.getInt("modo");
        botones_marcados_ID = savedInstanceState.getIntegerArrayList("botones_marcados");
        botones_marcados_PIEZA = savedInstanceState.getStringArrayList("botones_marcados_pieza");


        for (int i=0 ; i<botones_marcados_ID.size() ; i++){
            for(int x =0 ; x<9 ; x++)
            {

                if(botones_marcados_ID.get(i) == IDs.get(x).getId())
                {
                    IDs.get(x).setText(botones_marcados_PIEZA.get(i));
                    IDs.get(x).setEnabled(false);
                }
            }
        }
        contador_movimientos_X = savedInstanceState.getInt("contador_movimientos_X");
        contador_movimientos_O = savedInstanceState.getInt("contador_movimientos_O");
        puntos_O = savedInstanceState.getInt("puntos_O");
        puntos_X = savedInstanceState.getInt("puntos_X");
        turno = savedInstanceState.getString("turno");

        tv_puntosX.setText(String.valueOf(puntos_X));
        tvPuntos_O.setText(String.valueOf(puntos_O));
        tvTurno.setText(String.valueOf(turno));
        HAY_GANADOR = savedInstanceState.getBoolean("hay_ganador");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_juego);
        modo = getIntent().getExtras().getInt("modo");
        IDs = new ArrayList<Button>();
        botones_marcados_ID = new ArrayList<>();
        botones_marcados_PIEZA = new ArrayList<>();
        nombreO = getIntent().getExtras().getString("nombre_O");
        nombreX = getIntent().getExtras().getString("nombre_X");

        iniciarDB();
        tablero = (GridLayout) findViewById(R.id.tablero);
        tvTurno = (TextView)findViewById(R.id.turno) ;
        tvNombre_O = (TextView)findViewById(R.id.JuegoNombre_O);
        tvNombre_X = (TextView)findViewById(R.id.JuegoNombre_X);
        tvPuntos_O = (TextView)findViewById(R.id.puntos_O);
        tv_puntosX = (TextView)findViewById(R.id.puntos_X);
        juego = findViewById(R.id.juego);
        GlobalVar.setBackgroundColor(juego);
        btnJugarOtra =(Button) findViewById(R.id.btnJugarOtravez);
        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnVolver=(Button)findViewById(R.id.btnVolver);
        eventosBotones(btnVolver, btnJugarOtra,btnSalir);

        tvNombre_O.setText(nombreO );
        tvNombre_X.setText(nombreX );

        setTurno();

        creartableroBotones();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        puntos_X = puntos_O =0;
    }

    private void eventosBotones(Button btnVolver, Button btnJugarOtra, Button btnSalir)
    {
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnJugarOtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                limpiarSinespera();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Process.killProcess(Process.myPid());

            }

        });
    }


    private void setTurno()
    {
        if(modo==1)
        {
            tvTurno.setText(getResources().getString(R.string.turno_X));
        }else
        {
            turnoAleatorio();
        }
    }

    private void limpiarSinespera() {
        botones_marcados_ID = null;
        botones_marcados_PIEZA = null;

        botones_marcados_PIEZA = new ArrayList<>();
        botones_marcados_ID = new ArrayList<>();

        for (int i = 0; i < IDs.size(); i++) {
            IDs.get(i).setText("");
            IDs.get(i).setEnabled(true);
            IDs.get(i).setBackgroundColor(Color.parseColor(COLOR_FONDO_BOTON));
        }
        contador_movimientos_O = 0;
        contador_movimientos_X = 0;
        HAY_GANADOR = false;

        setTurno();
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

    private String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "vertical";
            case Surface.ROTATION_90:
            default:
                return "horizontal";

        }
    }

    private void crearBoton(int height, int width, int textSize , int id)
    {
        final Button boton = new Button(getApplicationContext());//porque final ?
        boton.setId(id);
        boton.setHeight(height);
        GridLayout.LayoutParams glp = new GridLayout.LayoutParams();
        glp.setMargins(10, 10, 0, 0);
        boton.setLayoutParams(glp);
        boton.setBackgroundColor(Color.parseColor(COLOR_FONDO_BOTON));
        boton.setTextColor(Color.parseColor(COLOR_LETRA_BOTON));
        boton.setWidth(width);
        boton.setTextSize(textSize);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventoBoton(boton);
            }
        });
        tablero.addView(boton);
        IDs.add(boton);
    }

    private void creartableroBotones()
    {
        if (getRotation(getApplicationContext()).equals("vertical"))
        {
            for(int i=0; i<9;i++) {

                crearBoton(DisplayMetrics.DENSITY_300,DisplayMetrics.DENSITY_260-33,100,i);
            }
        }else if(getRotation(getApplicationContext()).equals("horizontal")){
            for(int i=0; i<9;i++) {
                crearBoton(DisplayMetrics.DENSITY_260-120,DisplayMetrics.DENSITY_420-40,45,i);
            }
        }
    }

    private void marcar(final Button boton, String pieza)
    {
        boton.setText(pieza);

        botones_marcados_ID.add(boton.getId());
        botones_marcados_PIEZA.add(pieza);
        boton.setEnabled(false);
        if (pieza.equals("X"))
        {

            contador_movimientos_X++;
            if(contador_movimientos_X>=3)
            {
                comprobar_ganador("X");
            }
            tvTurno.setText(getResources().getString(R.string.turno_O));
        }
        if (pieza.equals("O")){
            contador_movimientos_O++;
            if (contador_movimientos_O >= 3) {
                comprobar_ganador("O");
            }
            tvTurno.setText(getResources().getString(R.string.turno_X));
        }



    }


    // la maquina siempre será ficha --   O
    private void eventoBoton(final Button boton)
    {

        if (modo==1) {
            //MODO 1 JUGADOR
            marcar(boton, "X");
            // Marcado aleatorio, No es inteligente [EN FUTURAS VERSIONES AQUÍ METER MOVIMIENTOS INTELIGENTES
            if(HAY_GANADOR!=true) {
                cuenta_atras = new CountDownTimer(100, 555) {
                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {

                        int posicion;
                        for (int i = 0; i < 9; i++) {
                            posicion = (int) (Math.random() * 9) + 0;
                            if (IDs.get(posicion).getText().toString().length() < 1) {

                                marcar(IDs.get(posicion),"O");
                                break;
                            }
                        }
                    }
                };
                cuenta_atras.start();
            }
        }
        if(modo==2){
            // MODO 2 JUGADORES
            if(tvTurno.getText() == getResources().getString(R.string.turno_O))
            {
                marcar(boton,"O");

            }else
            {
               marcar(boton,"X");
            }

        }


    }

    private void limpiarConEspera()
    {
            botones_marcados_ID = null;
            botones_marcados_PIEZA = null;

            botones_marcados_PIEZA= new ArrayList<>();
            botones_marcados_ID = new ArrayList<>();

        cuenta_atras = new CountDownTimer(2000,555) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                for(int i =0; i<IDs.size(); i++)
                {
                    IDs.get(i).setText("");
                    IDs.get(i).setBackgroundColor(Color.parseColor(COLOR_FONDO_BOTON));
                    IDs.get(i).setEnabled(true);
                }
                contador_movimientos_O=0;
                contador_movimientos_X=0;
                HAY_GANADOR=false;
                setTurno();
            }
        };
        cuenta_atras.start();

    }
    private void bloquearBotones()
    {
        if (HAY_GANADOR=true) {
            for (int i = 0; i < 9; i++) {
                IDs.get(i).setEnabled(false);
            }
        }else{
            for (int i = 0; i < 9; i++) {
                IDs.get(i).setEnabled(true);
            }
        }
    }

    private void mostrarGanador(String pieza)
    {
        HAY_GANADOR=true;
        bloquearBotones();

        //-----------------

       //--------------------
        if (pieza.equals("X"))
        {
            puntos_X++;
            tv_puntosX.setText(String.valueOf(puntos_X));
            grabarDatos("X",tvNombre_X.getText().toString());
        }else{
            puntos_O++;
            tvPuntos_O.setText(String.valueOf(puntos_O));
            grabarDatos("O",tvNombre_O.getText().toString());
        }
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.ganador)+ pieza,Toast.LENGTH_SHORT).show();
        limpiarConEspera();


    }

    private void grabarDatos(String piezaGanadora, String nombreGanador)
    {
        GlobalVar.db = openOrCreateDatabase(GlobalVar.nombreBD, Context.MODE_PRIVATE,null);
        GlobalVar.db.execSQL("INSERT INTO jugadas (Fecha,Ganador_ficha,Ganador_nombre)values( datetime() , '" + piezaGanadora + "' , '" + nombreGanador + "');");
        GlobalVar.db.close();
    }

    private void colorearBotones(int i, int x, int y)
    {
        IDs.get(i).setBackgroundColor(Color.parseColor(COLOR_GANADOR_BOTON));
        IDs.get(x).setBackgroundColor(Color.parseColor(COLOR_GANADOR_BOTON));
        IDs.get(y).setBackgroundColor(Color.parseColor(COLOR_GANADOR_BOTON));

    }

    private void comprobar_ganador(String pieza)
    {

        if(IDs.get(0).getText().toString().equals(pieza) && IDs.get(1).getText().toString().equals(pieza) && IDs.get(2).getText().toString().equals(pieza))
        {
            colorearBotones(0,1,2);
            mostrarGanador(pieza);
        }else if(IDs.get(2).getText().toString().equals(pieza) && IDs.get(5).getText().toString().equals(pieza) && IDs.get(8).getText().toString().equals(pieza))
        {
            colorearBotones(2,5,8);
            mostrarGanador(pieza);
        }else if(IDs.get(0).getText().toString().equals(pieza) && IDs.get(4).getText().toString().equals(pieza) && IDs.get(8).getText().toString().equals(pieza))
        {
            colorearBotones(0,4,8);
            mostrarGanador(pieza);
        }else if(IDs.get(3).getText().toString().equals(pieza) && IDs.get(4).getText().toString().equals(pieza) && IDs.get(5).getText().toString().equals(pieza))
        {
            colorearBotones(3,4,5);
            mostrarGanador(pieza);
        }else if(IDs.get(0).getText().toString().equals(pieza) && IDs.get(3).getText().toString().equals(pieza) && IDs.get(6).getText().toString().equals(pieza))
        {
            colorearBotones(0,3,6);
            mostrarGanador(pieza);
        }else if(IDs.get(6).getText().toString().equals(pieza) && IDs.get(4).getText().toString().equals(pieza) && IDs.get(2).getText().toString().equals(pieza))
        {
            colorearBotones(6,4,2);
            mostrarGanador(pieza);
        }else if(IDs.get(1).getText().toString().equals(pieza) && IDs.get(4).getText().toString().equals(pieza) && IDs.get(7).getText().toString().equals(pieza))
        {
            colorearBotones(1,4,7);
            mostrarGanador(pieza);
        }else if(IDs.get(6).getText().toString().equals(pieza) && IDs.get(7).getText().toString().equals(pieza) && IDs.get(8).getText().toString().equals(pieza))
        {
            colorearBotones(6,7,8);
            mostrarGanador(pieza);
        }else if((contador_movimientos_O==4 && contador_movimientos_X==5) || contador_movimientos_O==5 && contador_movimientos_X==4 ){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.empate),Toast.LENGTH_SHORT).show();
            limpiarConEspera();
        }

    }

    private void turnoAleatorio()
    {
        int x = (int)(Math.random() * 2) + 1;
        if(x==1){
            tvTurno.setText(getResources().getString(R.string.turno_O));
        }else{
            tvTurno.setText(getResources().getString(R.string.turno_X));
        }
    }



}
