package com.example.laspiedrasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    TextView tvNombre;
    Button btnCarrito;
    ListView lvProductos;
    //Cliente micliente;

    FirebaseDatabase miBase;
    DatabaseReference miReferencia;

    ArrayList<Product> listaProductos;
    ArrayAdapter<Product> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        String id = getIntent().getExtras().getString("cliente");

        miBase = FirebaseDatabase.getInstance();
        miReferencia = miBase.getReference();

        tvNombre = findViewById(R.id.tvNombre);
        btnCarrito = findViewById(R.id.btnCarrito);
        lvProductos = findViewById(R.id.lvProductos);

        listaProductos = new ArrayList<>();

        adaptador = new ArrayAdapter<Product>(ProductActivity.this, android.R.layout.simple_list_item_1, listaProductos);

        lvProductos.setAdapter(adaptador);

        // Selecciono el producto

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Intent intencion = new Intent(ProductActivity.this, DetalleActivity.class);
                intencion.putExtra("producto", listaProductos.get(i).id);
                intencion.putExtra("cliente", micliente.id);
                startActivity(intencion);*/
            }
        });

        // Guardo la seleccion del producto

        miReferencia.child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    listaProductos.clear();
                    for (DataSnapshot dato: dataSnapshot.getChildren()){
                        Product unProducto = dato.getValue(Product.class);
                        listaProductos.add(unProducto);
                        adaptador.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Obtengo el nombre del cliente

        miReferencia.child("clientes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*micliente = dataSnapshot.getValue(Cliente.class);
                tvNombre.setText(micliente.nombre);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Me manda al carrito
        btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intencion = new Intent(ProductActivity.this, ShoppingCartActivity.class);
                intencion.putExtra("cliente", micliente.id);
                startActivity(intencion);*/
            }
        });
    }
}
