package com.example.holamundo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextapellido, editTextemail, editTextcelTel, editTextdireccion, editTextreferencia;

    Button btnAviso, btnRegistrate;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progressDialog;

    @Override

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_registro);
        if(estaRegistradoElUsuario()) {
            enviarALaPrinciaplActivity();
            //Mostrar el toast de bienvenido con el nombre
        }else {
            btnAviso = (Button) findViewById(R.id.btnAviso);
            btnRegistrate = (Button) findViewById(R.id.btnRegistrate);


            editTextNombre = findViewById(R.id.editTextName);
            editTextapellido = findViewById(R.id.editTextsurname);
            editTextemail = findViewById(R.id.editTextemail);
            editTextcelTel = findViewById(R.id.editTextcelPhone);
            editTextdireccion = findViewById(R.id.editTextaddress);
            editTextreferencia = findViewById(R.id.editTextreference);


            ////programacion para abrir activity aviso

            btnAviso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//inicia metodo
                    // Intent intentAvisoPric = new Intent(RegistroActivity.this, AvisoPrivacidadActivity.class);
                    ///   startActivity(intentAvisoPric);
                    AlertDialog.Builder aviso = new AlertDialog.Builder(RegistroActivity.this);
                    aviso.setMessage("Nos comprometemos a mantener esa confianza.\nPara esto, comenzaremos por ayudarlo a comprender nuestras prácticas de privacidad." +
                            "En este aviso se describen los datos personales que recopilamos, cómo los usamos y compartimos, y sus opciones con respecto a ellos.\n" +
                            "Recomendamos que lea el aviso junto con nuestra descripción general de la privacidad, que destaca los puntos clave sobre nuestras prácticas en esta área." +
                            "Alcance  Este aviso se aplica a todos los usuarios de los servicios de Umami en cualquier parte del mundo, incluidos los que usan las apps, sitios web, funciones u otros servicios.\n" +
                            "Este aviso describe cómo Umami y sus afiliados recopilan y usan los datos personales.También se aplica a todos los usuarios de nuestras apps, sitios web, funciones y otros servicios en cualquier lugar del mundo, " +
                            "a menos que estén cubiertos por otra política, como el Aviso de privacidad de Umami.\n" +
                            "Se aplica específicamente a las siguientes partes:\n" +
                            "Usuarios: personas que piden viajes o que se transportan, incluidos quienes reciben los servicios programados por un tercero.\n" +
                            "Clientes: personas que piden o que reciben comida u otros productos y servicios.\n" +
                            "Socios repartidores: personas que prestan servicios de entrega a domicilio.\n" +
                            "Este aviso también rige otras recopilaciones de datos personales de Uber con relación a sus servicios. " +
                            "P. ej., podemos recopilar información de contacto de las personas que usan las cuentas de clientes de Uber para Empresas o de propietarios o empleados de restaurantes u otros comercios, " +
                            "datos personales de aquellos que inician solicitudes para ser socios conductores o socios repartidores (aunque no las completen) u otra información personal relacionada con nuestras funciones y tecnología de mapeo.\n" +
                            "Se hará referencia a todas las personas sujetas a este aviso como “usuarios”.\nAdemás, tenga en cuenta lo siguiente:\n" +
                            "Para los usuarios en México: Consulte aquí para obtener información sobre las prácticas de privacidad de Uber previstas en la Ley Federal de Protección de Datos Personales en Posesión de los Particulares de México\n" +
                            "Acepta los terminos y condiciones de este Aviso de Privacidad?")
                            .setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })


                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });

                    AlertDialog titulo = aviso.create();
                    titulo.setTitle("Aviso de Privacidad");
                    titulo.show();

                }//termina metodo
            });
        }
    }

    public void enviarALaPrinciaplActivity(){
        Intent intent;

        intent = new Intent(RegistroActivity.this, PrincipalActivity.class);

        startActivity(intent);
    }
    public boolean estaRegistradoElUsuario(){
        return true;
    }

    public void btnRegistrar_onClick(View view) {
        if (verificarCampos()) {
            registrarUsuario();
        }
        /// Descargar los datos para poder enviarlos

    }

    public boolean verificarCampos() {
        if (editTextNombre.getText().toString().length() == 0) {
            Toast.makeText(this, "Anote un nombre valido", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editTextapellido.getText().toString().length() == 0) {
            Toast.makeText(this, "Anote un apellido valido", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void enviarPrincipal() {


        Toast.makeText(this, "Datos Registrados", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegistroActivity.this, PrincipalActivity.class);
        startActivity(intent);
    }

    public void registrarUsuario() {

        String url = "https://www.vmartinez1984.somee.com/Api/Clients";

        btnAviso.setEnabled(false);
        String Nombre = editTextNombre.getText().toString();
        String Apellido = editTextapellido.getText().toString();
        String Email = editTextemail.getText().toString();
        String CelTel = editTextcelTel.getText().toString();
        String Direccion = editTextdireccion.getText().toString();
        String Referencia = editTextreferencia.getText().toString();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", 0);
            jsonObject.put("registrationDate", "2021-08-07T03:07:23.561Z");
            jsonObject.put("isActive", true);
            jsonObject.put("name", Nombre);
            jsonObject.put("surname", Apellido);
            jsonObject.put("email", Email);
            jsonObject.put("celPhone", CelTel);
            jsonObject.put("address", Direccion);
            jsonObject.put("reference", Referencia);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(this);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int usuarioId;

                            usuarioId = response.getInt("id");

                            ///progressDialog.dismiss();


                            Toast.makeText(getApplicationContext(), "Usuario Registrado " + usuarioId, Toast.LENGTH_LONG).show();

                            //Registrar en la base sqlite
                            //registrarUsuarioEnlaBaseSqlite(usuarioId,Nombre,Apellido);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       btnRegistrate.setEnabled(true);
                       Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

        Toast.makeText(this, "Datos Registrados", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegistroActivity.this, PrincipalActivity.class);
        startActivity(intent);

    }


}