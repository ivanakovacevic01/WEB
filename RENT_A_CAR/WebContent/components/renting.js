Vue.component("rentACarObject", {
	data: function () {
		    return {
			  startDateTime:"",
			  endDateTime:"",
			  visibility:true,
			  vehicles:[],
			  basket:{vehicles:null,username:null,price:null},
			  usernameParam:"",
			  user:null,
			  basketId:"",
			  vehicle:null,
			  dateTimeVisibility:false,
			  orders:null,
			  orderDateList:null
		    }
	},
	
	template: ` 
	<div id="userProfile" class="objectsViewContainer" style="text-align:center">
	<div v-bind:hidden="dateTimeVisibility==true" style="margin-top: 20px">
		<label class="registrationLabel">Start date for order:</label>
		<input class="registrationInput" type="datetime-local" id="datum-vrijeme1" name="startDate" v-model = "startDateTime"><br>
		<label class="registrationLabel" style="margin-top: 15px">End date for order:</label>
		<input class="registrationInput"  style="margin-top: 15px" type="datetime-local" id="datum-vrijeme2" name="endDate" v-model = "endDateTime"><br>
		<input class="id1Submit" type="submit" v-on:click = "search" value="Search"><br></div>
		
		<div v-bind:hidden="visibility==true" style="text-align:center"><h1>Rent a car objects</h1>
			<table class="vehiclesTabel" align="center" style = "width: 95%; margin-top: 2px">
				<tr class="rowBackground">
					<th class = "th1Label">Brand</th>
					<th class = "th1Label">Model</th>
					<th class = "th1Label">Price</th>
					<th class = "th1Label">Type</th>
					<th class = "th1Label">Kind</th>
					<th class = "th1Label">Fuel type</th>
					<th class = "th1Label">Consumption</th>
					<th class = "th1Label">Doors number</th>
					<th class = "th1Label">Persons number</th>
					<th class = "th1Label">Status</th>
					<th class = "th1Label">Description</th>
					<th class = "th1Label">Image</th>
					<th class = "th1Label">Add to basket</th>
				</tr>
				<tr v-for = "(v, index) in vehicles" v-if = "v.removed!=true" class="rowBackground">
					<td style = "text-align: center"><label class="objects1Label">{{v.brand}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.model}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.price}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.type}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.kind}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.fuelType}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.consumption}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.doorsNumber}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.personNumber}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.status}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.description}}</label></td>
					<td><img class="objectsImage" :src="v.image"/></td>
					<td style = "text-align: center; vertical-align: center" >
						<div style="height: 100%;">
							<button class="confirmButton" v-on:click="add(v.id,v.quantity)">Add</button>
						</div>
					</td>
				</tr>
			</table>
			
			<button class="id1Submit" v-on:click="viewBasket">BASKET VIEW</button>
			</div>
	</div>		  
`
	, 
	methods : {
		
		
		search : function()
		{
			this.vehicles=[]
			var StartDateTime=new Date(this.startDateTime)
			var EndDateTime=new Date(this.endDateTime)
			var isValid=true;
			var newDate=new Date();
			
			if(document.getElementsByName("startDate")[0].value=="" || document.getElementsByName("endDate")[0].value==""){
				document.getElementsByName("startDate")[0].style.background='red';
				document.getElementsByName("endDate")[0].style.background='red';
				isValid=false;
			}
			else if(StartDateTime>EndDateTime || (StartDateTime.getMonth()<newDate.getMonth() && StartDateTime.getFullYear()==newDate.getFullYear()) || (StartDateTime.getMonth()==newDate.getMonth() && StartDateTime.getFullYear()==newDate.getFullYear() && StartDateTime.getDate()<newDate.getDate()) || (StartDateTime.getMonth()==newDate.getMonth() && StartDateTime.getFullYear()<newDate.getFullYear() && StartDateTime.getDate()==newDate.getDate())){
				document.getElementsByName("startDate")[0].style.background='red';
				document.getElementsByName("endDate")[0].style.background='red';
				isValid=false;
			}
			else
			{
				document.getElementsByName("startDate")[0].style.background='white';
				document.getElementsByName("endDate")[0].style.background='white';
			}
				
			if(isValid){
			
				this.visibility=false;
				axios
				.get('rest/vehicleService/')
				.then(response => {
					this.vehicles=response.data
					this.vehicles=this.vehicles.filter(v => v.removed==false)
					axios
					.get('rest/orderService/getOrdersByStatus') 
					.then(response => {
						this.orders = response.data
						  
						if(this.orders!="" && this.orders!=null)
						for(var order of this.orders){
							var startDate=new Date(order.startDateTime.split('T')[0]) //start date of rental
							var endDate=new Date(new Date(order.startDateTime).getTime() + order.duration * 1000) //end date of rental
							if(StartDateTime<=endDate && EndDateTime>=startDate){
								for(var vehicle of order.vehicles){
									this.vehicles = this.vehicles.filter(v => v.id!==vehicle.id)
									//this.vehicles = this.vehicles.filter(v => v.status!='Rented')
								}
							}
						}
						//removing rented vehicles
						axios
							.get('rest/orderService')
							.then(response =>
							{
								allOrders = response.data;
								if(allOrders!="" && allOrders!=null)
								{
									for(const ord of allOrders)
									{
										for(const veh of ord.vehicles)
										{
											var suggestedVehicle = this.vehicles.find(v => v.id==veh.id && veh.status=='Rented')
											if(suggestedVehicle)
											{
												this.vehicles = this.vehicles.filter(v => v.id != veh.id);
											}
										}
									}
								}
							})
						
						
					})
				})
				
				this.dateTimeVisibility=true;
			}
		},
		add:function(id,quantity){
		
			//dodaj validaciju za datume
			
			
			
			if(this.user.basket==null || this.user.basket==""){
				axios.post('rest/basketService/addBasket', this.basket)
									.then(response => 
									{
										if(response.data==null || response.data==""){
											alert("Creating is impossible!");
										}else{
											
											this.basket = response.data;	//to get new basket id
											axios
												.put('rest/basketService/edit/' + this.basket.id+'/'+id+'/'+this.usernameParam)
												.then(response => {
													this.user.basket=response.data;
													//this.user.basket.price=response.data.price;
													axios.put('rest/userService/'+this.usernameParam,this.user)
														 .then(response => 
																	{
																		if(response.data==null){
																			alert("Error!")
																		}else{
																			alert("Successfully added to basket.")
																		}
																	})
												})
											
										}
									})
			}else{
				axios
				.get('rest/basketService/getVehicleByBasket/' + this.user.basket.id + '/' + id)
				.then(response =>
					{
						this.vehicle=response.data
						//alert(this.vehicle)
						if(this.vehicle!=null || this.vehicle!=""){
							this.vehicle.quantity+=1
							axios
											.put('rest/basketService/edit/' + this.user.basket.id+'/'+id+'/'+this.usernameParam)
											.then(response => {
												this.user.basket=response.data;
												axios.put('rest/userService/'+this.usernameParam,this.user)
													 .then(response => 
																{
																	if(response.data==null){
																		alert("Error!")
																	}else{
																		alert("Successfully added to basket.")
																	}
																})
											})
						}else{
							
							axios
				.get('rest/basketService/usernameExists/' + this.usernameParam)
				.then(response =>
							{
								if(response.data==true)
									axios
									.get('rest/basketService/getByUser/' + this.usernameParam)
									.then(response => {
										this.basket=response.data
										if(this.basketId==null){
											alert("Error!")
										}else{
											axios
												.put('rest/basketService/edit/' + this.basket.id+'/'+id+'/'+this.usernameParam)
												.then(response => {
													this.user.basket=response.data;
													axios.put('rest/userService/'+this.usernameParam,this.user)
														 .then(response => 
																	{
																		if(response.data==null){
																			alert("Error!")
																		}else{
																			alert("Successfully added to basket.")
																		}
																	})
												})
										}
										
									})	
								else
									axios.post('rest/basketService', this.basket)
									.then(response => 
									{
										if(response.data==null || response.data==""){
											alert("Creating is impossible!");
										}else{
											
											this.basket = response.data;	//to get new basket id
											axios
												.put('rest/basketService/edit/' + this.basket.id+'/'+id+'/'+this.usernameParam)
												.then(response => {
													this.user.basket=response.data;
													//this.user.basket.price=response.data.price;
													axios.put('rest/userService/'+this.usernameParam,this.user)
														 .then(response => 
																	{
																		if(response.data==null){
																			alert("Error!")
																		}else{
																			alert("Successfully added to basket.")
																		}
																	})
												})
											
										}
									})
								
							})
						}	
					})
									
			}	
					
			
		},
		viewBasket : function()
		{
			router.push(`/basketView/${this.usernameParam}/${this.startDateTime}/${this.endDateTime}`);
		}
	},
	mounted()
	{
		/*const datumVremeInput = document.getElementById('datum-vrijeme1');
		const today = new Date();
		const year = today.getFullYear();
		let month = today.getMonth() + 1;
		let day = today.getDate();
		let hours = today.getHours();
		let minutes = today.getMinutes();
		
	
		month = (month < 10 ? "0" : "") + month;
		day = (day < 10 ? "0" : "") + day;
		hours = (hours < 10 ? "0" : "") + hours;
		minutes = (minutes < 10 ? "0" : "") + minutes;
		
		const formattedDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;
		
		datumVremeInput.value = formattedDateTime;
		datumVremeInput.min = formattedDateTime;
		
		const datumVremeInput2 = document.getElementById('datum-vrijeme2');
		datumVremeInput2.value = formattedDateTime;
		datumVremeInput2.min = formattedDateTime;*/
		
		this.usernameParam = this.$route.params.username;
		axios
			.get('rest/userService/getUserByUsername/' + this.usernameParam)
			.then(response => (this.user = response.data))		

		
	}
});