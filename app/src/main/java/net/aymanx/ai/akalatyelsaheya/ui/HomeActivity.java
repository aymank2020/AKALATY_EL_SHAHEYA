package net.aymanx.ai.akalatyelsaheya.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.aymanx.ai.akalatyelsaheya.Interface.ItemClickListener;
import net.aymanx.ai.akalatyelsaheya.R;
import net.aymanx.ai.akalatyelsaheya.ViewHolder.MenuViewHolder;
import net.aymanx.ai.akalatyelsaheya.common.Common;
import net.aymanx.ai.akalatyelsaheya.pojo.Category;
import net.aymanx.ai.akalatyelsaheya.pojo.User;

public class HomeActivity extends AppCompatActivity {


    //Firebase init
     FirebaseDatabase database ;
     DatabaseReference category;
     TextView textFullName;
     RecyclerView recyclerView_menu;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter = null;
    FirebaseRecyclerOptions<Category> options ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //init Firebase
        //Firebase init
         database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //set Name for user
        View headerView = navigationView.getHeaderView(0);
        textFullName = headerView.findViewById(R.id.textFullName);
        textFullName.setText(Common.currentUser.getName());




        //Load Menu
        recyclerView_menu = findViewById(R.id.recycler_menu);
        recyclerView_menu.hasFixedSize();
        recyclerView_menu.setLayoutManager(new LinearLayoutManager(this));

        LoadMenu();

    }

    private void LoadMenu() {

         options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {


            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_design, parent, false);
                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, final int position, @NonNull final Category categoryModel) {
                holder.textMenuName.setText(categoryModel.getName());
                Picasso.get().load(categoryModel.getImage()).into(holder.MenuImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int postion, Boolean isLongClick) {
                        //Get CategoryID
                        Intent foodIntent = new Intent(HomeActivity.this,FoodListActivity.class);
                        foodIntent.putExtra("CategoryID",adapter.getRef( position).getKey() );
                        startActivity(foodIntent);

                    }

                });
            }
        } ;
        adapter.startListening();
        recyclerView_menu.setAdapter(adapter);
    }


}
