package com.example.faircorpapp.HttpManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.faircorpapp.MainActivity;

import java.util.LinkedHashMap;


public class RoomContextHttpManager {


   private RoomContextHttpManager(final MainActivity mainActivity) {

            //Ici on va initialiser les volleys afin de parser les JSONarraysgrâce aux méthodes du volley
            String url = "  ";
            RequestQueue queue = Volley.newRequestQueue(mainActivity.getApplicationContext());


        //Ici on précise qu'on va obtenir un JsonArray que l'on va ensuite traiter à la suite d'une requête http de type get
            JsonArrayRequest roomsRequest =
                    new JsonArrayRequest(Request.Method.GET, url, null,

                            new Response.Listener<JSONArray>() {
                                @Override

                                public void onResponse(JSONArray response) {

                                    try {
                                        //On précise sous quelle type de formAt on veut récupérer les informations
                                        LinkedHashMap<String,Integer> roomList= new LinkedHashMap<>();

                                        //Ici on parcourIR le JSON array et y récupérer les informations désirées, i.e le nom de la chambre et son ID
                                        for (int i = 0; i < response.length(); i++) {
                                            Integer roomId = Integer.parseInt(response.getJSONObject(i).get("id").toString());
                                            System.out.println(roomId);
                                            String roomName = response.getJSONObject(i).getString("name");
                                            System.out.println(roomName);
                                            roomList.put(roomName,roomId);



                                        }


                                        // Ici on met à jours les valeurs initialisées dans le mainActivity grâce à son constructeur
                                        mainActivity.updateActivity(roomList);


                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                // Some error to access URL : Room may not exists...
                                System.out.println("error ta mere");

                            }
                    }
                    );
            queue.add(roomsRequest);
    }

    //Ici on utilise une méthode qui permet de lancer la lecture du JSON et qui est utilisée dans le main activity
    public static void start(MainActivity activity) {

        new RoomContextHttpManager(activity);

    }
}