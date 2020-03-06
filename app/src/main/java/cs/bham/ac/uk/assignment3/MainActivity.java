package cs.bham.ac.uk.assignment3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cs.bham.ac.uk.assignment3.models.Food;

public class MainActivity extends FragmentActivity
        implements MainListFragment.OnListFragmentInteractionListener, FavouriteFragment.OnListFragmentInteractionListener, FilterFragment.CallFilterRefresh {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.main_fragment, mainListFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_favourites:
                    transaction.replace(R.id.main_fragment, favouriteFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_filter:
                    transaction.replace(R.id.main_fragment, filterFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
            }

            return false;
        }
    };

    FragmentManager fragmentManager = getSupportFragmentManager();
    MainListFragment mainListFragment = new MainListFragment();
    FilterFragment filterFragment = new FilterFragment();
    FavouriteFragment favouriteFragment = new FavouriteFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.main_fragment, mainListFragment).commit();
            if (!isNetworkConnected())
                Snackbar.make(findViewById(android.R.id.content), "You are offline ! Please check your data connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }


    }

    @Override
    public void onListFragmentInteraction(Food item) {

    }

    @Override
    public void refresh() {
        mainListFragment.refresh();
    }

    Boolean firstTime = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstTime) {
            Log.d("PREFS", getSharedPreferences("Settings", Context.MODE_PRIVATE).getStringSet("favMeals", new HashSet<>()).toString());
            Fragment frg = null;
            frg = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();

            //Log.d("PREFS", getSharedPreferences("Settings", Context.MODE_PRIVATE).getStringSet("favMeals", new HashSet<>()).toString());
        } else
            firstTime = false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        /*android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);*/
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
