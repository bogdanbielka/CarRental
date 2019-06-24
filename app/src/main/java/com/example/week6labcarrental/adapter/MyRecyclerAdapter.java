package com.example.week6labcarrental.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.week6labcarrental.R;
import com.example.week6labcarrental.model.Car;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    public static final String TAG = "MyRecyclerAdapter";
    private LayoutInflater mLayoutInflater;
    private ItemClickListener mClickListener;
    private List<Car> mData;

    public MyRecyclerAdapter(Context context, List<Car> data){
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mData = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.layout_item, viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.category.setText(mData.get(position).getCategory());
        viewHolder.availability.setText(mData.get(position).getAvailibility());
        viewHolder.carPriceDay.setText(String.valueOf(mData.get(position).getPricePerDay()));
        viewHolder.carPriceHour.setText(String.valueOf(mData.get(position).getPricePerHour()));
        viewHolder.seats.setText(mData.get(position).getSeats());
        viewHolder.carModel.setText(mData.get(position).getCarModel());
        viewHolder.carMake.setText(mData.get(position).getCarMake());
        viewHolder.color.setText(mData.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        if(mData != null) {
            return mData.size();
        } return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView category, carMake, carModel, carPriceHour, carPriceDay, seats, availability, color;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.txtCategory);
            carMake = itemView.findViewById(R.id.txtCarMake);
            carModel = itemView.findViewById(R.id.txtCarModel);
            carPriceHour = itemView.findViewById(R.id.txtPriceHour);
            carPriceDay = itemView.findViewById(R.id.txtPriceDay);
            seats = itemView.findViewById(R.id.txtSeats);
            availability = itemView.findViewById(R.id.txtAvailability);
            color = itemView.findViewById(R.id.txtColor);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            Log.d("TAG","View: Clicked!");
        }

        @Override
        public boolean onLongClick(View v) {
            mClickListener.onItemLongClick(v, getAdapterPosition());
            Log.d("TAG","View:Long Clicked!");
            return true;
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
        Log.d("TAG","Success: Clicked!");
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

}
