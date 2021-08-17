package com.example.holamundo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.holamundo.R;
import com.example.holamundo.models.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context mContext;
    private ArrayList<Category> categoryList;

    //Generacion de actividades OnItemListener (presionar click) ////////////////////////////////////////
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClicklistener(OnItemClickListener listener) {
        mListener = listener;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    public CategoryAdapter(Context context, ArrayList<Category> categoryList) {//INICIA CONSTRUCTOR
        mContext = context;
        this.categoryList = categoryList;
    }//TERMINA CONSTRUCTOR

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category currentItem = categoryList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String creatorName = currentItem.getName();
        int likeCount = currentItem.getId();
        holder.mTextViewCreator.setText(creatorName);
        holder.mTextViewLikes.setText("" + likeCount);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
        //Picasso.with(mContext).load("file:///android_asset/DvpvklR.png").into(mImageView);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            mTextViewLikes = itemView.findViewById(R.id.text_view_likes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }

            });

        }
    }
}

