package net.aymanx.ai.akalatyelsaheya.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.aymanx.ai.akalatyelsaheya.Interface.ItemClickListener;
import net.aymanx.ai.akalatyelsaheya.R;
import net.aymanx.ai.akalatyelsaheya.ViewHolder.FoodViewHolder;
import net.aymanx.ai.akalatyelsaheya.ViewHolder.MenuViewHolder;
import net.aymanx.ai.akalatyelsaheya.pojo.Category;
import net.aymanx.ai.akalatyelsaheya.pojo.Food;

public class FoodListActivity extends AppCompatActivity {

    //Firebase init
    FirebaseDatabase database ;
    DatabaseReference food;
    RecyclerView recyclerView_food;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = null;
    String categoryID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase init
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");
        //Load Menu
        recyclerView_food = findViewById(R.id.recycler_food);
        recyclerView_food.setHasFixedSize(true);
        recyclerView_food.setLayoutManager(new LinearLayoutManager(this));

        //get Intent that contain Category ID
        if(getIntent() != null){
            categoryID=getIntent().getStringExtra("CategoryID");
        }
        if (!categoryID.isEmpty() && categoryID != null ){
            LoadListFood(categoryID);

        }


    }

    private void LoadListFood(final String mCategoryID) {

        Query searchByName = food.orderByChild("MenuID").equalTo(mCategoryID);

        FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByName, Food.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodOptions) {
                @Override
                protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull final Food foodModel) {
                    holder.foodName.setText(foodModel.getName());

                    Picasso.get().load(foodModel.getImage()).into(holder.foodImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(FoodListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int postion, Boolean isLongClick) {
                            Toast.makeText(FoodListActivity.this, " "+foodModel.getName(), Toast.LENGTH_SHORT).show();

                        }

                    });

                }

                @NonNull
                @Override
                public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_design, parent, false);
                    return new FoodViewHolder(view);
                }
            };


            recyclerView_food.setAdapter(adapter);
            adapter.startListening();

    }
}
