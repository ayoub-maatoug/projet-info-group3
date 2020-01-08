new Vue({
  el: '#app',
  data: {
  	  rooms: [],
      //info: [],
      loading: true,
      errored: false,
      selectedRoom: 0,
      selectedLight: 0

  },
  mounted () {
    axios
      .get('http://ayoub-springlab.cleverapps.io/api/rooms')
      .then(response => {
        this.rooms = response.data

      },
      )
      .catch(error => {
        console.log(error)
        this.errored = true
      })
      .finally(() => this.loading = false)
  },


	methods:{
	  fonc (light){   /*Ici on utilise une fonction qui permet de faire un PUT pour changer l'état
      de la lumière et qui traite la réponse, qui est du même format que la réponse précédente, afin de mettre à jour la page*/
      this.selectedLight= light;
      let post_url="http://ayoub-springlab.cleverapps.io/api/lights/"+ light.id +"/switch";
      //let post_url= "https://thawing-journey-78988.herokuapp.com/api/rooms"+ "/"+ room.id +"/switch-light-and-list";
      axios.put(post_url,{lightId: light.id})
      	   .then(response=> {this.rooms=response.data});


    }


	 }
	}

)
