package cs.bham.ac.uk.assignment3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cs.bham.ac.uk.assignment3.adapter.FoodAdapter;
import cs.bham.ac.uk.assignment3.models.Food;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavouriteFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private ArrayList<Food> list = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPref;

    public FavouriteFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavouriteFragment newInstance(int columnCount) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public void refresh() {
        //Log.d("Request link", "https://www.sjjg.uk/eat/food-items" + filter.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET,
                "https://www.sjjg.uk/eat/food-items", null,
                items -> populateList(items,getContext()),
                error -> Log.e("Error Volley", error.getMessage())
        );
        requestQueue.add(getRequest);
    }

    private void populateList(JSONArray items, Context context) {
        Set<String> ids = context.getSharedPreferences("Settings",Context.MODE_PRIVATE).getStringSet("favMeals", new HashSet<>());
        list.clear();
        try {
            for (int i = 0; i < items.length(); i++) {
                JSONObject jo = items.getJSONObject(i);
                if (ids.contains(String.valueOf(jo.getInt("id"))))
                    list.add(new Food(jo.getInt("id"), jo.getString("name"), jo.getString("meal"), jo.getInt("time")));
            }
        } catch (JSONException err) {
        }
        foodAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_list, container, false);
        foodAdapter = new FoodAdapter(list);
        sharedPref = getActivity().getSharedPreferences("Settings",Context.MODE_PRIVATE);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(foodAdapter);
            refresh();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Food item);
    }


}
