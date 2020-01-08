package com.example.faircorpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.faircorpapp.ContextState.RoomContextState;
import com.example.faircorpapp.HttpManager.LightContextHttpManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {

    public String roomName;
    public Integer roomId;
    public ArrayList<Integer> lightIdList = new ArrayList<>();
    TableLayout roomDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ici on relie l'activité à la vue voulue
        setContentView(R.layout.activity_room);

        //Ici on récupère les informations envoyées par la mainActivity afin de les traiter.
        Intent intent = getIntent();
        roomName = intent.getStringExtra(MainActivity.EXTRA_ROOM);
        roomId = intent.getIntExtra(MainActivity.EXTRA_ID,0);

        //Ici on rentre les valeurs qu'on a obtenue dans la vue roomName afin de voir le nom de la room sur laquelle on a cliqué
        ((TextView) findViewById(R.id.roomName)).setText(roomName);
        ((TextView) findViewById(R.id.roomName)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        //Ici on instancie la classe LightContexthttpmanager afin d'obtenir les informations concernant chaque room.
        LightContextHttpManager.start(this);

        //Le bouton refresh permet de mettre à jour les données sur les lampes en cas de changement
        ((Button) findViewById(R.id.refreshButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LightContextHttpManager.start(RoomActivity.this);
            }
        });
    }

    //Cette méthode récupère les informations sur chaque room et les traite pour bien les afficher
    public void updateContext(RoomContextState room) throws JSONException {

        //Ici on commence par créer différentes vues sur notre activty
        roomDisplay = (TableLayout) findViewById(R.id.roomTable);
        roomDisplay.removeAllViews();

        //On entre le numéro de l'étage qui a été récupéré grâce à la classe lightcontexthttpmanager
        TextView roomFloor = findViewById(R.id.roomFloor);
        roomFloor.setText(String.format("Room floor : %d", room.getRoomFloor()));
        roomFloor.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        //On créer une ligne qui va être remplie de colonnes. Chaque colonne représentera les informations concernant une lampe.
        //La première ligne créée va définir les informations que l'on verra affichées en dessous d'elle.
        TableRow lightTitle = new TableRow(this);
        lightTitle.setGravity(Gravity.CENTER);
        // Space between columns (applied to every child)
        android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();
        //la méthode dpToPixel est définie plus bas et permet de convertir les dp en pixel
        p.rightMargin = dpToPixel(20, this);
        p.bottomMargin = dpToPixel(10, this);
        p.topMargin = dpToPixel(10, this);

        //On créée une colonne qui affichera les ID de chacune des lampes présente dans la room
        TextView lightIdColumn = new TextView(this);
        lightIdColumn.setLayoutParams(p);
        lightIdColumn.setText("Light ID");
        lightIdColumn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        lightTitle.addView(lightIdColumn);

        //On créée une colonne qui affichera le level de chacune des lampes présente dans la room
        TextView lightLevelColumn = new TextView(this);
        lightLevelColumn.setLayoutParams(p);
        lightLevelColumn.setText("Level");
        lightLevelColumn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        lightTitle.addView(lightLevelColumn);


        //On créée une colonne qui affichera le Status de chacune des lampes présente dans la room
        TextView lightStatusColumn = new TextView(this);
        lightStatusColumn.setLayoutParams(p);
        lightStatusColumn.setText("Status");
        lightStatusColumn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        lightTitle.addView(lightStatusColumn);


        // On va ensuite définir une vue qui permet d'afficher les informations de chaque lampe sous la colonne correspondante
        roomDisplay.addView(lightTitle);
        //On va ensuite parcourir la room, qui est object de type roomcontextstate,
        // et récupérer les informations concernant ses lampes afin de les afficher dans les colonnes apparties
        for (int i = 0; i< room.getLights().length(); i++) {

            TableRow lightInformation = new TableRow(this);
            lightInformation.setGravity(Gravity.CENTER);
            JSONObject roomLight = room.getLights().getJSONObject(i);

            TextView lightIDValue = new TextView(this);
            lightIDValue.setGravity(Gravity.CENTER);
            lightIDValue.setLayoutParams(p);
            lightIDValue.setText(roomLight.get("id").toString());
            lightIDValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            lightIdList.add(Integer.parseInt(roomLight.get("id").toString()));
            lightInformation.addView(lightIDValue);

            TextView lightLevelValue = new TextView(this);
            lightLevelValue.setGravity(Gravity.CENTER);
            lightLevelValue.setLayoutParams(p);
            lightLevelValue.setText(roomLight.get("level").toString());
            lightLevelValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            lightInformation.addView(lightLevelValue);

            ImageView lightStatusImage = new ImageView(this);
            lightStatusImage.setLayoutParams(p);
            lightStatusImage.setClickable(true);
            String lightStatusValue = roomLight.get("status").toString();
            if (lightStatusValue.equals("ON")) {
                lightStatusImage.setImageResource(R.drawable.ic_bulb_on);
            } else {
                lightStatusImage.setImageResource(R.drawable.ic_bulb_off);
            }
            final int index = i;

            //Ici on précise quelle méthode utilisée lorsqu'on clique sur la lumière afin de changer la lampe.
            lightStatusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //On utilise la méthode startPut() de la classe LightContexthttpmanager afin de lancer la requête http put qui permet de modifier le statut d'une lampe.
                    // Il faudra ensuite cliquer sur refresh pour avoir le nouvel état
                    LightContextHttpManager.startPut(RoomActivity.this, lightIdList.get(index));
                }
            });
            lightInformation.addView(lightStatusImage);

            roomDisplay.addView(lightInformation);

        }
    }

    //Ici on créer une méthode pour convertir les dp en pixels, elle est utilisée pus haut
    private static Float scale;
    public static int dpToPixel(int dp, Context context) {
        if (scale == null)
            scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) dp * scale);
    }
}
