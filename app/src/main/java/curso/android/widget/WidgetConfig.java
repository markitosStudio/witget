package curso.android.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import curso.android.widget.R;

/**
 * Created by markitos on 06/08/16.
 */
public class WidgetConfig extends Activity {

    private Button btnAceptar, btnCancelar;
    private EditText txtMensaje;
    private int widgetId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config);

        //Obtenemos el itnten que ha lanzado esta ventana
        // y recuperamos us parametro

        Intent intentOrigen = getIntent();
        Bundle params = intentOrigen.getExtras();

        //Obtenemos el id del widget que se esta configutado

        widgetId = params.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        //Establecemos el resultaod por defecto (si se pulsa el boton 'Atras'
        //del tebefono sera este el resultado devuelto)

        setResult(RESULT_CANCELED);

        btnAceptar=(Button) findViewById(R.id.btnAceptar);
        btnCancelar=(Button) findViewById(R.id.btnCancelar);
        txtMensaje=(EditText)findViewById(R.id.txtMensaje);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("msg_"+widgetId,txtMensaje.getText().toString());
                editor.commit();

                //Actualizamos el widget tras la configuracion

                AppWidgetManager appManager = AppWidgetManager.getInstance(WidgetConfig.this);
                MiWidget.actualizarWidget(WidgetConfig.this, appManager,widgetId);

                //Devolvemos como resultado: ACEPTAR (result_ok)
                Intent resultado = new Intent();
                resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);
                setResult(RESULT_OK,resultado);
                finish();
            }
        });

    }




}
