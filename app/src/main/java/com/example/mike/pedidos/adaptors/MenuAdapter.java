package com.example.mike.pedidos.adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike.pedidos.R;
import com.example.mike.pedidos.items.MenuResItem;

import java.util.ArrayList;

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
        //holder.setData(listData.get(position));

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



    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre,precio,descripcion;
        ImageView foto;
        private ConstraintLayout parentLayout;

        public MenuViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textNombre);
            precio = itemView.findViewById(R.id.textPrecio);
            foto = itemView.findViewById(R.id.imageFoto);

            //nombre = itemView.findViewById(R.id.textNombre);

            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

        public void setData(MenuResItem item) {
            nombre.setText(item.getNombre());
            precio.setText(item.getPrecio().toString());

        }
    }
}
