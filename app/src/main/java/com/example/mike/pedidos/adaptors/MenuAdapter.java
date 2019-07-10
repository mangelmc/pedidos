package com.example.mike.pedidos.adaptors;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mike.pedidos.R;
import com.example.mike.pedidos.items.MenuResItem;
import com.example.mike.pedidos.utils.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {


    private Context context;
    private ArrayList<MenuResItem> listData;



    public MenuAdapter(Context context, ArrayList<MenuResItem> listData) {
        this.context = context;
        this.listData = listData;

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu,parent,false);

        return new MenuViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, final int position) {
        holder.setData(listData.get(position),position);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,listData.get(position),Toast.LENGTH_SHORT).show();
                //context.startActivity(new Intent(context,Main2Activity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nombre,precio,descripcion;
        ImageView foto;
        TextView btnMore;
        String id,idRestaurant;
        int position;
        MenuResItem currentItem;
        private ConstraintLayout parentLayout;

        public MenuViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textDescripcion);
            precio = itemView.findViewById(R.id.textPrecio);
            foto = itemView.findViewById(R.id.imageFoto);
            btnMore = itemView.findViewById(R.id.btnMore);
            btnMore.setOnClickListener(this);

            //nombre = itemView.findViewById(R.id.textNombre);

            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

        public void setData(MenuResItem currentItem,int position) {
            nombre.setText(currentItem.getNombre());
            precio.setText(currentItem.getPrecio().toString());
            Glide.with(context).load(/*Data.URL_IMG + */currentItem.getFoto()).into(foto);//delete for service load
            id = currentItem.getId();

            this.position = position;
            this.currentItem = currentItem;

        }

        @Override
        public void onClick(View v) {
            //creating a popup menu
            PopupMenu popup = new PopupMenu(context, btnMore);
            //inflating menu from xml resource
            popup.inflate(R.menu.menu_options_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu1:
                            //handle menu1 click

                            removeItem(position);
                            return true;
                        case R.id.menu2:
                            //handle menu2 click
                            addItem(position,currentItem);
                            return true;
                        case R.id.menu3:
                            //handle menu3 click
                            return true;
                        default:
                            return false;
                    }
                }
            });
            //displaying the popup
            popup.show();

            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // Add the buttons
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    deleteMenu();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.setTitle("Esta seguro de eliminar el menu");

            AlertDialog dialog = builder.create();
            dialog.show();
            */




            //Toast.makeText(context,id,Toast.LENGTH_LONG).show();
        }

        private void deleteMenu() {
            AsyncHttpClient client = new AsyncHttpClient();

            client.delete(Data.MENU_URL + id,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        String message = response.getString("message");
                        if (message != null){
                            Toast.makeText(context,message,Toast.LENGTH_LONG).show();


                        }else   {
                            Toast.makeText(context,"Error al borrar",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
        public void removeItem(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.ic_food);
            builder.setMessage("Any message");
            builder.setTitle("Esta seguro de eliminar el menu ?");

            // Add the buttons
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    listData.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listData.size());

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });


            AlertDialog dialog = builder.create();
            dialog.show();


//		notifyDataSetChanged();
        }
        public void addItem(int position, MenuResItem currentObject) {
            listData.add(position, currentObject);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, listData.size());
//		notifyDataSetChanged();

        }
    }

}
