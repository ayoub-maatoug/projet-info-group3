package com.example.faircorpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.faircorpapp.adapter.MapEntryListAdapter;
import com.example.faircorpapp.HttpManager.RoomContextHttpManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Ici on initialise nos différentes variables.
    ListView rooms;
    LinkedHashMap<String, Integer> roomList; //On initialise un LinkedHashMap car c'est de cette façon que nous allons récupérer la liste des rooms.
    public static final String EXTRA_ROOM = "com.example.faircorpapp.ROOM"; //Ici on initialize un nom que nous utiliserons pour le intent afin de communiquer avec l'activité "Room activity"
    public static final String EXTRA_ID = "com.example.faircorpapp.ID"; //idem
    RoomContextHttpManager httpManager;

    private List<Map.Entry<String, Integer>> mListOfMapEntries;  // Ceci va nous servir à convertir notre LinkedHashMap en adapter afin de générer notre listview
    private MapEntryListAdapter mMapEntryListAdapter;  // Ici on initialise notre adapter


    //Cette classe sera instanciée après avoir récupéré les infos des rooms dans le "RoomContexthttpmanager". Elle permet de donner les valeurs aux variables initialisées.
    public void updateActivity (final LinkedHashMap<String, Integer> roomList) {
        this.roomList = roomList;
        mListOfMapEntries = new ArrayList<>(this.roomList.entrySet());    // Ici on créée la liste qu'on va convertir en adapter
        mMapEntryListAdapter = new MapEntryListAdapter(this, mListOfMapEntries); //Ici on convertie notre liste en un adapter grâce à la classe mentionné
        //Ici on crée notre ListView après avoir généré notre Adapter
        rooms.setAdapter (mMapEntryListAdapter);

        //Après avoir céée la listView on précise ce qu'il se passe une fois qu'on clique sur une des lignes
        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Ici on commence par préciser sur quelle room on appuie afin de donner ces informations à la RoomActivity pour qu'il puisse afficher les informations de la bonne room
                String selectedRoom = mMapEntryListAdapter.getItem(position).getKey();
                Integer selectedRoomId = roomList.get(selectedRoom);

                //Ici on va prévenir la RoomActivity sur quelle ligne de contenue on appuuie afin d'avoir l'affichage associé à la bonne chambre
                Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                intent.putExtra(EXTRA_ROOM, selectedRoom);
                intent.putExtra(EXTRA_ID, selectedRoomId);
                startActivity(intent);
            }
        });

       /* mListOfMapEntries.clear ();
        mListOfMapEntries.addAll (roomList.entrySet());
        mMapEntryListAdapter.notifyDataSetChanged ();
*/
        //for (String name : this.roomList.keySet()) {
           // System.out.println(name);
       // }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ici on relie l'activité à la vue voulue
        setContentView(R.layout.new_new_activity_main);
        //Ici on lance le RoomConexthttpmanager grâce à la méthode start(),
        RoomContextHttpManager.start(MainActivity.this);
        rooms = (ListView)findViewById(R.id.ListView);

  /*      RoomContextHttpManager.start(this);
        httpManager = new RoomContextHttpManager(this);
        httpManager.start(this);
        httpManager.RetrieveListRoom();
*/
        // Define the actions linked to the REQUEST ROOMS BUTTON
        // This allows us to update the view for the names of the rooms
        ((Button) findViewById(R.id.refreshButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomContextHttpManager.start(MainActivity.this);

            }

        });


        // Define the actions linked to each row of the list view
        // This allows us to navigate to another activity where all the information about the selected room are regrouped


    }


}
