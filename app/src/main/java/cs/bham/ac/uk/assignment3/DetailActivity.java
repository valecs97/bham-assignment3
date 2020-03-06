package cs.bham.ac.uk.assignment3;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cs.bham.ac.uk.assignment3.models.Food;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {

    private Integer id;
    private String name;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        setTitle(name);
        preferences = getSharedPreferences("Settings",Context.MODE_PRIVATE);
        if (preferences.getStringSet("favMeals", new HashSet<>()).contains(id.toString())){
            findViewById(R.id.fabAdd).setVisibility(View.INVISIBLE);
            findViewById(R.id.fabRemove).setVisibility(View.VISIBLE);
        } else{
            findViewById(R.id.fabAdd).setVisibility(View.VISIBLE);
            findViewById(R.id.fabRemove).setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.fabAdd).setOnClickListener(view -> {
            Snackbar.make(view, "Meal added to your favourite list !", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            findViewById(R.id.fabAdd).setVisibility(View.INVISIBLE);
            findViewById(R.id.fabRemove).setVisibility(View.VISIBLE);

            SharedPreferences.Editor editor = getSharedPreferences("Settings",Context.MODE_PRIVATE).edit();
            Set<String> prefs =  preferences.getStringSet("favMeals", new HashSet<>());
            Set<String> newSet = new HashSet<>();
            newSet.add(id.toString());
            newSet.addAll(prefs);
            editor.putStringSet("favMeals",newSet);
            editor.apply();

        });

        findViewById(R.id.fabRemove).setOnClickListener(view -> {
            Snackbar.make(view, "Meal removed from your list !", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            findViewById(R.id.fabAdd).setVisibility(View.VISIBLE);
            findViewById(R.id.fabRemove).setVisibility(View.INVISIBLE);

            SharedPreferences.Editor editor = getSharedPreferences("Settings",Context.MODE_PRIVATE).edit();
            Set<String> prefs =  preferences.getStringSet("favMeals", new HashSet<>());
            Set<String> newSet = new HashSet<>();
            newSet.addAll(prefs);
            newSet.remove(id.toString());
            editor.putStringSet("favMeals",newSet);
            editor.apply();

        });

        getInitialRequest();
    }

    private void getInitialRequest(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "https://www.sjjg.uk/eat/recipe-details/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                TextView description = findViewById(R.id.description);
                TextView ingredients = findViewById(R.id.ingredients);
                TextView steps = findViewById(R.id.steps);
                try {
                    description.setText(response.getString("description"));

                    JSONArray ingredientsArray = response.getJSONArray("ingredients");
                    StringBuilder ingredientsString = new StringBuilder("Ingredients:\n");
                    for (int i=0;i<ingredientsArray.length();i++){
                        String jo = ingredientsArray.get(i).toString();
                        ingredientsString.append(i + 1).append(". ").append(jo).append("\n");
                    }
                    ingredients.setText(ingredientsString.toString());

                    JSONArray stepsArray = response.getJSONArray("steps");
                    StringBuilder stepsString = new StringBuilder("Steps:\n");
                    for (int i=0;i<stepsArray.length();i++){
                        String jo = stepsArray.get(i).toString();
                        stepsString.append(i + 1).append(". ").append(jo).append("\n");
                    }
                    steps.setText(stepsString.toString());
                } catch (JSONException err) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(getRequest);
    }

}
