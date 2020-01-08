package com.example.faircorpapp.HttpManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.faircorpapp.RoomActivity;
import com.example.faircorpapp.ContextState.RoomContextState;


//Dans cette classe on a parser un JSON object, qui correspond aux informations concernant une room, et les utiliser dans la RoomActivity.
public class LightContextHttpManager {

    private static boolean PUT_REQUEST = false;
    private static Integer lightUpdateId;

    private LightContextHttpManager(final RoomActivity roomActivity) {


        String CONTEXT_SERVER_URL = "http://ayoub-springlab.cleverapps.io/api/";
        String url = CONTEXT_SERVER_URL + "rooms/" + roomActivity.roomId;
        RequestQueue queue = Volley.newRequestQueue(roomActivity.getApplicationContext());


        //ici on définie une condition qui permet de savoir si on lance ou non une requete put, cette condition est traité dans la méthode StartPut définie plus bas.
        if (PUT_REQUEST) {

            PUT_REQUEST = false;
            String url_put = CONTEXT_SERVER_URL + "lights/" + lightUpdateId.toString() + "/switch";
            //set room values
            JsonObjectRequest lightUpdateRequest =
                    new JsonObjectRequest(Request.Method.PUT, url_put, null,null,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Some error to access URL : Room may not exists...
                                    System.out.println("put error");
                                }
                            }
                    );
            queue.add(lightUpdateRequest);

        }


        //Ici on obtient d'autres informations concernant la room tel que le floor et les lumières associées
        JsonObjectRequest roomsRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    int roomFloor = Integer.parseInt(response.get("floor").toString());
                                    JSONArray roomLights = response.getJSONArray("lights");
                                    RoomContextState room = new RoomContextState(roomActivity.roomId, roomActivity.roomName, roomFloor, roomLights);

                                    //On prévient alors la roomActivity des nouvelles informations obtenues
                                    roomActivity.updateContext(room);

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Some error to access URL : Room may not exists...
                        System.out.println("get error");

                    }
                }
                );
        queue.add(roomsRequest);
    }


    //Cette méthode permet de lancer le lightcontexthttpmanager et est utilisée dans la roomActivity
    public static void start(RoomActivity activity) {

        new LightContextHttpManager(activity);

    }


    //cette méthode permet de savoir si une requête put doit être lancé afin de changer l'état de la lampe ou non. Elle est utilisée dans la roomactivity
    public static void startPut(RoomActivity activity, Integer lightId) {
        lightUpdateId = lightId;
        PUT_REQUEST = true;
        new LightContextHttpManager(activity);

    }
}