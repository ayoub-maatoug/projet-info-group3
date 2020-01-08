#include <string.h>
#include <SPI.h>
#include <WiFiNINA.h>
#include <Arduino_JSON.h>
#include <HttpClient.h>
#include <MQTT.h>

//On commence par initialiser les variables necessaires : wifi, mqtt, pins, etc.
WiFiClient client;
MQTTClient mqtt_client ;

#define POTENTIOMETER_PIN A2 
#define BUTTON_PIN 2

// le Status et le Light_id seront les 2 informations reçues par notre payload. 
/* Le light_Id représente la lampe dont on veut changer l'état et le Status représente le future état qu'on veut obtenir d'elle. 
Dans notre cas on ne fera des changement que sur l'état allumé ou éteint à partir de nos applis. Le changement du brightness se fera avec le potentiometre */
String ID = "999" ;
String LIGHT_ID = "11";
String Status = "ON";



const char* wifi_ssid     = "lab-iot-1";// TODO: change this
const char* wifi_password = "lab-iot-1"; // TODO: change this

// mqtt configuration change here
const char* mqtt_host = "max.isasecret.com" ;
const char* mqtt_user     = "majinfo2019";
const char* mqtt_password = "Y@_oK2";
const uint16_t mqtt_port =  1723;

/* Some variables to handle measurements. */
int potentiometerValue;
int buttonState = 0;         // variable for reading the pushbutton status


uint32_t t0, t ;

//Ici on initialize une valeur de temps qui définie le lapse de temps pour lequel l'arduino vérifie si des changements ont été faits au niveau du bouton et du potentiometre
#define DELTA_T 500
//StaticJsonBuffer<200> jsonBuffer;

void setup() {
  // monitoring via Serial is always nice when possible
  Serial.begin(9600) ;
  delay(100) ;
  Serial.println("Initializing...\n") ;

  //On commence par se connecter au wifi
  WiFi.begin(wifi_ssid, wifi_password);

  //Ici on lance le mqtt client
  mqtt_client.begin(mqtt_host, mqtt_port, client) ;
  //Ici on demande à notre mqtt client d'afficher le payload lorsqu'il recevra un message, ceci se fait grâce à la fonction callback() et la fonction mqtt-client.loop()
  mqtt_client.onMessage(callback) ;

  //Cette fonction permet de se connecter au topic souhaité
  connect();
  // initialize the Potentiometer and button pin as inputs:
  pinMode(POTENTIOMETER_PIN, INPUT);
  pinMode(BUTTON_PIN, INPUT);
  
  // Time begins now!
  t0 = t = millis() ;
  }
  
  
boolean isOn = false;
int oldValue = 0;
  

void loop() {  
  //Cette fonction permet de mettre à jour les informations reçues par le mqtt client
  mqtt_client.loop() ;
  
  //Ici on relance la connection grâce à la fonction connect(), s'il y a un problème de connection
  if (!mqtt_client.connected()) {
    connect();
  }
  
  t = millis() ;
 // Ici on vérifie le bouton et le potentiometre chaque 0.5 secondes. Ceci est necessaire pour pas que l'arduino ne traite trop de changements successifs et se mettre à allumer et éteindre la lampe successivement. 
  if ( (t - t0) >= DELTA_T ) {
      t0 = t ;
      buttonState = digitalRead(BUTTON_PIN);
      Serial.println(buttonState);
      potentiometerValue = analogRead(POTENTIOMETER_PIN); 
      if(oldValue <= (potentiometerValue - 10) || oldValue >= (potentiometerValue + 10)){
            //Ici on effectue des changements sur le brightness si les changements dépassent la valeur de 10
            String briSt = String(potentiometerValue);
            //Cette fonction permet d'effectuer les changements necessaires concernant le brightness
            changeBrightness(briSt);
            oldValue = potentiometerValue;
    }
    if(buttonState == 1){
      //ici on lance une fonction qui permet de faire les changements necessaires lorsqu'on clique sur le bouton        
        httpRequest();
    }    
  }
}

char server[] = "192.168.1.131";

/*La fonction suivante permet d'allumer la lampe en envoyer une requête http à l'api de la lampe pour lui demander de s'allumer.
Pas besoin de notifier notre API puisque cette fonction ne se lance qu'une fois que l'API a changé d'état.
Notre API est déjà au courrant de l'état qu'on veut obtenir*/
void turnOn(){
  Serial.println("Trying to turn ligh on");
  if(client.connect(server, 80)){
    Serial.println("connected !");
     client.println("PUT /api/LDd1NE7AhgJ6bJ-SZ-g1zCkydqssE2wd5Lwe7lMU/lights/"+LIGHT_ID+"/state HTTP/1.1");
     client.println("Host: 192.168.1.131"  );                          
     client.println("Connection: close");
     client.println("Content-Type: application/x-www-form-urlencoded");
     client.println("Content-Length: 11\r\n");
     client.print("{\"on\":true}");
     
  }
  delay(100);
  while (client.available()) {
    String line = client.readStringUntil('\r');
      Serial.print(line);
   }
}

//Cette fonction permet d'éteindre la lampe.
void turnOff(){
  Serial.println("Trying to turn ligh off");
  if(client.connect(server, 80)){
    Serial.println("connected !");
     client.println("PUT /api/LDd1NE7AhgJ6bJ-SZ-g1zCkydqssE2wd5Lwe7lMU/lights/"+LIGHT_ID+"/state HTTP/1.1");
    client.println("Host: 192.168.1.131");                          
     client.println("Connection: close");
     client.println("Content-Type: application/x-www-form-urlencoded");
     client.println("Content-Length: 12\r\n");
     client.print("{\"on\":false}");
     
  }
}

