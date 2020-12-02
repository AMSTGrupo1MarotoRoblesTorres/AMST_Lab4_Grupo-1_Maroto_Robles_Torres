package com.example.amst1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class nueva_temperatura extends AppCompatActivity {
    private RequestQueue mQueue;
    private String token = "";
    private String id_creado="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_temperatura);
        mQueue = Volley.newRequestQueue(this);



//        obtenemos una instancia de intent para obtener el token de la pantalla de inicio
        Intent login = getIntent();
        this.token = (String)login.getExtras().get("token");

    }

    public void irSensores(View v){
        TextView txt_new_temp = (TextView) findViewById(R.id.txt_new_temp);
        int txt_new_temp2 = Integer.parseInt(txt_new_temp.getText().toString());
        guardarTemperatura(txt_new_temp2);
    }

    public void guardarTemperatura(int nueva_temp){

        JSONObject parametros = new JSONObject();
        try{
            parametros.put("temperatura",nueva_temp);
            parametros.put("humedad",30);
            parametros.put("peso",510.2);

        }catch(Exception ignored){

        }

        String url_temp = "https://amst-labx.herokuapp.com/api/sensores";
        JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, url_temp, parametros, new Response.Listener<JSONObject>() {
            @Override public void onResponse(JSONObject response) {
                System.out.println(response);
                System.out.println("*******************************************");
                try {
                    id_creado = response.getString("id");

                    AlertDialog alertDialog = new AlertDialog.Builder(nueva_temperatura.this).create();
                    alertDialog.setTitle("Alerta");
                    alertDialog.setMessage("Temperatura creada con éxito");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent red_sensores = new Intent(getBaseContext(), red_sensores.class);
                            red_sensores.putExtra("token", token);
                            red_sensores.putExtra("id",id_creado);
                            startActivity(red_sensores);
                        }
                    });
                    alertDialog.show();





                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(nueva_temperatura.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Error al guardar temperatura");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }){
            @Override public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "JWT " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);










    }
}