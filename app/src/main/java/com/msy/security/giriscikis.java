package com.msy.security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;

import android.net.ConnectivityManager;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;

import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class giriscikis extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Başlangıçtaki bekleme ProgresBarı
    private ProgressDialog sondurumokuma;
    //**************************************

    private DrawerLayout drawer;

    // Alınan tc
    public static String alınantc;
    // Alınan İsim
    public static String alınanisim;


    // Saha Kodu
    public static String saha;

    private boolean sharedadminbln;
    private boolean sharedtombln;
    public boolean shareddurumbln;
    public boolean sharedtomdurumbln;
    //**************************************


    // Barkod okutma butonu
    private Button scanbtn;
    // Tarih texti
    private TextView datetext;
    // TC texti
    private TextView tctext;


    // Barkod yazıları
    public TextView barkodtext1;
    public TextView barkodtext2;



    // Görünmeyen TextViewler
    public TextView okunantext;
    public TextView tamamlanantext;
    public TextView sahabarkodadet;

    // İkon tanımlamaları
    public ImageView barkodicon1;
    public ImageView barkodicon2;

    // Firebase işlemleri
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4;

    // Saat
    private static String saat;

    // Tarih
    private String date;
    private String datemonth;
    private String year;

    // Barkod no tanımlamaları
    public String barkodbir;
    public String barkodiki;


    private static long buaykigünal = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giriscikis);

        // Tc Text tanımlaması
        tctext = (TextView) findViewById(R.id.tctexti);


        // Barkod Noları
        barkodbir = "010101";
        barkodiki = "020202";



        // Text tanımlamaları
        barkodtext1 = (TextView) findViewById(R.id.giris);
        barkodtext2 = (TextView) findViewById(R.id.cikis);


        // Görünmeyen TextView tanımlamaları
        okunantext = (TextView) findViewById(R.id.okutulan);
        tamamlanantext = (TextView) findViewById(R.id.tamamlanan);
        sahabarkodadet = (TextView) findViewById(R.id.sahabarkodadet);


        // İkon tanımlamaları
        barkodicon1 = (ImageView) findViewById(R.id.barkodicon1);
        barkodicon2 = (ImageView) findViewById(R.id.barkodicon2);




        // Barkod okut butonu tanımlaması
        scanbtn = (Button) findViewById(R.id.scan);

        if (internetkontrol()){

            // Başlangıçtaki bekleme ProgresBarı
            sondurumokuma = new ProgressDialog(this);
            sondurumokuma.setTitle("Lütfen Bekleyiniz ...");
            sondurumokuma.setCanceledOnTouchOutside(false);
            sondurumokuma.show();

        }
        else{
            AlertDialog.Builder builder2 = new AlertDialog.Builder(giriscikis.this);
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

        // Firebase işlemleri
        mAuth = FirebaseAuth.getInstance();




        // Tc ve saha bilgisini sharedpreferencesden alma
        tc_sahaalma();

        // Tarih alma
        tarihalma();

        // NavigationView işlemleri
        draver();


        tctext.setText(alınanisim);





        // barkod okuma butonu
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barkodoku();
            }
        });

        // Mevcut kullanıcı kontrolü
        if (mAuth.getCurrentUser() == null){

            Intent Loginintent = new Intent(giriscikis.this,loginekrani.class);
            startActivity(Loginintent);
            finish();

        }
        else{
            buaykigünal();
            okunanbilgialma();
            listekontrol();
        }

    }


    private void buaykigünal() {
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference3.keepSynced(true);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Aylık Tamamlanan Gün Sayıları").child(year).child(datemonth).child(saha).child(alınanisim).exists()){
                    buaykigünal = (long)snapshot.child("Aylık Tamamlanan Gün Sayıları").child(year).child(datemonth).child(saha).child(alınanisim).getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }

    private void buaykigünyaz() {
        buaykigünal++ ;
        databaseReference4 = FirebaseDatabase.getInstance().getReference("Aylık Tamamlanan Gün Sayıları");
        databaseReference4.child(year).child(datemonth).child(saha).child(alınanisim).setValue(buaykigünal);

    }


    private void tc_sahaalma() {

        SharedPreferences sharedtom = this.getSharedPreferences("sharedtom",Context.MODE_PRIVATE);
        sharedtombln = sharedtom.getBoolean("sharedtom",false);

        //Shared Preferences tc alma
        SharedPreferences sharedtc = this.getSharedPreferences("sharedtc", Context.MODE_PRIVATE);
        alınantc = sharedtc.getString("tc",null);
        //Shared Preferences isim alma

        SharedPreferences sharedisim = this.getSharedPreferences("sharedisim", Context.MODE_PRIVATE);
        alınanisim = sharedisim.getString("isim",null);

        // Shared Preferences ile sahakodu alma
        SharedPreferences sharedsahakodu = this.getSharedPreferences("sharedsahakodu",Context.MODE_PRIVATE);
        saha = sharedsahakodu.getString("sahakodu",null);

        // Shared Preferences ile admin alma
        SharedPreferences sharedadmin = this.getSharedPreferences("sharedadmin",Context.MODE_PRIVATE);
        sharedadminbln = sharedadmin.getBoolean("sharedadmin",false);

        SharedPreferences shareddurum = this.getSharedPreferences("shareddurum",Context.MODE_PRIVATE);
        shareddurumbln = shareddurum.getBoolean("shareddurum",false);

        SharedPreferences sharedtomdurum = this.getSharedPreferences("sharedtomdurum",Context.MODE_PRIVATE);
        sharedtomdurumbln = sharedtomdurum.getBoolean("sharedtomdurum",false);


        //******************************************************************************************************************************
    }

    private void tarihalma() {
        // Tarih Alma İşlemleri
        datetext = (TextView) findViewById(R.id.date);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());
        datetext.setText(date);

        SimpleDateFormat df2 = new SimpleDateFormat("MMM");
        datemonth = df2.format(Calendar.getInstance().getTime());
        switch (datemonth){
            case "Oca":
                datemonth = "1-"+ datemonth;
                break;
            case "Şub":
                datemonth = "2-"+ datemonth;
                break;
            case "Mar":
                datemonth = "3-"+ datemonth;
                break;
            case "Nis":
                datemonth = "4-"+ datemonth;
                break;
            case "May":
                datemonth = "5-"+ datemonth;
                break;
            case "Haz":
                datemonth = "6-"+ datemonth;
                break;
            case "Tem":
                datemonth = "7-"+ datemonth;
                break;
            case "Ağu":
                datemonth = "8-"+ datemonth;
                break;
            case "Eyl":
                datemonth = "9-"+ datemonth;
                break;
            case "Eki":
                datemonth = "10-"+ datemonth;
                break;
            case "Kas":
                datemonth = "11-"+ datemonth;
                break;
            case "Ara":
                datemonth = "12-"+ datemonth;
                break;
        }

        SimpleDateFormat df3 = new SimpleDateFormat("yyyy");
        year = df3.format(Calendar.getInstance().getTime());
    }


    // NavigationView işlemleri
    //*********************************************************************************************************************
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.menu_admin:
                Intent adminsayfasına = new Intent(giriscikis.this, admin_main.class);
                startActivity(adminsayfasına);
                finish();
                break;

            case R.id.menu_giriscikis:
                Intent giriscikissayfasina = new Intent(giriscikis.this, giriscikis.class);
                startActivity(giriscikissayfasina);
                finish();
                break;

            case R.id.menu_barkod:
                Intent barkodsayfasına = new Intent(giriscikis.this,MainActivity.class);
                startActivity(barkodsayfasına);
                finish();
                break;
            case R.id.menu_yardım_ic:
                Intent icyardımsayfasına = new Intent(giriscikis.this,yardimekrani_ic.class);
                startActivity(icyardımsayfasına);
                finish();
                break;
            case R.id.menu_sorun:
                Intent sorunbildirsayfasına = new Intent(giriscikis.this,sorunbildir.class);
                startActivity(sorunbildirsayfasına);
                finish();
                break;
            case R.id.menu_kisiselbilgiler:
                Intent kisiselbilgilersayfasına = new Intent(giriscikis.this,kisisel_bilgiler.class);
                startActivity(kisiselbilgilersayfasına);
                finish();
                break;
            case R.id.menu_kisiselraporlar:
                Intent kisiseraporlarsayfasına = new Intent(giriscikis.this,kisisel_rapor.class);
                startActivity(kisiseraporlarsayfasına);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void draver() {
        //******************************************************************************************************************************

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        NavigationView navigationView = (NavigationView) findViewById(R.id.naw_view_main);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = (View) navigationView.getHeaderView(0);
        TextView navdate = (TextView) headerView.findViewById(R.id.nawdate);
        navdate.setText(date);
        TextView navisim = (TextView) headerView.findViewById(R.id.menu_isim);
        navisim.setText(alınanisim);
        TextView navtc = (TextView) headerView.findViewById(R.id.menu_tc);
        navtc.setText(alınantc);
        TextView navsaha = (TextView) headerView.findViewById(R.id.menu_saha);
        navsaha.setText(saha);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.menu_barkod);
        Menu menu = navigationView.getMenu();
        for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
            MenuItem menuItem= menu.getItem(menuItemIndex);
            if(menuItem.getItemId() == R.id.menu_barkod){
                menuItem.setVisible(sharedtombln);
            }
            if(menuItem.getItemId() == R.id.menu_admin){
                menuItem.setVisible(sharedadminbln);
            }

        }

    }
    //*********************************************************************************************************************

    // Okunan barkod bilgi alma
    private void okunanbilgialma() {

        final SharedPreferences shareddurum = this.getSharedPreferences("shareddurum",Context.MODE_PRIVATE);
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.keepSynced(true);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listekontrol();

                if (sharedtombln = false){

                    if (shareddurumbln){
                        if (snapshot.child("Giriş Çıkış Saatleri").child(year).child(datemonth).child(date).child(alınanisim + "-Çıkış").exists()){

                            if (!snapshot.child("Tamamlananlar").child(year).child(datemonth).child(date).child(alınanisim).exists()){
                                buaykigünyaz();
                            }

                            scanbtn.setEnabled(false);
                            databaseReference2.child("Tamamlananlar").child(year).child(datemonth).child(date).child(alınanisim).setValue("Tamamladı");



                            databaseReference2.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child("Gün Durumu").child("Gün").setValue("Tamamlandı");

                            shareddurumbln = false;
                            SharedPreferences.Editor editor = shareddurum.edit();
                            editor.putBoolean("shareddurum",shareddurumbln);
                            editor.commit();

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(giriscikis.this);
                            builder2.setTitle(date);
                            builder2.setMessage("Gün Tamamlandı.");
                            builder2.setCancelable(false);
                            builder2.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listekontrol();
                                    okunanbilgialma();
                                    dialog.dismiss();


                                }
                            });
                            builder2.show();
                        }
                    }
                    if (snapshot.child("Giriş Çıkış Saatleri").child(year).child(datemonth).child(date).child(alınanisim + "-Çıkış").exists()){
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(giriscikis.this);
                        builder3.setTitle(date);
                        builder3.setMessage("Gün Tamamlandı.");
                        builder3.setCancelable(false);
                        builder3.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listekontrol();
                                okunanbilgialma();
                                dialog.dismiss();


                            }
                        });
                        builder3.show();
                    }
                }


                if (sharedtombln = true){
                    if (sharedtomdurumbln){
                        if (shareddurumbln){
                            if (snapshot.child("Giriş Çıkış Saatleri").child(year).child(datemonth).child(date).child(alınanisim + "-Çıkış").exists()){

                                if (!snapshot.child("Tamamlananlar").child(year).child(datemonth).child(date).child(alınanisim).exists()){
                                    buaykigünyaz();
                                }

                                scanbtn.setEnabled(false);
                                databaseReference2.child("Tamamlananlar").child(year).child(datemonth).child(date).child(alınanisim).setValue("Tamamladı");



                                databaseReference2.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child("Gün Durumu").child("Gün").setValue("Tamamlandı");
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(giriscikis.this);
                                builder2.setTitle(date);
                                builder2.setMessage("Gün Tamamlandı.");
                                builder2.setCancelable(false);
                                builder2.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        listekontrol();
                                        dialog.dismiss();


                                    }
                                });
                                builder2.show();

                            }
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Okutulan barkodların kontorlü
    private void listekontrol() {



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Giriş Çıkış Saatleri").child(year).child(datemonth).child(date);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                sondurumokuma.dismiss();



                if (snapshot.hasChild(alınanisim + "-Giriş")){
                    barkodtext1.setTextColor(getResources().getColor(R.color.green));
                    barkodicon1.setImageResource(R.mipmap.tikicon);
                }
                if (snapshot.hasChild(alınanisim + "-Çıkış")){
                    barkodtext2.setTextColor(getResources().getColor(R.color.green));
                    barkodicon2.setImageResource(R.mipmap.tikicon);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    private boolean internetkontrol(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected();
    }


    // Barkod okuma işlemleri
    private void barkodoku() {



        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Barkodu Okutun");

        integrator.initiateScan();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        final SharedPreferences shareddurum = this.getSharedPreferences("shareddurum",Context.MODE_PRIVATE);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() != null){


                // saat bilgisini alma
                saat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.keepSynced(true);

                String yaz;


                // barkod sonucunu karşılaştırma
                switch (result.getContents()){
                    case "111111":
                        barkodtext1.setTextColor(getResources().getColor(R.color.green));
                        barkodicon1.setImageResource(R.mipmap.tikicon);
                        mDatabase.child("Giriş Çıkış Saatleri").child(year).child(datemonth).child(date).child(alınanisim + "-Giriş").setValue(saat + "-Saha:" + saha).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
                            }
                        });
                        break;

                    case  "000000":
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child("Gün Durumu").child("Devriyeler").exists()){
                                    barkodtext2.setTextColor(getResources().getColor(R.color.green));
                                    barkodicon2.setImageResource(R.mipmap.tikicon);

                                    shareddurumbln = true;
                                    // Giriş Çıkış değerini alıp sharedpreferences e kaydetme
                                    SharedPreferences.Editor editor = shareddurum.edit();
                                    editor.putBoolean("shareddurum",shareddurumbln);
                                    editor.commit();


                                    mDatabase.child("Giriş Çıkış Saatleri").child(year).child(datemonth).child(date).child(alınanisim + "-Çıkış").setValue(saat + "-Saha:" + saha ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            okunanbilgialma();
                                        }
                                    });
                                }
                                else{
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(giriscikis.this);
                                    builder3.setTitle("Uyarı ! Tom Devriyesi Tamamlanmadı");
                                    builder3.setMessage("Lütfen Tom Devriyenizi Tamamladıktan Sonra Çıkış Barkodunu Okutunuz");
                                    builder3.setCancelable(false);
                                    builder3.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();


                                        }
                                    });
                                    builder3.show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        break;

                    
                }

            }
            else {
                Toast.makeText(this,"Sonuç Yok",Toast.LENGTH_LONG).show();
            }

        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}