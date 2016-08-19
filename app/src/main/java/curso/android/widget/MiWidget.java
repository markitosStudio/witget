package curso.android.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.GregorianCalendar;



/**
 * Created by markitos on 06/08/16.
 */
public class MiWidget  extends AppWidgetProvider{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("curso.android.widget.action.ACTUALIZAR_WIDGET")){
            //Obtener el ID del widget a actualizar
            int widgetId= intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            //Obnetenos el widget manager de nuestro contexto
            AppWidgetManager widgetManager =AppWidgetManager.getInstance(context);

            //Actualizamos el widget
            if(widgetId!= AppWidgetManager.INVALID_APPWIDGET_ID){
                actualizarWidget(context,widgetManager,widgetId);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i=0;i<appWidgetIds.length;i++){
            //Id del widget actual
            int widgetId = appWidgetIds[i];

            //Actualizamos el widget actual
            actualizarWidget(context,appWidgetManager,widgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //modificado desde la web 

        //Eliminamos las preferencias lde widget  borrados
        for(int i=0; i<appWidgetIds.length;i++){
            int widgetId = appWidgetIds[i];

            editor.remove("msg_"+widgetId);
        }
        editor.commit();

        super.onDeleted(context, appWidgetIds);
    }

    public static void actualizarWidget(Context context, AppWidgetManager appWidgetManager, int widgetId){
        //Recuperamos el  mensaje personalizado para el widget actual
        SharedPreferences prefs = context.getSharedPreferences("WidgetPrefs",Context.MODE_PRIVATE);
        String mensaje = prefs.getString("msg_"+widgetId,"Hora Actual");

        //Obtenemos el listado de controlar el widget actusl
        RemoteViews controles = new RemoteViews(context.getPackageName(), R.layout.miwidget);

        //Asociamos los 'Eventos' al widget
        Intent intent = new Intent("curso.android.widget.action.ACTUALIZAR_WIDGET");
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,widgetId,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        controles.setOnClickPendingIntent(R.id.btnActualizar,pendingIntent);

        Intent intent2 = new Intent(context,MainActivity.class);

        PendingIntent pendingIntent2 = PendingIntent.getActivity(context,widgetId,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        controles.setOnClickPendingIntent(R.id.FreWidget,pendingIntent2);

        //Actualizamos el mensaje  en el control del widget
        controles.setTextViewText(R.id.lblMensaje,mensaje);

        //Obtenemos la hora actual
        Calendar calendario = new GregorianCalendar();
        String hora =calendario.getTime().toLocaleString();

        //Actualizamos la hora en el control del widget
        controles.setTextViewText(R.id.lblHora,hora);

        //Modificar al manager la actualizacion del widget
        appWidgetManager.updateAppWidget(widgetId,controles);

    }



}
