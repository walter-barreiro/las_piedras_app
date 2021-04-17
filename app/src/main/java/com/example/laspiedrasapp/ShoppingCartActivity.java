package com.example.laspiedrasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ShoppingCartActivity extends AppCompatActivity {
    ListView lvCarrito;
    TextView tvTotal;

    ArrayList<Product> listaCarrito;
    ArrayAdapter<Product> adaptador;
    String idCliente;

    FirebaseDatabase miBase;
    DatabaseReference miReferencia;

    String cantidad ="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);

        lvCarrito = findViewById(R.id.lvCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        setTitle("Carrito de compras");

        idCliente = getIntent().getExtras().getString("cliente");
        listaCarrito = new ArrayList<>();
        adaptador = new ArrayAdapter<Product>(ShoppingCartActivity.this, android.R.layout.simple_list_item_1, listaCarrito);

        lvCarrito.setAdapter(adaptador);

        // Borro el producto de la lista manteniendo presionado el producto

        lvCarrito.setOnItemLongClickListener((adapterView, view, i, l) -> {
            miReferencia.child("clientes").child(idCliente).child("carrito").child(listaCarrito.get(i).id).removeValue();
            return true;
        });

        // Obtengo el id del cliente y lo conecto con el id del producto y la cantidad de productos seleccionados

        miBase = FirebaseDatabase.getInstance();
        miReferencia = miBase.getReference();

        miReferencia.child("clientes").child(idCliente).child("carrito").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCarrito.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot item: dataSnapshot.getChildren()){
                        String idProducto= item.getKey();
                        cantidad = item.child("cantidad").getValue().toString();
                        miReferencia.child("productos").child(idProducto).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Product miproducto = dataSnapshot.getValue(Product.class);
                                listaCarrito.add(miproducto);
                                adaptador.notifyDataSetChanged();
                                definirTotal();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }else{
                    definirTotal();
                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Realizo el total de la compra

    private void definirTotal() {
        double total = 0;

        for (Product miproducto: listaCarrito){
            total = total + miproducto.precio * Integer.parseInt(cantidad);
        }

        tvTotal.setText("Total: $" + total);
    }

    // Manda a la actividad de pago con tarjeta y borra el carrito

    public void BotonPagar(View view) {

        Intent intent= new Intent (this, PaymentsActivity.class);

        miBase = FirebaseDatabase.getInstance();
        miReferencia = miBase.getReference();
        miReferencia.child("clientes").child(idCliente).child("carrito").removeValue();


        startActivity(intent);
    }

}