//Ici on change le brightness de la lampe
void changeBrightness(String bri){
  Serial.print("Changing brightness...");
  Serial.println(bri);
  if(client.connect(server, 80)){
    Serial.println("connected !");
     client.println("PUT /api/LDd1NE7AhgJ6bJ-SZ-g1zCkydqssE2wd5Lwe7lMU/lights/"+LIGHT_ID+"/state HTTP/1.1");
     client.println("Host: 192.168.1.131");                          
     client.println("Connection: close");
     client.println("Content-Type: application/x-www-form-urlencoded");
     int str_length = 19 + bri.length();
     String len = String(str_length);
     String content = "Content-Length: "+len+"\r\n";
     client.println(content);
     String body = "{\"on\":true,\"bri\":"+bri+"}";
     client.print(body);
  }
}

/*/Cette fonction permet de modifier l'état de la lampe après avoir appuyé sur le bouton. 
 * La fonction se charge en fait de recevoir les informations concernant la lampe et demande à une autre fonction 
de traiter les résultats reçu pour allumer ou eteindre la lampe*/
void httpRequest(){
   Serial.println("\nStarting connection to server...");
  // if you get a connection, report back via serial:
  if (client.connect(server, 80)) {
      Serial.println("connected to server");
      // Make a HTTP request:
      client.println("GET /api/LDd1NE7AhgJ6bJ-SZ-g1zCkydqssE2wd5Lwe7lMU/lights/"+LIGHT_ID+" HTTP/1.1");
      client.println("Host: 192.168.1.131");
      //client.println("Content-type: application/json");
      
      client.println("Connection: keep-alive");
      client.println();
  
   }
    delay(100);
    String line= "";
    while (client.available()) {
       line = client.readStringUntil('\n');
   }
   
    //Cette fonction permet alors de traiter le JSON reçu afin de modifier l'état de la lampe
    demoParse(line);
    delay(100);
    Serial.print(line);
  
}

//Cette fonction permet de parser/traiter le JSON reçu afin de modifier l'état de la lampe
void demoParse(String input) {
  Serial.println("parse");
  Serial.println("=====");

  JSONVar myObject = JSON.parse(input);

  // JSON.typeof(jsonVar) can be used to get the type of the var
  if (JSON.typeof(myObject) == "undefined") {
    Serial.println("Parsing input failed!");
    return;
  }

  Serial.print("JSON.typeof(myObject) = ");
  Serial.println(JSON.typeof(myObject)); // prints: object

  // myObject.hasOwnProperty(key) checks if the object contains an entry for key
  if (myObject.hasOwnProperty("state")) {
    JSONVar state_var = myObject["state"];
    Serial.print("state_var[\"on\"] = ");
    if((bool)state_var["on"] == true){
      Serial.println("The lamp is on !");
      //Si la lampe est allumé alors on éteint la lampe puis on notifie le changement à notre API en envoyant une requête HTTP PUT
      turnOff();
      client.println("PUT /api/lights/1/switch HTTP/1.1");
      client.println("Host: ayoub-springlab.cleverapps.io");

    }
    
  else{
      Serial.println("The lamp is off !");
      // Si la lampe est éteinte alors on allume la lampe puis on notifie le changement à notre API en envoyant une requête HTTP PUT
      turnOn();
      client.println("PUT /api/lights/1/switch HTTP/1.1");
      client.println("Host: ayoub-springlab.cleverapps.io");
      

    }
    
  }


  Serial.println();
}

//Cette fonction permet de se connecter au wifi et au mqtt server afin de recevoir les topics
void connect() {
  
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(1000);
  }

  Serial.print("\nconnecting...");
  while (!mqtt_client.connect("999", mqtt_user, mqtt_password)) {
    Serial.print(".");
    delay(1000);
  }
  
  Serial.print("MQTT: trying to connect to host ") ;
  Serial.print(mqtt_host) ;
  Serial.print(" on port ") ;
  Serial.print(mqtt_port) ;
  Serial.println(" ...") ;


  Serial.println("MQTT: connected") ;

  //Ici on précise que notre topic est SwitchLight
   mqtt_client.subscribe("SwitchLight");

  //Ici on précise qu'il faut utiliser la méthode callback lorsqu'une information a été reçu du mqtt server avec le topic indiqué
   mqtt_client.onMessage(callback);
    Serial.print("\n");
    Serial.print("WiFi connected\n");
    Serial.print("IP address: \n");
    Serial.print(WiFi.localIP());
    Serial.print("\n") ;
}

//Ici on précise quoi faire lorsqu'on reçoi le payload
void callback(String &intopic, String &payload){
  /* Le payload est de cette forme "11:ON", c'est à dire qu'on a l'id de la lampe à gauche du ":" et l'état qu'on souhaite à voir à droite.
   *   On modifie donc les variables initialisés en début de code afin d'effectuer les bons changements necessaires.
   */
      Serial.print("received");
      Serial.println("incoming: " + intopic + " - " + payload);

      //La fonction getValue permet de séparer notre string
      String xval = getValue(payload, ':', 0);
      String yval = getValue(payload, ':', 1);
      LIGHT_ID = xval;
      Status = yval;
      if (Status == "ON") {
        //On allume la lampe si l'information reçu pour le status est "ON"
         turnOn();
      }
      else if (Status == "OFF") {
        turnOff();
      }
    }

//Ici on déchire notre string bien sérré en 2
String getValue(String data, char separator, int index){
    int found = 0;
    int strIndex[] = { 0, -1 };
    int maxIndex = data.length() - 1;

    for (int i = 0; i <= maxIndex && found <= index; i++) {
        if (data.charAt(i) == separator || i == maxIndex) {
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i+1 : i;
        }
    }
    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}
