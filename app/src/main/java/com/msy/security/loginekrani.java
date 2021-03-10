package com.msy.security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class loginekrani extends AppCompatActivity {

    // Giriş butonu
    private Button loginbtn;
    // Yardım sayfasına gidiş butonu butonu
    private Button yardimbtn;
    // String tanımlamarı
    public static String tc;
    public static String isim;
    public static String saha;
    public static String telefon;
    // Edit text tanımlamaları
    public static EditText tctext;
    public static EditText isimtext;
    private EditText mailtext;
    private EditText sifretext;
    private EditText sahatext;
    private EditText telefontext;
    // Firebase işlemleri
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    // Progress dialog tanımlaması
    private ProgressDialog logindialog;

    private boolean admin;

    private String date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginekrani);
        // İlknur'un satırı ; :) 15.09.2020

        // Buton tanımlamaları
        loginbtn = (Button) findViewById(R.id.loginbtn);
        yardimbtn = (Button) findViewById(R.id.yardimbtn);
        // EditText tanımlamarı
        tctext = (EditText) findViewById(R.id.tcalani);
        isimtext = (EditText) findViewById(R.id.isimalani);
        mailtext = (EditText) findViewById(R.id.mailalani);
        sifretext = (EditText) findViewById(R.id.sifrealani);
        sahatext = (EditText) findViewById(R.id.sahaalani);
        telefontext = (EditText) findViewById(R.id.telefonalani);
        // ProgressDialog tanımlaması
        logindialog = new ProgressDialog(this);
        // Firebase İşlemleri
        mAuth = FirebaseAuth.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

        sahatext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        isimtext.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        if (!internetkontrol()){
            AlertDialog.Builder builder2 = new AlertDialog.Builder(loginekrani.this);
            builder2.setMessage("Lütfen İnternet Bağlantınızı Kontrol Edin");
            builder2.setCancelable(false);
            builder2.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder2.show();
        }


        // Login Button İşlemleri
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisyap();
                loginbtn.setClickable(false);
            }
        });

        // Yardım ekranına gidiş
        yardimbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yrdmekrani();
            }
        });
    }

    // Yardım ekranı fonksiyonu
    private void yrdmekrani() {
        Intent yardimIntent = new Intent(loginekrani.this,yardimekrani.class);
        startActivity(yardimIntent);
        finish();

    }

    // Giriş Yap fonksiyonu
    private void girisyap() {

        // Tc edittextinden değer alıp stringe dönüştürüp tc değerine atama
        tc = tctext.getText().toString();
        // İsim edittextinden değer alıp stringe dönüştürüp isim değerine atama
        isim = isimtext.getText().toString();
        // Mail edittextinden değer alıp stringe dönüştürüp mail değerine atama
        String mail = mailtext.getText().toString();
        // Şifre edittextinden değer alıp stringe dönüştürüp sifre değerine atama
        String sifre = sifretext.getText().toString();
        // Saha edittextinden değer alıp stringe dönüştürüp saha değerine atama
        saha = sahatext.getText().toString();
        // Telefon edittextinden değer alıp stringe dönüştürüp saha değerine atama
        telefon = telefontext.getText().toString();


        if (!TextUtils.isEmpty(tc)&&!TextUtils.isEmpty(mail)&&!TextUtils.isEmpty(sifre)&&!TextUtils.isEmpty(saha)){

            // Giriş yapılıyor Progressdialogu
            logindialog.setTitle("Giriş Yapılıyor. Lütfen Bekleyiniz ...");
            logindialog.setCanceledOnTouchOutside(false);
            logindialog.show();


            // Tc değerini alıp sharedpreferences e kaydetme
            SharedPreferences sharedtc = this.getSharedPreferences("sharedtc",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedtc.edit();
            editor.putString("tc",tc);
            editor.commit();
            // İsim değerini alıp sharedpreferences e kaydetme
            SharedPreferences sharedisim = this.getSharedPreferences("sharedisim",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedisim.edit();
            editor2.putString("isim",isim);
            editor2.commit();
            // Saha kodunu alıp Sharedpreferences e kaydetme
            SharedPreferences sharedsahakodu = this.getSharedPreferences("sharedsahakodu",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedsahakodu.edit();
            editor1.putString("sahakodu",saha);
            editor1.commit();

            if (saha.equals("ADMİN"))
            {
                admin = true;
                SharedPreferences sharedadmin = this.getSharedPreferences("sharedadmin",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3 = sharedadmin.edit();
                editor3.putBoolean("sharedadmin",admin);
                editor3.commit();
            }
            // Kullanıcı girişi fonksiyonu
            loginuser(tc,isim,mail,sifre,saha,telefon);


        }


    }

    private void loginuser(final String tc, final String isim, final String mail, String sifre, final String saha, final String telefon) {
        final SharedPreferences sharedtom = this.getSharedPreferences("sharedtom",Context.MODE_PRIVATE);
        mAuth.signInWithEmailAndPassword(mail,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Başarılı ise
                if(task.isSuccessful()){
                    // Şu anki kullanıcının uid sini alma
                    String user_id = mAuth.getCurrentUser().getUid();
                    // Kullanıcılar altına tc den child oluşturma
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(isim).child("Kişisel Bilgiler");
                    // Tc childine değerleri
                    HashMap <String,String> usermap = new HashMap<>();
                    usermap.put("1 İsim Soyisim",isim);
                    usermap.put("2 Tc",tc);
                    usermap.put("3 Telefon No",telefon);
                    usermap.put("4 Mail",mail);
                    usermap.put("5 Saha",saha);
                    usermap.put("6 Kayıt Tarihi",date);





                    // Değerleri yerleştime başarılı ise
                    mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Sahalar").child(saha);
                                mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String tomkontrol = snapshot.child("Tom").getValue().toString();


                                        if (tomkontrol.equals("Evet")){

                                            SharedPreferences.Editor editor3 = sharedtom.edit();
                                            editor3.putBoolean("sharedtom",true);
                                            editor3.commit();

                                        }
                                        else {
                                            SharedPreferences.Editor editor3 = sharedtom.edit();
                                            editor3.putBoolean("sharedtom",false);
                                            editor3.commit();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                // Progressdialogu kapatma
                                logindialog.dismiss();
                                // MainActivity e git
                                Intent mainIntent = new Intent(loginekrani.this,giriscikis.class);
                                startActivity(mainIntent);
                                finish();

                            }
                        }
                    });


                }
                else {
                    Toast.makeText(getApplicationContext(),"Giriş Yapılamadı... Lütfen Bilgileri Kontrol ediniz",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private boolean internetkontrol(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected();
    }

}