package com.msy.security;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class yardimekrani extends AppCompatActivity {

    // Geri buttonu tanımlaması
    private Button geribtn;
    private Button mailbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yardimekrani);

        // Geri buttonu tanımlaması
        geribtn = (Button) findViewById(R.id.yardimgeribtn);
        mailbtn = (Button) findViewById(R.id.mailbtn);

        mailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","msy@themsy.co",null));
                mail.putExtra(Intent.EXTRA_SUBJECT, "Mısırlı Güvenlik Giriş Sorunu");
                startActivity(Intent.createChooser(mail, "Email uygulaması seçiniz:"));

            }
        });

        geribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login ekranına dönüş
                Intent loginegeri = new Intent(yardimekrani.this,loginekrani.class);
                startActivity(loginegeri);
                finish();
            }
        });

    }
}