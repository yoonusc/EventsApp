package mycodlabs.events;

/** * Created by mycodlabs */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListEventsTabs extends Fragment {


    private Context context = null;

    public SQLiteDatabase db;

    RequestQueue queue;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.tab_layout,null);

        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        context = getActivity();
        queue = Volley.newRequestQueue(context);

        use_old_category();


        return x;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (queue != null){

            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
        if(db != null){
            db.close();
        }
    }

    protected void createDatabase(){
        db=getActivity().openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS category (category_id INTEGER NOT NULL, name VARCHAR NOT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS usertype (usertype_id INTEGER NOT NULL, name VARCHAR NOT NULL);");
    }

    protected void insertIntoDBcategory(String category_id, String name){
        String query = "INSERT INTO category (category_id,name) VALUES('"+category_id+"', '"+name+"');";
        db.execSQL(query);
    }

    protected void insertIntoDBusertype(String usertype_id, String name){
        String query = "INSERT INTO usertype (usertype_id,name) VALUES('"+usertype_id+"', '"+name+"');";
        db.execSQL(query);
    }





    private void use_old_category(){

        ArrayList<String> categories = new ArrayList<>();
        //Position after the last row means the end of the results
            categories.add("UpComing");
            categories.add("Passed");
            categories.add("Private");
        viewPager.setAdapter(new dynamicAdapter(getChildFragmentManager(),categories));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

    }


    class dynamicAdapter extends FragmentPagerAdapter{

        ArrayList<String> pass_category;

        public dynamicAdapter(FragmentManager fm, ArrayList<String> category) {
            super(fm);
            this.pass_category = category;


        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = new DisplayEventList();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            int cat=0;
            if(position==0)
            {
                cat=0;  // upcoming
            }
            else if(position==1)
            {
                cat=3; // passed
            }
            else if(position==2)
            {
                cat=1; //private
            }

            args.putInt("category_id", cat);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {

            return pass_category.size();

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            return  pass_category.get(position);
        }
    }

}