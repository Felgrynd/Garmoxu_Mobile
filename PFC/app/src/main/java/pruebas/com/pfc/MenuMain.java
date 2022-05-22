package pruebas.com.pfc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import pruebas.com.pfc.databinding.ActivityMenuMainBinding;

public class MenuMain extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuMainBinding binding;

    private ImageView ivPuesto;
    private TextView tvNombre, tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

/*1>>Aqui debemos añadir los nombres de los usuarios con el id como where de condicional<<*/
        NavigationView nv = findViewById(R.id.nav_view);
        View headerView = nv.getHeaderView(0);

        ivPuesto = headerView.findViewById(R.id.ivPuesto);
        tvUser = headerView.findViewById(R.id.tvUser);
        tvNombre = headerView.findViewById(R.id.tvNombre);

        //ivPuesto.setImageResource(R.drawable.ic_launcher_foreground);
        tvUser.setText(getIntent().getStringExtra("NombreUsuario"));
        tvNombre.setText(getIntent().getStringExtra("NombreEmpleado"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("ImagenUsuario"), 0, getIntent().getByteArrayExtra("ImagenUsuario").length);
        ivPuesto.setImageBitmap(bitmap);
//1------*/

        setSupportActionBar(binding.appBarMenuMain.toolbar);
        //Evento del boton flotante
        binding.appBarMenuMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 */
                Intent intent = new Intent().setClass(MenuMain.this, DetallesPedidos.class);
                intent.putExtra("NombreUsuario", getIntent().getStringExtra("NombreUsuario"));
                intent.putExtra("btnDer", "Crear \nPedido");
                intent.putExtra("btnIzq", "Cancelar \nPedido");
                intent.putExtra("esNuevoPedido", true);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.itmMenuMain, R.id.itmPlatos, R.id.itmOtrosClientes, R.id.itmConfiguracion)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}