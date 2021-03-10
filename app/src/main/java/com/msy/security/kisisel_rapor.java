package com.msy.security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class kisisel_rapor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    // Tarih
    private String date;
    private String datemonth;
    private String year;

    //**************************************

    private DrawerLayout drawer;
    public static String alınantc;
    public static String alınanisim;
    public static String saha;
    //**************************************
    private static boolean sharedadminbln;
    private boolean sharedtombln;


    private TextView  bugun;
    private TextView buay;
    private TextView kayıttarih;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase2;
    private DatabaseReference mDatabase3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisisel_rapor);

        //******************************************************************************************************************************
        // Tc ve saha bilgisini sharedpreferencesden alma
        tc_sahaalma();

        // Tarih alma
        tarihalma();

        // NavigationView işlemleri
        drawer();


        bugun = (TextView) findViewById(R.id.bilgi_bugunku_durum);
        buay = (TextView) findViewById(R.id.bilgi_ay_tamamlanan);
        kayıttarih = (TextView) findViewById(R.id.bilgi_kayıt_tarih);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase1 = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase3 = FirebaseDatabase.getInstance().getReference();


        verial();




    }

    private void verial() {

        DatabaseReference bugunkudata = mDatabase3.child("Tamamlananlar");
        bugunkudata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(year).child(datemonth).child(date).child(alınanisim).exists()){
                    String veri1 = snapshot.child(year).child(datemonth).child(date).child(alınanisim).getValue().toString();
                    bugun.setText(veri1);
                }
                else {
                    bugun.setText("Tamamlanmadı");
                    bugun.setTextColor(Color.RED);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference buaykitamamlanangun = mDatabase.child("Aylık Tamamlanan Gün Sayıları");
        buaykitamamlanangun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot3) {

                if (snapshot3.child(year).child(datemonth).child(saha).child(alınanisim).exists()){
                    String aydata = snapshot3.child(year).child(datemonth).child(saha).child(alınanisim).getValue().toString();
                    buay.setText(aydata);

                }
                else{
                    buay.setText(0);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        

        DatabaseReference kayittarihi = mDatabase2.child("Kullanıcılar").child(alınanisim).child("Kişisel Bilgiler").child("6 Kayıt Tarihi");
        kayittarihi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot4) {
                String tarih = snapshot4.getValue().toString();
                kayıttarih.setText(tarih);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void tarihalma() {
        // Tarih Alma İşlemleri

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

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
        //******************************************************************************************************************************
    }

    private void drawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        drawer = findViewById(R.id.drawer_layout_admin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        NavigationView navigationView = findViewById(R.id.naw_view_admin);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navisim = headerView.findViewById(R.id.menu_isim);
        navisim.setText(alınanisim);
        TextView navtc = headerView.findViewById(R.id.menu_tc);
        navtc.setText(alınantc);
        TextView navsaha = (TextView) headerView.findViewById(R.id.menu_saha);
        navsaha.setText(saha);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.menu_barkod);
        TextView navdate = (TextView) headerView.findViewById(R.id.nawdate);
        navdate.setText(date);
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
        //******************************************************************************************************************************

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_admin:
                Intent adminsayfasına = new Intent(kisisel_rapor.this, admin_main.class);
                startActivity(adminsayfasına);
                finish();
                break;

            case R.id.menu_giriscikis:
                Intent giriscikissayfasina = new Intent(kisisel_rapor.this, giriscikis.class);
                startActivity(giriscikissayfasina);
                finish();
                break;

            case R.id.menu_barkod:
                Intent barkodsayfasına = new Intent(kisisel_rapor.this, MainActivity.class);
                startActivity(barkodsayfasına);
                finish();
                break;
            case R.id.menu_yardım_ic:
                Intent icyardımsayfasına = new Intent(kisisel_rapor.this,yardimekrani_ic.class);
                startActivity(icyardımsayfasına);
                finish();
                break;
            case R.id.menu_sorun:
                Intent sorunbildirsayfasına = new Intent(kisisel_rapor.this,sorunbildir.class);
                startActivity(sorunbildirsayfasına);
                finish();
                break;

            case R.id.menu_kisiselbilgiler:
                Intent kisiselbilgilersayfasına = new Intent(kisisel_rapor.this,kisisel_bilgiler.class);
                startActivity(kisiselbilgilersayfasına);
                finish();
                break;
            case R.id.menu_kisiselraporlar:
                Intent kisiseraporlarsayfasına = new Intent(kisisel_rapor.this,kisisel_rapor.class);
                startActivity(kisiseraporlarsayfasına);
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}