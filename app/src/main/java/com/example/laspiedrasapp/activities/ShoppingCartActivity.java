package com.example.laspiedrasapp.activities;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import com.example.laspiedrasapp.R;
        import com.example.laspiedrasapp.models.WholesaleProductModel;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {
    ListView lvCarrito;
    TextView tvTotal;

    private ArrayList<WholesaleProductModel> listaCarrito;
    private ArrayAdapter<WholesaleProductModel> adaptador;
    private String userId;

    private FirebaseDatabase miBase;
    private DatabaseReference miReferencia;
    private FirebaseAuth mAuth; // Para poder obtener el id del usuario

    final int[] total = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        mAuth = FirebaseAuth.getInstance();// Inicializo el auth del usuario
        miReferencia = FirebaseDatabase.getInstance().getReference();

        lvCarrito = findViewById(R.id.lvCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        setTitle("Carrito de compras");

        userId = mAuth.getUid();
        listaCarrito = new ArrayList<>();
        adaptador = new ArrayAdapter<WholesaleProductModel>(ShoppingCartActivity.this, android.R.layout.simple_list_item_1, listaCarrito);

        lvCarrito.setAdapter(adaptador);

        // Borro el producto de la lista manteniendo presionado el producto

        lvCarrito.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Toast.makeText(this, "Elemento eliminado del carrito", Toast.LENGTH_SHORT).show();
            miReferencia.child("users").child(userId).child("shoppingCart").child(listaCarrito.get(i).getId()).removeValue();
            adaptador.notifyDataSetChanged();
            return true;
        });

        // Obtengo el id del cliente y lo conecto con el id del producto y la cantidad de productos seleccionados

        miReferencia.child("users").child(userId).child("shoppingCart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCarrito.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot item: dataSnapshot.getChildren()){
                        if(item.exists()){
                            String idProducto= item.getKey();
                            String cantidad = item.getValue().toString();
                            miReferencia.child("wholesale_products").child(idProducto).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    WholesaleProductModel wholesaleProductModel = dataSnapshot.getValue(WholesaleProductModel.class);
                                    total[0] = total[0] + Integer.parseInt(cantidad)*Integer.parseInt(wholesaleProductModel.getWholesalePrice());
                                    listaCarrito.add(wholesaleProductModel);
                                    adaptador.notifyDataSetChanged();
                                    tvTotal.setText("Total: $" + String.valueOf(total[0]));
//                                definirTotal();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                }else{
//                    definirTotal();
//                    adaptador.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Realizo el total de la compra

//    private void definirTotal() {
//        double total = 0;
//
//        for (Product miproducto: listaCarrito){
//            total = total + miproducto.precio * Integer.parseInt(cantidad);
//        }
//
//        tvTotal.setText("Total: $" + total);
//    }
//
    // Manda a la actividad de pago con tarjeta y borra el carrito

    public void BotonPagar(View view) {
        miReferencia.child("users").child(userId).child("shoppingCart").removeValue();
        Intent intent= new Intent (this, PaymentActivity.class);
        startActivity(intent);
    }

}
