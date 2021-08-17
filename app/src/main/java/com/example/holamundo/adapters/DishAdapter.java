package com.example.holamundo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.holamundo.R;
import com.example.holamundo.models.Dish;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    private Context context;
    private ArrayList<Dish> dishList;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public DishAdapter(Context context, ArrayList<Dish> dishList){
        this.context = context;
        this.dishList = dishList;
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.dish_item, parent, false);
        return  new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DishViewHolder holder, int position) {
        Dish dish;

        dish = dishList.get(position);
        holder.textViewId.setText(""+dish.getId());
        holder.textViewName.setText(dish.getName());
        holder.textViewDescription.setText(dish.getDescription());
        holder.textViewPrice.setText(""+dish.getPrice());
        Picasso.get().load(dish.getImagePath()).fit().centerInside().into(holder.imageViewDish);
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageViewDish;
        public TextView textViewId;
        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewPrice;

        public DishViewHolder(View itemView) {
            super(itemView);
            imageViewDish = itemView.findViewById(R.id.imageViewDish);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
