package com.msy.security;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class admin_yillikizin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //**************************************

    private DrawerLayout drawer;
    public static String alınantc;
    public static String alınanisim;
    public static String saha;
    //**************************************
    private static boolean sharedadminbln;
    private boolean sharedtombln;
    private static long günlong;
    private static long günlongson;

    private String etisim;
    private String etgün;
    private static long izingün;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;

    private Button okeybtn;
    private EditText isim;
    private EditText gün;


    // Tarih
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_yillikizin);
        //******************************************************************************************************************************

        // Tc ve saha bilgisini sharedpreferencesden alma
        tc_sahaalma();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

        // NavigationView işlemleri
        drawer();

        okeybtn = (Button) findViewById(R.id.adminokeybtn);
        isim = (EditText) findViewById(R.id.AdminİzinİsmAlanı);
        gün = (EditText) findViewById(R.id.AdminİzinGünSayısı);

        isim.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        okeybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                günyaz();
            }
        });

    }

    private void günyaz() {

        etisim = isim.getText().toString();
        etgün = gün.getText().toString();
        günlong = Long.parseLong(etgün);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(etisim).child("Kişisel Bilgiler");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("7 Yıllık İzin Gün Sayısı").exists()){
                    izingün = snapshot.child("7 Yıllık İzin Gün Sayısı").getValue(Long.class);
                    günlongson = günlong + izingün;
                    yaz();
                }
                else{
                    izingün = 0;
                    günlongson = günlong;
                    yaz();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void yaz(){
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.child("Kullanıcılar").child(etisim).child("Kişisel Bilgiler").child("7 Yıllık İzin Gün Sayısı").setValue(günlongson);
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
                Intent adminsayfasına = new Intent(admin_yillikizin.this, admin_main.class);
                startActivity(adminsayfasına);
                finish();
                break;

            case R.id.menu_giriscikis:
                Intent giriscikissayfasina = new Intent(admin_yillikizin.this, giriscikis.class);
                startActivity(giriscikissayfasina);
                finish();
                break;

            case R.id.menu_barkod:
                Intent barkodsayfasına = new Intent(admin_yillikizin.this, MainActivity.class);
                startActivity(barkodsayfasına);
                finish();
                break;
            case R.id.menu_yardım_ic:
                Intent icyardımsayfasına = new Intent(admin_yillikizin.this,yardimekrani_ic.class);
                startActivity(icyardımsayfasına);
                finish();
                break;
            case R.id.menu_sorun:
                Intent sorunbildirsayfasına = new Intent(admin_yillikizin.this,sorunbildir.class);
                startActivity(sorunbildirsayfasına);
                finish();
                break;
            case R.id.menu_kisiselbilgiler:
                Intent kisiselbilgilersayfasına = new Intent(admin_yillikizin.this,kisisel_bilgiler.class);
                startActivity(kisiselbilgilersayfasına);
                finish();
                break;
            case R.id.menu_kisiselraporlar:
                Intent kisiseraporlarsayfasına = new Intent(admin_yillikizin.this,kisisel_rapor.class);
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
        }

        Intent adminsayfasına = new Intent(admin_yillikizin.this, admin_main.class);
        startActivity(adminsayfasına);
        finish();
    }
}
