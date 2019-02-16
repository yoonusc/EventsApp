package mycodlabs.events;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.roger.catloadinglibrary.CatLoadingView;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mycodlabs.adapters.Clickcb;
import mycodlabs.adapters.OldImagesAdapter;
import mycodlabs.instapp.PublishActivity;
import mycodlabs.instapp.Utils;
import mycodlabs.utils.Events;
import mycodlabs.utils.GlobalBus;
import mycodlabs.utils.PickerDialogFragment;

public class EditOrganisedEvent extends AppCompatActivity implements View.OnClickListener {

    Context context;
    UserSessionManager session;
    ImageView imageCalender;
    ImageView imageMap;
    ImageView imageTime;
    ImageView imageGallery;
    ImageView imageDuration;
    TextView textEventLocation;
    TextView textEventDate;
    TextView textEventTime;
    TextView textEventDuration;
    EditText eventName;
    EditText eventDescription;
    EditText eventVedioUrl;
    Spinner eventCategory;
    CatLoadingView mView;
    Button bUpdate, bDelete;
    CatLoadingView mview;

    //    private Spinner usertypeSpinner;

    private int event_id;
    private String name;
    private String time;
    private String venue;
    private String details;
    private int usertype_id;
    private String usert_ype_id;
    private String usertype;
    private int creator_id;
    private String creator;
    private int category_id;
    private String category;
    private ArrayList<Medais> images;
    private String videos;
    private String duration;
    String[] imgs = new String[5];
    private ViewGroup mSelectedImagesContainer;
    private static String user_id;
    RequestQueue queue;
    List<Medais> medias = new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;
    RecyclerView previousimg;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_organised_event);
        mView = new CatLoadingView();
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        previousimg = (RecyclerView) findViewById(R.id.oldimg);
        mView = new CatLoadingView();
        imageTime = (ImageView) findViewById(R.id.imageTime);
        imageCalender = (ImageView) findViewById(R.id.imagecalender);
        imageMap = (ImageView) findViewById(R.id.imageMap);
        imageGallery = (ImageView) findViewById(R.id.imageGallery);
        imageDuration=(ImageView)findViewById(R.id.imageDuration);
        textEventDuration=(TextView)findViewById(R.id.texEventDuration);
        imageTime.setOnClickListener(this);
        imageCalender.setOnClickListener(this);
        imageMap.setOnClickListener(this);
        imageGallery.setOnClickListener(this);
        imageTime.setOnClickListener(this);
        imageDuration.setOnClickListener(this);
        imageCalender.setOnClickListener(this);
        textEventLocation = (TextView) findViewById(R.id.textEventLocation);
        textEventDate = (TextView) findViewById(R.id.textViewEventDate);
        textEventTime = (TextView) findViewById(R.id.texEventTime);
        eventName = (EditText) findViewById(R.id.evntName);
        eventDescription = (EditText) findViewById(R.id.evtDescription);
        eventVedioUrl = (EditText) findViewById(R.id.evntVediourl);
        eventCategory = (Spinner) findViewById(R.id.eventCategory);
        bUpdate = (Button) findViewById(R.id.bUpdate);
        bDelete = (Button) findViewById(R.id.bDelete);
        bUpdate.setOnClickListener(this);
        bDelete.setOnClickListener(this);
        session = new UserSessionManager(this);

        session = new UserSessionManager(getApplicationContext());
        if (session.checkLogin())
            finish();
        HashMap<String, String> user = session.getUserDetails();
        queue = Volley.newRequestQueue(context);
        user_id = user.get(UserSessionManager.KEY_USER_ID);

        Intent intent = getIntent();
        event_id = intent.getIntExtra("event_id", 0);
        name = intent.getStringExtra("name");
        time = intent.getStringExtra("time");
        venue = intent.getStringExtra("venue");
        details = intent.getStringExtra("details");
        usertype_id = intent.getIntExtra("usertype_id", 0);
        usertype = intent.getStringExtra("usertype");
        creator_id = intent.getIntExtra("creator_id", 0);
        creator = intent.getStringExtra("creator");
        category_id = intent.getIntExtra("category_id", 0);
        category = intent.getStringExtra("category");
        videos = intent.getStringExtra("videos");
        duration=intent.getStringExtra("duration");
        images = intent.getParcelableArrayListExtra("images");
        eventCategory = (Spinner) findViewById(R.id.eventCategory);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        eventName.setText(name);
        eventDescription.setText(details);
        textEventDate.setText(time);
        textEventTime.setText(time);
        eventVedioUrl.setText(videos);
        textEventDuration.setText(duration);
        mView = new CatLoadingView();
        usert_ype_id = user.get(UserSessionManager.KEY_USERTYPE_ID);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);

        textEventLocation.setText(venue);
        getImages(event_id, null);
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textEventDate.setText(DateFormat.getDateInstance().format(date));
        textEventTime.setText(new SimpleDateFormat("hh:mm aa").format(date));

        final String[] selectedDate = new String[1];
        selectedDate[0] = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();

        final String[] selectedTime = new String[1];
        selectedTime[0] = new SimpleDateFormat("HH:mm:ss").format(date).toString();

        final Calendar c = Calendar.getInstance();
        c.setTime(date);


        getcategory();

        textEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Event Name");
                final EditText input = new EditText(context);
                input.setText(textEventTime.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventDescription.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }
        });

        textEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Event Name");
                final EditText input = new EditText(context);
                input.setText(textEventTime.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventDescription.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }
        });
        textEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Venue");
                final EditText input = new EditText(context);
                input.setText(textEventLocation.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textEventLocation.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }
        });
        eventDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Details");
                final EditText input = new EditText(context);
                input.setText(eventDescription.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventDescription.setText(input.getText().toString());

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();

            }
        });

        imageCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.YEAR, year);
                                    cal.set(Calendar.MONTH, monthOfYear);
                                    cal.set(Calendar.DATE, dayOfMonth);
                                    Date selected = cal.getTime();
                                    textEventDate.setText(DateFormat.getDateInstance().format(selected));


                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);


                                    formatter = new SimpleDateFormat("yyyy-MM-dd");

                                    selectedDate[0] = formatter.format(date).toString();


                                } catch (Exception ex) {

                                }


                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dd.show();
            }
        });

        imageTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog td = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    cal.set(Calendar.MINUTE, minute);
                                    cal.set(Calendar.SECOND, 0);
                                    Date selected = cal.getTime();

                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    textEventTime.setText(new SimpleDateFormat("hh:mm aa").format(selected));


                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                    java.sql.Time timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    selectedTime[0] = String.valueOf(timeValue);

                                } catch (Exception ex) {
                                }
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        android.text.format.DateFormat.is24HourFormat(context)
                );
                td.show();
            }
        });


        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new LovelyStandardDialog(EditOrganisedEvent.this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                        .setTopColorRes(R.color.style_color_primary)
                        .setButtonsColorRes(R.color.style_color_primary)
                        .setIcon(R.drawable.ic_info_outline_white_36dp )
                        .setTitle("Are you sure?")
                        .setMessage("Changes will save  !")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteEvent(selectedDate, selectedTime);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();




            }
        });


        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new LovelyStandardDialog(EditOrganisedEvent.this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                        .setTopColorRes(R.color.primary_light)
                        .setButtonsColorRes(R.color.style_color_primary)
                        .setIcon(R.drawable.ic_info_outline_white_36dp )
                        .setTitle("Are you sure?")
                        .setMessage("Won't be able to recover this file!")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editEvent();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();





            }
        });
    }

    private void deleteEvent(String[] selectedDate, String[] selectedTime) {
        name = eventName.getText().toString().trim();
        venue = textEventLocation.getText().toString().trim();
        category_id = eventCategory.getSelectedItemPosition() ;
        details = eventDescription.getText().toString().trim();
        time = selectedDate[0] + " " + selectedTime[0];
        videos = eventVedioUrl.getText().toString();
        if (name.equals("")) {
            Toast.makeText(context, "Please specify Name", Toast.LENGTH_SHORT).show();
        } else if (venue.equals("")) {
            Toast.makeText(context, "Please specify Venue", Toast.LENGTH_SHORT).show();
        } else if (details.equals("")) {
            Toast.makeText(context, "Please specify Details", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
            StringRequest registerRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.updateEvent_url),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Log.d("response", Utils.fromHtml(response));
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {

                                    hideDialog();
                                    Toast.makeText(context, "Event " + name + " was updated Successfully ", Toast.LENGTH_SHORT).show();
                                    onBackPressed();


                                } else {
                                    hideDialog();
                                    Toast.makeText(context, "Event update Failed", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Toast.makeText(context, "Event update Failed", Toast.LENGTH_SHORT).show();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("user_id", user_id + "");
                    params.put("event_id", event_id + "");
                    params.put("category_id", category_id +"");
                    params.put("usertype_id", usert_ype_id +"");
                    params.put("venue", venue);
                    params.put("time", time);
                    params.put("details", details);
                    params.put("videos", videos+"");
                    params.put("duration", duration+"");
                    if (jsonArray != null) {
                        params.put("images", jsonArray.toString());
                        Log.d(getLocalClassName(), "getParams: " + jsonArray.toString());
                    }

                    return params;
                }

            };
            queue.add(registerRequest);
        }
    }

    @Subscribe
    public void getMessage(Events.FragmentActivityMessage fragmentActivityMessage) {

        textEventDuration.setText(fragmentActivityMessage.getMessage());
        duration=fragmentActivityMessage.getMessage();

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        GlobalBus.getBus().register(this);
    }
    private void editEvent() {
        showDialog();
        StringRequest registerRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.deleteEvent_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("response", response.toString());
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                hideDialog();
                                Toast.makeText(context, "Event " + name + " was deleted ", Toast.LENGTH_SHORT).show();

                                onBackPressed();


                            } else {

                                hideDialog();
                                Toast.makeText(context, "Event deletion Failed", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                hideDialog();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("event_id", event_id + "");
                params.put("usertype_id", usert_ype_id +"");
                return params;
            }

        };
        queue.add(registerRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
        GlobalBus.getBus().unregister(this);
    }


    OldImagesAdapter oldImagesAdapter;

    private void setupOldImgs(List<Medais> medais) {

        for (Medais data : medais) {
            if ("image".equals(data.getType())) {
                //TODO
            } else {
                eventVedioUrl.setText(data.getPath());
            }

        }

        previousimg.setLayoutManager(new GridLayoutManager(this, 3));

        oldImagesAdapter = new OldImagesAdapter(getApplicationContext(), medais, false, new Clickcb() {
            @Override
            public void onclick(Object object) {

               final  Object ob=object;
                if (object instanceof Medais) {

                    new LovelyStandardDialog(EditOrganisedEvent.this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                            .setTopColorRes(R.color.style_color_primary)
                            .setButtonsColorRes(R.color.style_color_primary)
                            .setIcon(R.drawable.ic_info_outline_white_36dp )
                            .setTitle("Are you sure?")
                            .setMessage("Won't be able to recover this file!")
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DeleteImage(((Medais)ob).getId());

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();




                }
            }
        });
        oldImagesAdapter.notifyDataSetChanged();
        previousimg.setAdapter(oldImagesAdapter);

    }


    public void DeleteImage(final int id) {
        showDialog();
        StringRequest registerRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.deleteEvent_Image),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("response", response.toString());
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                hideDialog();
                                Toast.makeText(context, "deleted ", Toast.LENGTH_SHORT).show();

                               getImages(event_id,null);

                            } else {

                                hideDialog();
                                Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                hideDialog();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id+"");
                params.put("imgId", id+"");


                return params;
            }

        };
        queue.add(registerRequest);

    }





    public void showLocationPicker() {
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(47.64299816, -122.14351988),
                new LatLng(47.64299816, -122.14351988));
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(latLngBounds);
        try {
            startActivityForResult(builder.build(EditOrganisedEvent.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.d("bugggggggggggggg", e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(intent, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                textEventLocation.setText(latitude+"\n"+longitude+"\n"+placename + "\n" + address);
            }
        }



        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && intent != null) {
            ArrayList<Image> images = intent.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0, l = images.size(); i < l; i++) {
                image_uris.add(images.get(i).path);
            }
            showUploaded();

        }

//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
//
//                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//
//                if (image_uris != null) {
//                    showMedia();
//                }
//
//
//            }
//        }
        }



    private void showUploaded() {
        jsonArray = new JSONArray();
        // Remove all views before
        // adding the new ones.
        mSelectedImagesContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        }

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());


        for (String uri : image_uris) {

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
//                jsonObject.put("image",Utils.getStringImage(bitmap));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            Glide.with(this)
                    .asBitmap().load(uri)
                    .into(new SimpleTarget<Bitmap>(250, 250) {


                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                jsonArray.put(new JSONObject().put("image", Utils.getStringImage(resource)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            Glide.with(this)
                    .load(uri.toString())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.noimg)
                            .fitCenter())
                    .into(thumbnail);


            mSelectedImagesContainer.addView(imageHolder);

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));


        }
    }


    private static final int REQUEST_CODE = 123;

    public void chooseImages() {

        image_uris.clear();

// start multiple photos selector
//        Intent intent = new Intent(EditOrganisedEvent.this, ImagesSelectorActivity.class);
//// max number of images to be selected
//        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
//// min size of image which will be shown; to filter tiny images (mainly icons)
//        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
//// show camera or not
//        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
//// pass current selected images as the initial value
//        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, image_uris);
//// start the selector
//        startActivityForResult(intent, REQUEST_CODE);

        Intent intent = new Intent(this, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
        startActivityForResult(intent, Constants.REQUEST_CODE);


    }


    private void getImages(final int eventId, final DisplayEventList.OncompleteCallaback oncompleteCallaback) {
        medias.clear();
        showDialog();
        StringRequest eventRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.get_media) + String.valueOf(eventId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("response", response.toString());
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {
                                medias.clear();
                                JSONArray array = jsonResponse.getJSONArray("media");
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject e = array.getJSONObject(i);
                                    Medais data = new Medais(e.getInt("id"),e.getInt("event_id"), e.getString("type"), e.getString("path"));//)//,e.getString("name"),e.getString("time"),e.getString("venue"),e.getString("details"),e.getInt("usertype_id"),e.getString("usertype"),e.getInt("creator_id"),e.getString("creator"),e.getInt("category_id"),e.getString("category"));
                                    medias.add(data);

                                }
                                if (oncompleteCallaback != null) {
                                    oncompleteCallaback.oncomplete();
                                }

                                setupOldImgs(medias);
                                hideDialog();

                            } else {

                                Toast.makeText(getApplicationContext(), "invalid response", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Toast.makeText(getApplicationContext(), "Error fetching image", Toast.LENGTH_LONG).show();


            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        queue.add(eventRequest);


    }

    int PLACE_PICKER_REQUEST = 1;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ArrayList<String> image_uris = new ArrayList<String>();


    JSONArray jsonArray;

    private void showMedia() {
        jsonArray = new JSONArray();
        // Remove all views before
        // adding the new ones.
        mSelectedImagesContainer.removeAllViews();
        if (imgs.length >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        }

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        int length = imgs.length;
        int i = 0;
        for (Medais medais : medias) {

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
//                jsonObject.put("image",Utils.getStringImage(bitmap));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Glide.with(this)
//                    .asBitmap().load(imgs[1])
//                    .into(new SimpleTarget<Bitmap>(250,250) {
//
//
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            try {
//                                jsonArray.put(new JSONObject().put("image", Utils.getStringImage(resource)));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });

            if ("image".equals(medais.getType())) {
                View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
                ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

                Glide.with(this)
                        .load(getResources().getString(R.string.base_url) + medais.getPath())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.noimg)
                                .fitCenter())
                        .into(thumbnail);


                mSelectedImagesContainer.addView(imageHolder);

                thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
            } else {
                eventVedioUrl.setText(medais.getPath());
            }

        }
    }


    private void getcategory() {
        //Creating a string request


        StringRequest categoryRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.categoryRegister_url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            jsonResponse = new JSONObject(response);
                            JSONArray result = jsonResponse.getJSONArray("category");
                            ArrayList<String> categorys = new ArrayList<String>();
                            for (int i = 0; i < result.length(); i++) {

                                //Getting json object
                                JSONObject obj = result.getJSONObject(i);

                                //Adding the name of the student to array list
                                categorys.add(obj.getString("name"));

                            }
                            eventCategory.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner, categorys));
                            eventCategory.setSelection(category_id);
                            //Calling method getStudents to get the students from the JSON Array
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue


        //Adding request to the queue
        queue.add(categoryRequest);

    }

    boolean isShowing=false;
    public void showDialog() {


        if(!isShowing) {
            try {

                mView.show(getSupportFragmentManager(), "");
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imageGallery:
                chooseImages();
                break;

            case R.id.imageMap:
                showLocationPicker();
                break;
            case R.id.imageDuration:
                showDurationPicekr();
                break;

        }

    }

    private void showDurationPicekr() {

        Events.ActivityFragmentMessage activityFragmentMessage =
                new Events.ActivityFragmentMessage(
                        duration);
        GlobalBus.getBus().post(activityFragmentMessage);

        new PickerDialogFragment().geInstance(new Clickcb() {
            @Override
            public void onclick(Object object) {

                if(object instanceof String)
                {
                    textEventDuration.setText(object.toString());
                }
            }
        }).show(getFragmentManager(), "dialog");
    }
}
