package com.example.laspiedrasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class PaymentsActivity extends AppCompatActivity {

    private static final int SCAN_RESULT = 100;
    Button btnPagar;

    private TextView tvTarjeta;
    private TextView tvFecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        init();

        btnPagar = findViewById(R.id.btnPagar);

        // Mensaje de confirmación de compra

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(PaymentsActivity.this, "Compra realizada satifactoriamente", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void init(){
        tvTarjeta = findViewById(R.id.tvTarjeta);
        tvFecha = findViewById(R.id.tvFecha);
    }

    //Obtengo los datos de la tarjeta por el escaner

    public void escanearTarjeta(View view) {
        Intent intent = new Intent(this, CardIOActivity.class)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
        startActivityForResult(intent, SCAN_RESULT);
    }

    //Reviso si los datos capturados o ingresados por teclado son correctos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SCAN_RESULT){
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                tvTarjeta.setText(scanResult.getRedactedCardNumber());

                if (scanResult.isExpiryValid()){
                    String mes = String.valueOf(scanResult.expiryMonth);
                    String an = String.valueOf(scanResult.expiryYear);
                    tvFecha.setText(mes + "/" + an);
                }
            }
        }
    }

}

