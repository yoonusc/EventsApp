package mycodlabs.events;


        import android.app.ProgressDialog;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.database.Cursor;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.roger.catloadinglibrary.CatLoadingView;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;



public class DisplayEventList extends Fragment {


    public SQLiteDatabase db;
    UserSessionManager session;

    private static Context context = null;
    private static final String TAG = UserProfile.class.getSimpleName();
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DETAILS = "details";
    private static String usertype_id;

    private static ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private View nodataView;
    private GridLayoutManager gridLayoutManager;
    private eventAdapter adapter;

    private int loadlimit;
    boolean loading;
    boolean error_load;
    private int category_id;
    private String user_id;
    RequestQueue queue;
    CatLoadingView mView;
    ArrayList<Medais> medias;
    // URL to get events JSON
    Spinner category;
    ArrayList<eventItem> eventList;
    eventAdapter eventAdapter;

    protected void createDatabase(){
        db=getActivity().openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Events"+category_id+" (event_id INTEGER NOT NULL, name VARCHAR NOT NULL, time VARCHAR NOT NULL, venue VARCHAR NOT NULL, details VARCHAR NOT NULL, usertype_id INTEGER NOT NULL, usertype VARCHAR NOT NULL, creator_id INTEGER NOT NULL, creator VARCHAR NOT NULL, category_id INTEGER NOT NULL, category VARCHAR NOT NULL);");
    }

    protected void insertIntoDB(eventItem e){

        String query = "INSERT INTO Events"+category_id+" (event_id,name,time,venue,details,usertype_id,usertype,creator_id,creator,category_id,category) VALUES('"+e.getEvent_id()+"','"+e.getName()+"','"+e.getTime()+"','"+e.getVenue()+"', '"+e.getDetails()+"', '"+e.getUsertype_id()+"', '"+e.getUsertype()+"', '"+e.getCreator_id()+"', '"+e.getCreator()+"', '"+e.getCategory_id()+"', '"+e.getCategory()+"');";

        db.execSQL(query);
    }
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        context = getActivity();
        queue = Volley.newRequestQueue(context);
        session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        usertype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);
        user_id = user.get(UserSessionManager.KEY_USER_ID);
        category_id =getArguments().getInt("category_id", 0);
        mView=new CatLoadingView();
        eventList = new ArrayList<>();
        medias=new ArrayList<>();
       // createDatabase();
        return inflater.inflate(R.layout.display_event_list_layout, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        nodataView=(View) getView().findViewById(R.id.no_data_found);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        eventList = new ArrayList<>();
        lodData(0);
        adapter = new eventAdapter(context,eventList);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(
//                new DividerItemDecoration(getActivity(), null));


    }




    @Override
    public void onResume() {

        super.onResume();

    }





    @Override
    public void onStart() {
        super.onStart();
//        eventList.clear();
//        lodData(0);
        loadlimit = 0;
        loading = false;
        error_load = false;



    }


    private void lodData(final int id) {

        showDialog();
        eventList.clear();
        String path="";
        if(category_id==0) //public
        {
        path= getResources().getString(R.string.listAll_url)   ;
        }
        else if(category_id==3)
        {
            path= getResources().getString(R.string.listPast_url)   ;
        }
        else if(category_id==1) //public
        {
            path= getResources().getString(R.string.listAll_url)   ;
        }
        String finalPath = path;
        final StringRequest eventRequest= new StringRequest(Request.Method.POST,path,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try{
                            Log.d("response "+ finalPath,response.toString());
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {
                                JSONArray array = jsonResponse.getJSONArray("events");
//                                db.execSQL("DELETE FROM Events"+category_id);
                                for (int i=0; i<array.length(); i++){

                                    JSONObject e = array.getJSONObject(i);
                                    eventItem data = new eventItem(e.getString("duration"),e.getString("updated"),e.getInt("event_id"),e.getString("creator"),e.getString("name"),e.getString("time"),e.getString("venue"),e.getString("details"),e.getInt("usertype_id"),e.getString("usertype"),e.getInt("creator_id"),e.getString("creator"),e.getInt("category_id"),e.getString("category"));
                                    eventList.add(data);
                                    // insertIntoDB(data);
                                    getImages(0, new OncompleteCallaback() {
                                        @Override
                                        public void oncomplete() {

                                            for(eventItem each:eventList)
                                            {

                                                for(Medais medais:medias)
                                                {

                                                    if(medais.getEvent_id()==each.getEvent_id())
                                                    {
                                                        if(each.getMedais()==null)
                                                        {
                                                            ArrayList<Medais> data=new ArrayList<>();
                                                            data.add(medais);
                                                            each.setMedais(data);
                                                        }
                                                        else
                                                        {
                                                            each.getMedais().add(medais);
                                                        }

                                                    }

                                                }


                                            }
                                            hideDialog();
                                            setupFeed(eventList);
                                        }

                                        @Override
                                        public void onError() {
                                            Toast.makeText(getContext(),"failed fetching images ",Toast.LENGTH_LONG).show();
                                            nodataView.setVisibility(View.VISIBLE);
                                            hideDialog();
                                        }
                                    });

                                }

//                                loading = false;

                            } else {

                                Log.d("failed","failed");
                                hideDialog();
                                nodataView.setVisibility(View.VISIBLE);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Toast.makeText(getContext(),"Error fetching server",Toast.LENGTH_LONG).show();
                nodataView.setVisibility(View.VISIBLE);
                hideDialog();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("limit", id+"");
                params.put("usertype_id", usertype_id+"");
                params.put("category_id", category_id+"");
                params.put("user_id", user_id+"");
                return params;
            }

        };

        queue.add(eventRequest);


    }
    private void getImages(final int eventId , final OncompleteCallaback oncompleteCallaback) {


        StringRequest eventRequest= new StringRequest(Request.Method.GET, getResources().getString(R.string.get_media+eventId),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try{
                            Log.d("response",response.toString());
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {
                                medias.clear();
                                JSONArray array = jsonResponse.getJSONArray("media");
                                for (int i=0; i<array.length(); i++){

                                    JSONObject e = array.getJSONObject(i);
                                    Medais data = new Medais(e.getInt("id"),e.getInt("event_id"),e.getString("type"),e.getString("path"));//)//,e.getString("name"),e.getString("time"),e.getString("venue"),e.getString("details"),e.getInt("usertype_id"),e.getString("usertype"),e.getInt("creator_id"),e.getString("creator"),e.getInt("category_id"),e.getString("category"));
                                    medias.add(data);

                                }

                                oncompleteCallaback.oncomplete();


                            } else {

                                Log.d("failed","failed");
                                hideDialog();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Toast.makeText(getContext(),"Error fetching image",Toast.LENGTH_LONG).show();



            }
        }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                return params;
            }

        };

        queue.add(eventRequest);


    }



    private void setupFeed(ArrayList<eventItem> events) {

        if(events.size()>0) {
            nodataView.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
            eventAdapter = new eventAdapter(getContext(), events);
            eventAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(eventAdapter);
        }
        else

        {
            nodataView.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null){
            db.close();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null){
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    boolean isShowing=false;
    public void showDialog() {


        if(!isShowing) {
            try {

                mView.show(getActivity().getSupportFragmentManager(), "");
                isShowing=true;
            } catch (Exception e) {
                Log.d("exception", e.getMessage());
            }
        }
    }
    public void hideDialog() {

        try {
            if(isShowing) {
                mView.dismiss();
                isShowing=false;
            }
        }
        catch (Exception e)
        {
            Log.d("exception",e.getMessage());
        }
    }


    public interface  OncompleteCallaback
    {

        public void oncomplete();
        public void onError();
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

}