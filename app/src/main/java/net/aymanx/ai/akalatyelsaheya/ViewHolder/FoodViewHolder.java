package net.aymanx.ai.akalatyelsaheya.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.aymanx.ai.akalatyelsaheya.Interface.ItemClickListener;
import net.aymanx.ai.akalatyelsaheya.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView foodName;
    public ImageView foodImage;

    private ItemClickListener itemClickListener;
    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        foodName = itemView.findViewById(R.id.food_name);
        foodImage = itemView.findViewById(R.id.food_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
