package com.example.idnp_lab04;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private TextView layout;
    private BottomNavigationView menu;
    private ResultadosViewModel resultadosViewModel;
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultadosViewModel = new ViewModelProvider(this).get(ResultadosViewModel.class);

        layout = findViewById(R.id.pageHomeActivity);
        layout.setVisibility(View.GONE);

        menu = findViewById(R.id.menuNavegacion);
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentManager = getSupportFragmentManager();
                if (menuItem.getItemId() == R.id.menu_home) {
                    layout.setVisibility(View.GONE);
                    loadFragment(new HomeFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.menu_explorar) {
                    layout.setVisibility(View.VISIBLE);
                    layout.setText("Explorar");
                    resultadosViewModel.setfiltroSeleccionado("Lugares");
                    Log.d(TAG, "HomeActivity " + resultadosViewModel.getfiltroSeleccionado());
                    loadFragment(new ExplorarFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.menu_mapa) {
                    layout.setVisibility(View.VISIBLE);
                    layout.setText("Mapa");
                    loadFragment(new MapFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.menu_qr) {
                    layout.setVisibility(View.VISIBLE);
                    layout.setText("scan");
                    loadFragment(new QrFragment());
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        if(intent.getStringExtra("abrirFragmento").equals("DetalleObraFragment")) {
            boolean reproduciendo = intent.getBooleanExtra("estaReproduciendo", false);
            Log.d(TAG, "Cargando fragmento DetalleObraFragment");
            loadFragment(DetalleObraFragment.newInstance(reproduciendo));
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        Log.d(TAG, "loadFragment() Cargando fragmento DetalleObraFragment");
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragments, fragment);
        fragmentTransaction.addToBackStack(null); // Añade el fragmento al back stack
        fragmentTransaction.commit();
    }
}