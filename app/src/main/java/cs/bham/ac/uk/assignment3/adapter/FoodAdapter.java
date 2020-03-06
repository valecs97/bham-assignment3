package cs.bham.ac.uk.assignment3.adapter;

import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cs.bham.ac.uk.assignment3.DetailActivity;
import cs.bham.ac.uk.assignment3.R;
import cs.bham.ac.uk.assignment3.models.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private ArrayList<Food> list;

    public FoodAdapter(ArrayList<Food> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mainlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).toString());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("id", list.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public final TextView textView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView.getText() + "'";
        }
    }
}
