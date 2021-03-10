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
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class admin_sahaekleme extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    //**************************************

    private DrawerLayout drawer;
    public static String alınantc;
    public static String alınanisim;
    public static String saha;
    //**************************************
    private static boolean sharedadminbln;
    private boolean sharedtombln;


    // Tarih
    public String date;

    // Database işlemleri
    private DatabaseReference mDatabase,mDatabase2,mDatabase3;

    // EditText tanımlamaları
    private EditText sahaismi;
    private EditText sahabarkodsayısı;
    private EditText sahadevriyesayısı;

    // String tanımlamaları
    private String saha2;
    private String barkodsayısı;
    private String devriyesayısı;

    // Button tanımlamaları
    private Button adminokeybtn;
    private CheckBox tombox;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sahaekle);

        tc_sahaalma();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

        draver();



        // EditText tanımlamaları
        sahaismi = (EditText) findViewById(R.id.AdminSahaİsmiAlanı);
        sahabarkodsayısı = (EditText) findViewById(R.id.AdminSahaBarkodSayısı);
        sahadevriyesayısı = (EditText) findViewById(R.id.AdminSahaDevriyeSayısı);
        // Button tanımlamaları
        adminokeybtn = (Button) findViewById(R.id.adminokeybtn);
        tombox = (CheckBox) findViewById(R.id.tombox);

        sahaismi.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        tombox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tombox.isChecked()){

                    sahabarkodsayısı.setVisibility(View.VISIBLE);
                    sahadevriyesayısı.setVisibility(View.VISIBLE);
                }

                if (!tombox.isChecked()){
                    sahabarkodsayısı.setVisibility(View.INVISIBLE);
                    sahadevriyesayısı.setVisibility(View.INVISIBLE);
                }
            }
        });


        // Admin okey button işlevi
        adminokeybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminsahagiris();
            }
        });


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



    // Adminin saha girişleri fonksiyonu
    private void adminsahagiris() {

        if (tombox.isChecked()) {

            // Sahakodu edittextinden değer alıp sahaya eşitleme
            saha2 = sahaismi.getText().toString();
            // Barkodsayısı edittextinden değer alıp barkod sayısına eşitleme
            barkodsayısı = sahabarkodsayısı.getText().toString();

            devriyesayısı = sahadevriyesayısı.getText().toString();
            long devriyesayısılong = Long.parseLong(devriyesayısı);

            // Database e kaydetme
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Sahalar");
            mDatabase.child(saha2).child("Barkod Sayısı").setValue(barkodsayısı);

            mDatabase2= FirebaseDatabase.getInstance().getReference().child("Sahalar");
            mDatabase2.child(saha2).child("Devriye Sayısı").setValue(devriyesayısılong);

            mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Sahalar");
            mDatabase3.child(saha2).child("Tom").setValue("Evet");

        }

        if (!tombox.isChecked()){

            // Sahakodu edittextinden değer alıp sahaya eşitleme
            saha2 = sahaismi.getText().toString();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Sahalar");
            mDatabase.child(saha2).child("Tom").setValue("Hayır");
        }


    }

    // NavigationView işlemleri
    //*********************************************************************************************************************
    private void draver() {
        //******************************************************************************************************************************

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        drawer = findViewById(R.id.drawer_layout_admin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
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
        switch (item.getItemId()){

            case R.id.menu_admin:
                Intent adminsayfasına = new Intent(admin_sahaekleme.this, admin_main.class);
                startActivity(adminsayfasına);
                finish();
                break;

            case R.id.menu_giriscikis:
                Intent giriscikissayfasina = new Intent(admin_sahaekleme.this, giriscikis.class);
                startActivity(giriscikissayfasina);
                finish();
                break;

            case R.id.menu_barkod:
                Intent barkodsayfasına = new Intent(admin_sahaekleme.this,MainActivity.class);
                startActivity(barkodsayfasına);
                finish();
                break;
            case R.id.menu_yardım_ic:
                Intent icyardımsayfasına = new Intent(admin_sahaekleme.this,yardimekrani_ic.class);
                startActivity(icyardımsayfasına);
                finish();
                break;
            case R.id.menu_sorun:
                Intent sorunbildirsayfasına = new Intent(admin_sahaekleme.this,sorunbildir.class);
                startActivity(sorunbildirsayfasına);
                finish();
                break;
            case R.id.menu_kisiselbilgiler:
                Intent kisiselbilgilersayfasına = new Intent(admin_sahaekleme.this,kisisel_bilgiler.class);
                startActivity(kisiselbilgilersayfasına);
                finish();
                break;
            case R.id.menu_kisiselraporlar:
                Intent kisiseraporlarsayfasına = new Intent(admin_sahaekleme.this,kisisel_rapor.class);
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

        Intent adminsayfasına = new Intent(admin_sahaekleme.this, admin_main.class);
        startActivity(adminsayfasına);
        finish();

    }
    //*********************************************************************************************************************

}