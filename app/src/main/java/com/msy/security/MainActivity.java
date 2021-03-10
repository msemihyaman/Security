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


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Başlangıçtaki bekleme ProgresBarı
    private ProgressDialog sondurumokuma;
    //**************************************

    private DrawerLayout drawer;

    // Alınan tc
    public static String alınantc;
    // Alınan İsim
    public static String alınanisim;


    private static long sahadevriyesayılong;
    private static long mevcutdevriyesayılong = 1;


    // Saha Kodu
    public static String saha;

    private boolean sharedadminbln;
    private boolean sharedtombln;
    private boolean sharedtomdurumbln;
    //**************************************

    private static String sahabarkodsayı;

    // Okunan Yazma
    public static int okunanyazma;



    // Barkod okutma butonu
    private Button scanbtn;
    // Tarih texti
    private TextView datetext;
    // TC texti
    private TextView tctext;

    private TextView suankidevriyetext;

    // Barkod yazıları
    public TextView barkodtext1;
    public TextView barkodtext2;
    public TextView barkodtext3;
    public TextView barkodtext4;
    public TextView barkodtext5;
    public TextView barkodtext6;
    public TextView barkodtext7;
    public TextView barkodtext8;


    // Görünmeyen TextViewler
    public TextView okunantext;
    public TextView tamamlanantext;
    public TextView sahabarkodadet;

    // İkon tanımlamaları
    public ImageView barkodicon1;
    public ImageView barkodicon2;
    public ImageView barkodicon3;
    public ImageView barkodicon4;
    public ImageView barkodicon5;
    public ImageView barkodicon6;
    public ImageView barkodicon7;
    public ImageView barkodicon8;


    // Firebase işlemleri
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4;
    private DatabaseReference databaseReference5;

    // Saat
    private static String saat;

    // Tarih
    private String date;
    private String datemonth;
    private String year;

    // Barkod no tanımlamaları
    public String barkodbir;
    public String barkodiki;
    public String barkoduc;
    public String barkoddort;
    public String barkodbes;
    public String barkodaltı;
    public String barkodyedi;
    public String barkodsekiz;

    private static final long buaykigünal = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tc Text tanımlaması
        tctext = (TextView) findViewById(R.id.tctexti);


        suankidevriyetext = (TextView) findViewById(R.id.devriye);



        // Barkod Noları
        barkodbir = "010101";
        barkodiki = "020202";
        barkoduc = "030303";
        barkoddort = "040404";
        barkodbes = "050505";
        barkodaltı = "060606";
        barkodyedi = "070707";
        barkodsekiz = "080808";


        // Text tanımlamaları
        barkodtext1 = (TextView) findViewById(R.id.barkod1);
        barkodtext2 = (TextView) findViewById(R.id.barkod2);
        barkodtext3 = (TextView) findViewById(R.id.barkod3);
        barkodtext4 = (TextView) findViewById(R.id.barkod4);
        barkodtext5 = (TextView) findViewById(R.id.barkod5);
        barkodtext6 = (TextView) findViewById(R.id.barkod6);
        barkodtext7 = (TextView) findViewById(R.id.barkod7);
        barkodtext8 = (TextView) findViewById(R.id.barkod8);

        // Görünmeyen TextView tanımlamaları
        okunantext = (TextView) findViewById(R.id.okutulan);
        tamamlanantext = (TextView) findViewById(R.id.tamamlanan);
        sahabarkodadet = (TextView) findViewById(R.id.sahabarkodadet);


        // İkon tanımlamaları
        barkodicon1 = (ImageView) findViewById(R.id.barkodicon1);
        barkodicon2 = (ImageView) findViewById(R.id.barkodicon2);
        barkodicon3 = (ImageView) findViewById(R.id.barkodicon3);
        barkodicon4 = (ImageView) findViewById(R.id.barkodicon4);
        barkodicon5 = (ImageView) findViewById(R.id.barkodicon5);
        barkodicon6 = (ImageView) findViewById(R.id.barkodicon6);
        barkodicon7 = (ImageView) findViewById(R.id.barkodicon7);
        barkodicon8 = (ImageView) findViewById(R.id.barkodicon8);



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
            AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
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
        suankidevriyetext.setText("Şuanki Devriye: " + mevcutdevriyesayılong);




        TextView[] barkodtextler = new TextView[]{barkodtext1, barkodtext2, barkodtext3, barkodtext4, barkodtext5, barkodtext6, barkodtext7, barkodtext8};
        ImageView[] barkodiconlar = new ImageView[]{barkodicon1, barkodicon2, barkodicon3, barkodicon4, barkodicon5, barkodicon6, barkodicon7, barkodicon8};


        for (int b = 0 ; b < 8; b++) {
            barkodtextler[b].setVisibility(View.INVISIBLE);
            barkodiconlar[b].setVisibility(View.INVISIBLE);
        }


        // barkod okuma butonu
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barkodoku();
            }
        });

        // Mevcut kullanıcı kontrolü
        if (mAuth.getCurrentUser() == null){

            Intent Loginintent = new Intent(MainActivity.this,loginekrani.class);
            startActivity(Loginintent);
            finish();

        }
        else{
            devriyeisleri();
            //buaykigünal();
            okunanbilgialma();

        }

    }

    private void devriyeisleri() {
       databaseReference5 = FirebaseDatabase.getInstance().getReference();
       databaseReference5.keepSynced(true);
       databaseReference5.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               sahadevriyesayılong = (long) snapshot.child("Sahalar").child(saha).child("Devriye Sayısı").getValue();
               if (snapshot.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child("Mevcut Devriye").exists()) {

                   mevcutdevriyesayılong = (long) snapshot.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child("Mevcut Devriye").child("Devriye").getValue();
               }
               listekontrol();
               suankidevriyetext.setText("Şuanki Devriye: " + mevcutdevriyesayılong);



           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }


    private void barkodgoster() {


        TextView[] barkodtextler1 = new TextView[]{barkodtext1, barkodtext2, barkodtext3, barkodtext4, barkodtext5, barkodtext6, barkodtext7, barkodtext8};
        ImageView[] barkodiconlar1 = new ImageView[]{barkodicon1, barkodicon2, barkodicon3, barkodicon4, barkodicon5, barkodicon6, barkodicon7, barkodicon8};

        int z = Integer.parseInt(sahabarkodadet.getText().toString());

        for (int d = 0 ; d < z; d++) {
            barkodtextler1[d].setVisibility(View.VISIBLE);
            barkodiconlar1[d].setVisibility(View.VISIBLE);
        }


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

        // Shared Preferences ile admin alma
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
                Intent adminsayfasına = new Intent(MainActivity.this, admin_main.class);
                startActivity(adminsayfasına);
                finish();
                break;

            case R.id.menu_giriscikis:
                Intent giriscikissayfasina = new Intent(MainActivity.this, giriscikis.class);
                startActivity(giriscikissayfasina);
                finish();
                break;

            case R.id.menu_barkod:
                Intent barkodsayfasına = new Intent(MainActivity.this,MainActivity.class);
                startActivity(barkodsayfasına);
                finish();
                break;
            case R.id.menu_yardım_ic:
                Intent icyardımsayfasına = new Intent(MainActivity.this,yardimekrani_ic.class);
                startActivity(icyardımsayfasına);
                finish();
                break;
            case R.id.menu_sorun:
                Intent sorunbildirsayfasına = new Intent(MainActivity.this,sorunbildir.class);
                startActivity(sorunbildirsayfasına);
                finish();
                break;
            case R.id.menu_kisiselbilgiler:
                Intent kisiselbilgilersayfasına = new Intent(MainActivity.this,kisisel_bilgiler.class);
                startActivity(kisiselbilgilersayfasına);
                finish();
                break;
            case R.id.menu_kisiselraporlar:
                Intent kisiseraporlarsayfasına = new Intent(MainActivity.this,kisisel_rapor.class);
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
            if(menuItem.getItemId() == R.id.menu_admin){
                menuItem.setVisible(sharedadminbln);
            }
            if(menuItem.getItemId() == R.id.menu_barkod){
                menuItem.setVisible(sharedtombln);
            }
        }

    }
    //*********************************************************************************************************************

    // Okunan barkod bilgi alma
    private void okunanbilgialma() {

        final SharedPreferences sharedtomdurum = this.getSharedPreferences("sharedtomdurum",Context.MODE_PRIVATE);

        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.keepSynced(true);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Saha barkod sayısını database den alma
                sahabarkodsayı = snapshot.child("Sahalar").child(saha).child("Barkod Sayısı").getValue(String.class);
                sahabarkodadet.setText(sahabarkodsayı);
                barkodgoster();


                if(!snapshot.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Devriye Durumu").exists()){

                    // Okunan değeri kontrolü
                    if (snapshot.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").exists()){
                        // Okunan barkod sayısını database den alma
                        String barkodsayısı = snapshot.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").getValue(String.class);
                        // Okunantext in değerini okutulan barkod sayısına eşitleme
                        okunantext.setText(barkodsayısı);

                        // // Okutulan barkod sayısı ve saha barkod sayısını karşılaştırma
                        if (barkodsayısı.equals(sahabarkodsayı) & !TextUtils.isEmpty(barkodsayısı)){
                            // Devriye durumunu Tamamlandı Olarak yazma
                            databaseReference2.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Devriye Durumu").setValue("Tamamlandı").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    if (mevcutdevriyesayılong != sahadevriyesayılong){
                                        mevcutdevriyesayılong++;
                                        databaseReference2.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child("Mevcut Devriye").child("Devriye").setValue(mevcutdevriyesayılong);
                                    }

                                }
                            });


                            sharedtomdurumbln = true;

                            SharedPreferences.Editor editor = sharedtomdurum.edit();
                            editor.putBoolean("sharedtomdurum",sharedtomdurumbln);
                            editor.commit();


                            // Tüm barkodlar okutuldu AletDialog u oluşturma
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle(date);
                            builder.setMessage("Mevcut Devriyedeki Tüm Barkodlar Okutuldu. Lütfen TAMAM Butonuna Basınız.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    okunanbilgialma();
                                    listekontrol();
                                    dialog.dismiss();
                                }
                            });
                            builder.show();


                        }
                    }

                }
                else {

                    mevcutdevriyesayılong = mevcutdevriyesayılong-1;
                    suankidevriyetext.setText("Şuanki Devriye: " + mevcutdevriyesayılong);
                    scanbtn.setEnabled(false);

                    databaseReference2.child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child("Gün Durumu").child("Devriyeler").setValue("Tamamlandı");

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setTitle(date);
                    builder2.setMessage("Devriyeler Tamamlandı.");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent giriscikisa = new Intent(MainActivity.this,giriscikis.class);
                            startActivity(giriscikisa);
                            finish();


                        }
                    });
                    builder2.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Okutulan barkodların kontorlü
    private void listekontrol() {



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(alınanisim).child("Tamamlananlar").child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong));
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                sondurumokuma.dismiss();



                if (snapshot.hasChild("010101")){
                    barkodtext1.setTextColor(getResources().getColor(R.color.green));
                    barkodicon1.setImageResource(R.mipmap.tikicon);
                }
                if (snapshot.hasChild("020202")){
                    barkodtext2.setTextColor(getResources().getColor(R.color.green));
                    barkodicon2.setImageResource(R.mipmap.tikicon);
                }
                if (snapshot.hasChild("030303")){
                    barkodtext3.setTextColor(getResources().getColor(R.color.green));
                    barkodicon3.setImageResource(R.mipmap.tikicon);
                }
                if (snapshot.hasChild("040404")){
                    barkodtext4.setTextColor(getResources().getColor(R.color.green));
                    barkodicon4.setImageResource(R.mipmap.tikicon);
                }
                if (snapshot.hasChild("050505")){
                    barkodtext5.setTextColor(getResources().getColor(R.color.green));
                    barkodicon5.setImageResource(R.mipmap.tikicon);
                }
                if (snapshot.hasChild("060606")){
                    barkodtext6.setTextColor(getResources().getColor(R.color.green));
                    barkodicon6.setImageResource(R.mipmap.tikicon);
                }
                if (snapshot.hasChild("070707")){
                    barkodtext7.setTextColor(getResources().getColor(R.color.green));
                    barkodicon7.setImageResource(R.mipmap.tikicon);
                }
                if (snapshot.hasChild("080808")){
                    barkodtext8.setTextColor(getResources().getColor(R.color.green));
                    barkodicon8.setImageResource(R.mipmap.tikicon);
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

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() != null){


                // saat bilgisini alma
                saat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(alınanisim).child("Tamamlananlar");
                mDatabase.keepSynced(true);
                mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child(result.getContents()).setValue(saat);
                okunanyazma = Integer.parseInt(okunantext.getText().toString());

                String yaz;


                // barkod sonucunu karşılaştırma
                switch (result.getContents()){
                    case "010101":
                        barkodtext1.setTextColor(getResources().getColor(R.color.green));
                        barkodicon1.setImageResource(R.mipmap.tikicon);
                        okunanyazma++;
                        yaz = Integer.toString(okunanyazma);
                        mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").setValue(yaz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
                            }
                        });
                        break;

                    case  "020202":
                        barkodtext2.setTextColor(getResources().getColor(R.color.green));
                        barkodicon2.setImageResource(R.mipmap.tikicon);
                        okunanyazma++;
                        yaz = Integer.toString(okunanyazma);
                        mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").setValue(yaz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
                            }
                        });
                        break;

                    case "030303":
                        barkodtext3.setTextColor(getResources().getColor(R.color.green));
                        barkodicon3.setImageResource(R.mipmap.tikicon);
                        okunanyazma++;
                        yaz = Integer.toString(okunanyazma);
                        mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").setValue(yaz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
                            }
                        });
                        break;

                    case "040404":
                        barkodtext4.setTextColor(getResources().getColor(R.color.green));
                        barkodicon4.setImageResource(R.mipmap.tikicon);
                        okunanyazma++;
                        yaz = Integer.toString(okunanyazma);
                        mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").setValue(yaz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
                            }
                        });
                        break;

                    case "050505":
                        barkodtext5.setTextColor(getResources().getColor(R.color.green));
                        barkodicon5.setImageResource(R.mipmap.tikicon);
                        okunanyazma++;
                        yaz = Integer.toString(okunanyazma);
                        mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").setValue(yaz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
                            }
                        });
                        break;

                    case "060606":
                        barkodtext6.setTextColor(getResources().getColor(R.color.green));
                        barkodicon6.setImageResource(R.mipmap.tikicon);
                        okunanyazma++;
                        yaz = Integer.toString(okunanyazma);
                        mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").setValue(yaz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
                            }
                        });
                        break;

                    case "070707":
                        barkodtext7.setTextColor(getResources().getColor(R.color.green));
                        barkodicon7.setImageResource(R.mipmap.tikicon);
                        okunanyazma++;
                        yaz = Integer.toString(okunanyazma);
                        mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").setValue(yaz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
                            }
                        });
                        break;

                    case "080808":
                        barkodtext8.setTextColor(getResources().getColor(R.color.green));
                        barkodicon8.setImageResource(R.mipmap.tikicon);
                        okunanyazma++;
                        yaz = Integer.toString(okunanyazma);
                        mDatabase.child(year).child(datemonth).child(date).child(String.valueOf(mevcutdevriyesayılong)).child("Okunan").setValue(yaz).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                okunanbilgialma();
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