package cs.bham.ac.uk.assignment3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cs.bham.ac.uk.assignment3.adapter.FoodAdapter;
import cs.bham.ac.uk.assignment3.models.Food;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MainListFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainListFragment() {
    }

    private ArrayList<Food> list = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void refresh() {
        String prefMeal = sharedPref.getString("mealPref", "Any");
        String prefOrder = sharedPref.getString("orderPref", "Default");
        StringBuilder filter = new StringBuilder();

        if (!prefMeal.equals("Any") || !prefOrder.equals("Default")){
            filter.append("?");
            Boolean multiple = false;
            if (!prefMeal.equals("Any")) {
                filter.append("prefer=" + prefMeal);
                multiple = true;
            }
            if (!prefOrder.equals("Default")) {
                if (multiple) filter.append("&");
                filter.append("ordering=" + prefOrder.toLowerCase());
            }
        }
        //Log.d("Request link", "https://www.sjjg.uk/eat/food-items" + filter.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET,
                "https://www.sjjg.uk/eat/food-items" + filter.toString(), null,
                this::populateList,
                error -> Log.e("Error Volley", error.getMessage())
        );
        requestQueue.add(getRequest);
    }

    private void populateList(JSONArray items) {
        list.clear();
        try {
            for (int i = 0; i < items.length(); i++) {
                JSONObject jo = items.getJSONObject(i);
                list.add(new Food(jo.getInt("id"), jo.getString("name"), jo.getString("meal"), jo.getInt("time")));
            }
        } catch (JSONException err) {
        }
        foodAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainlist_list, container, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Food item);
    }
}
