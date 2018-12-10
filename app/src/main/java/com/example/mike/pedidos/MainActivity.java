package com.example.mike.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mike.pedidos.adaptors.MenuAdapter;
import com.example.mike.pedidos.items.MenuResItem;
import com.example.mike.pedidos.utils.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerMenu;
    Button insertar,tomarFoto;
    EditText editNombre,editPrecio;
    ImageView imageFoto;
    ArrayList<MenuResItem> listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intentRest = getIntent();
        if (intentRest.getExtras() != null){
            String idRestaurant = intentRest.getExtras().getString("idRestaurante");
        }

        loadComponents();
        loadData();



    }

    private void sendData() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("restaurant","5bf705dc9825331b589ab82a");
        params.put("nombre", editNombre.getText());
        params.put("precio", editPrecio.getText());

        client.post("",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String message = response.getString("message");
                    String id = response.getString("id");

                    if (message != null) {

                        getData();
                    } else {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void getData() {
        //cargar datos de la bd
        AsyncHttpClient client = new AsyncHttpClient();


        client.get(Data.MENU_URL,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("result");
                    for (int i = 0; i<data.length();i++ ){
                        JSONObject item = data.getJSONObject(i);
                        Double precio = item.getDouble("precio");
                        String nombre = item.getString("nombre");

                        MenuResItem menu = new MenuResItem("",nombre, "","",precio);
                        listData.add(menu);
                    }

                    loadData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void loadData() {
        recyclerMenu.setLayoutManager(

                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        MenuAdapter adapter = new MenuAdapter(this, listData);
        recyclerMenu.setAdapter(adapter);

    }

    private void loadComponents() {
        editNombre = findViewById(R.id.editNombre);
        editPrecio = findViewById(R.id.editPrecio);
        imageFoto = findViewById(R.id.imageFoto);

        recyclerMenu = findViewById(R.id.recyclerMenu);

        insertar = findViewById(R.id.insertar);
        insertar.setOnClickListener(this);


        listData = new ArrayList<>();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.insertar){
            sendData();
        }
        if (v.getId() == R.id.tomarFoto){
            //sendData();
        }
    }
}
