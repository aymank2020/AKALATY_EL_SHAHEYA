package net.aymanx.ai.akalatyelsaheya.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.aymanx.ai.akalatyelsaheya.R;

public class FoodDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        String name = getIntent().getStringExtra("FoodName");
        if (name == null || name.isEmpty()) {
            finish();
            return;
        }
        setTitle(name);
        ((TextView) findViewById(R.id.food_name)).setText(name);
        String price = getIntent().getStringExtra("FoodPrice");
        ((TextView) findViewById(R.id.food_price)).setText(
                getString(R.string.food_price_label, price == null ? getString(R.string.food_price_unavailable) : price));
        ((TextView) findViewById(R.id.food_description)).setText(getIntent().getStringExtra("FoodDescription"));
        String image = getIntent().getStringExtra("FoodImage");
        if (image != null && !image.isEmpty()) {
            Picasso.get().load(image).into((ImageView) findViewById(R.id.image_food));
        }
    }
}
