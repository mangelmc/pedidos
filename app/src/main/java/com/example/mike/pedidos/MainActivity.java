package com.example.mike.pedidos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
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
import com.example.mike.pedidos.utils.Methods;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    RecyclerView recyclerMenu;
    Button insertar,tomarFoto;
    EditText editNombre,editPrecio;
    ImageView imageFoto;
    ArrayList<MenuResItem> listData;

    private MenuAdapter mMyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Methods.validarPermisos(this,this);

        Intent intentRest = getIntent();
        if (intentRest.getExtras() != null){
            String idRestaurant = intentRest.getExtras().getString("idRestaurante");
        }

        loadComponents();

        /////getData(); uncomment for service load
        loadData();//delete for service



    }


    private void loadComponents() {
        editNombre = findViewById(R.id.editNombre);
        editPrecio = findViewById(R.id.editPrecio);
        imageFoto = findViewById(R.id.image);

        recyclerMenu = findViewById(R.id.recyclerMenu);

        insertar = findViewById(R.id.insertar);
        insertar.setOnClickListener(this);
        tomarFoto = findViewById(R.id.tomarFoto);
        tomarFoto.setOnClickListener(this);


        listData = new ArrayList<>();
    }

    public void getData() {
        //cargar datos de la bd
        listData.clear();
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
                        String id = item.getString("_id");
                        String idRestaurant = item.getString("restaurant");
                        String foto = "";

                        if(item.getString("foto")!= null){
                             foto = item.getString("foto");
                        }
                        Log.i("IMG",item.getString("foto"));


                        MenuResItem menu = new MenuResItem(idRestaurant,nombre, foto,precio,id);
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
        ////////delete for service load
        for (int i = 0; i<10;i++ ){
            Log.d("Load", "loadData: " + i);
            MenuResItem menu = new MenuResItem(
                    "sdiasdn342njnsd"+i,
                    "Nombre "+i,
                    "https://www.chiquipedia.com/imagenes/imagenes-gatos0"+i+"-498x260.jpg",
                    10.5,
                    "id"+i
            );
            listData.add(menu);

        }
        /////////

        recyclerMenu.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        MenuAdapter adapter = new MenuAdapter(this, listData);
        recyclerMenu.setAdapter(adapter);

    }

    private void sendData() {

        if (editPrecio.getText().toString().equals("") || editNombre.getText().toString().equals("")){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (path == null || path == ""){
            Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        File file = new File(path);
        try {
            params.put("img", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("restaurant","5bf705dc9825331b589ab82a");//idRestaurant
        params.put("nombre", editNombre.getText());
        params.put("precio", editPrecio.getText());

        client.post(Data.MENU_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String message = response.getString("message");
                    String id = response.getString("id");

                    if (message != null) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        path = "";
                        editNombre.getText().clear();
                        editPrecio.getText().clear();
                        getData();
                    } else {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_LONG).show();
                Log.d("message",responseString);
            }

        });

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
            Snackbar.make(v,"Message",Snackbar.LENGTH_LONG).show();
            cargarImagen();
        }
    }
    //DESDE AQUI VA LA PARTE DE LA FOTO
    final int COD_GALERIA=10;
    final int COD_CAMERA=20;
    String path;
    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(MainActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_GALERIA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }
    private void tomarFotografia() {

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Methods.FileAndPath fileAndPath= Methods.createFile(path);
        File file = fileAndPath.getFile();
        path = fileAndPath.getPath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri fileuri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
            //BuildConfig.APPLICATION_ID + ".provider"
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(camera, COD_CAMERA);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_GALERIA:
                    Uri imgPath=data.getData();
                    imageFoto.setImageURI(imgPath);
                    path = Methods.getRealPathFromURI(this,imgPath);
                    Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
                    break;
                case COD_CAMERA:
                    loadImageCamera();


                    break;
            }
        }
    }

    private void loadImageCamera() {
        Bitmap img = BitmapFactory.decodeFile(path);
        if(img != null) {
            imageFoto.setImageBitmap(img);

        }
    }


}
