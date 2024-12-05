Vue.component("viewBasket", {
	data: function () {
		    return {
			  usernameParam:"",
			  user:{basket: {vehicles: {}}, orders:{}},
			  order:{id:"", vehicles: {}, object: {vehicles: {}, location:{}},objectId: null, startDateTime: "", duration:null, price:null, customer:"", status:"", customerUsername:""},
			  startDateTime: "",
			  endDateTime: "",
			  duration: null,
			  canClick: true,
			  orders:[],
			  types:[],
			  points:null,
			  type:null,
			  forGold:null,
			  forSilver:null,
			  forBronze:null,
			  newOrders:[]
		    }
	},
	
	template: ` 
	<div>
			<div style="text-align: center">
				<h2>My basket</h2>
				<button v-on:click="reserve" :disabled="this.canClick!=true" class="loginButton">Reserve</button>
				<table align="center" style = "width: 95%; margin-top: 2px" v-if="user && user.basket && user.basket.vehicles" class="vehiclesTabel">
					<tr class="rowBackground">
						<td colspan = "14" style = "text-align: left">
							<label class="objects2Label">Total price: {{user.basket.price}} EUR</label>
						</td>
					</tr>
					<tr class="rowBackground">
						<th class = "th1Label">Brand</th>
						<th class = "th1Label">Model</th>
						<th class = "th1Label">Price</th>
						<th class = "th1Label">Type</th>
						<th class = "th1Label">Kind</th>
						<th class = "th1Label">Fuel type</th>
						<th class = "th1Label">Consumption</th>
						<th class = "th1Label">Doors</th>
						<th class = "th1Label">Capacity</th>
						<th class = "th1Label">Status</th>
						<th class = "th1Label">Description</th>
						<th class = "th1Label">Image</th>
						<th class = "th1Label">Change quantity</th>
						<th class = "th1Label">Remove</th>
					</tr>
					<tr class="rowBackground" v-for = "(v, index) in user.basket.vehicles" v-if = "v.removed!=true">
						<td style = "text-align: center"><label class="objects1Label">{{v.brand}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.model}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.price}} EUR</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.type}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.kind}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.fuelType}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.consumption}} l/100 km</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.doorsNumber}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.personNumber}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.status}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.description}}</label></td>
						<td style = "text-align: center"><img class="objectsImage" :src="v.image"/></td>
						<td style = "text-align: center"><input type="number"  class = "gradeInput" v-model="v.quantity" :name="'quantityInput' + v.id">
						<button class="confirmButton" v-on:click="changeQuantity(v.id, v.quantity)">Confirm</button></td>
						<td style = "text-align: center">
							<div style="height: 100%;">
								<button class="confirmButton" v-on:click = "remove(v.id, v.quantity)">Remove</button>
							</div>
						</td>
					</tr>
				</table>
		</div>
	</div>		  
`
	, 
	methods : {
		remove : function(id, quantity)
		{
			
				if(this.user.basket!=null && this.user.basket!="")
						axios
							.put('rest/basketService/removeVehicle/' + this.user.basket.id+'/'+id+'/'+this.usernameParam + '/' + quantity)
							.then(response => {
								this.user.basket=response.data;
								axios.put('rest/userService/'+this.usernameParam,this.user)
									 .then(response => 
												{
													if(response.data==null){
														alert("Error!")
													}else{
														alert("Successfully removed from basket.")
													}
												}
											)
										}
								)
			else
				alert("No basket.")
		
			
			
							
					

		},
		changeQuantity : function(id, quantity)
		{
			//validacija za kolicinu
			if(quantity<1)
			{
				alert("Quantity needs to greater then 0.");
				//vratiti na staru
				axios
										.get('rest/basketService/getVehicleByBasket/' + this.user.basket.id + '/' + id)
										.then(response =>
										{
											if(response.data=="" || response.data==null)
											{
												alert("Error")
											}
												
											else
												{
													nameInput = "quantityInput" + id;
													document.getElementsByName(nameInput)[0].value = response.data.quantity;
													//update basket
													axios
														.get('rest/basketService/'+this.user.basket.id)
														.then(response =>
															(this.user.basket=response.data)
														)
													
												}
										})
			}
				
			else
			{
				if(this.user.basket!=null && this.user.basket!="")
						axios
							.put('rest/basketService/changeQuantity/' + this.user.basket.id+'/'+id+'/'+this.usernameParam + '/' + quantity)
							.then(response => {
								if(response.data=="" || response.data==null)
								{
									alert("Invalid changing of quantity.");
									//vratiti na staru
									axios
										.get('rest/basketService/getVehicleByBasket/' + this.user.basket.id + '/' + id)
										.then(response =>
										{
											if(response.data=="" || response.data==null)
												alert("Error")
											else
												{
													nameInput = "quantityInput" + id;
													document.getElementsByName(nameInput)[0].value = response.data.quantity;
												}
										})
									
									
								}
								else
								{
									this.user.basket=response.data;
									axios.put('rest/userService/'+this.usernameParam,this.user)
									 .then(response => 
												{
													if(response.data==null){
														alert("Error!")
													}else{
														alert("Quantity successfully changed!")
													}
												}
											)
											}
								
										}
								)
				else
					alert("No basket.")
			}
			
		},
	reserve : function()
	{
		
		
		//separate vehicles on rent objects
		if(this.canClick)
		{
			var orders = [];
			const vehiclesByCompany = [];
			var companyIdHelper=-1;

		for (const vehicle of this.user.basket.vehicles) 
		{
		  const companyId = vehicle.objectId;
		
		  // if rent object already exists in list or not
		  if (vehiclesByCompany.hasOwnProperty(companyId)) 
		  {
		    vehiclesByCompany[companyId].push(vehicle);
		  } else 
		  {  
		    vehiclesByCompany[companyId] = [vehicle];
		  }
		}
		
		//now, we have "map" where key is rent object id, and value is vehicles list from that object
		for (const companyId in vehiclesByCompany)	//"in" is for keys
		{
			const vehiclesToOrder = vehiclesByCompany[companyId];
			//now, we can create order
			this.order = {};
			this.order.vehicles = vehiclesToOrder;
			this.order.startDateTime = this.startDateTime; 		
			this.order.duration = this.duration; 		
			orderPrice = 0;
			for(const vehicleToOrder of vehiclesToOrder)
			{
				orderPrice = orderPrice+vehicleToOrder.price*vehicleToOrder.quantity;
			} 
			this.order.price = orderPrice;
			this.order.customer = this.user.firstName + " " + this.user.lastName;
			this.order.customerUsername = this.usernameParam;
			this.order.objectId = companyId;
			orders.push(this.order);	//will be filled on backend
			
		}
		axios
			.post('rest/orderService', orders)
			.then(response => {
				this.newOrders=response.data
				if(response.data=="" || response.data==null)
				{
					alert("Impossible");
					
					
					orders=[];
					this.order = {};
				}
				else
				{
					
					alert("Successfully created");
					if(this.user.orders!=null)
						{
							for(const ord of response.data)
							{
								this.user.orders.push(ord);
							}
						}
					else
					{
						this.user.orders = [];
						this.user.orders = response.data;
					}
					
					//removing customer's basket
					var basketHelper = this.user.basket;
					this.user.basket=null;
					
					//add points
					this.user.collectedPoints += (basketHelper.price/1000.0*133);
					this.points=this.user.collectedPoints
					axios.get('rest/typeService/')
					.then(response => {
						this.types=response.data
						for(var t of this.types){
							if(t.typename.toString()=='Bronze'){
								this.forBronze=t.pointsNumber
								this.type=t
							}else if(t.typename.toString()=='Gold'){
								this.forGold=t.pointsNumber
							}else{
								this.forSilver=t.pointsNumber
							}
						}
						if(this.points>this.forGold){
							for(var t of this.types){
								if(t.typename.toString()=='Gold'){
									this.type=t
								}
							}
							
						}else if(this.points>this.forSilver && this.points<this.forGold){
							for(var t of this.types){
								if(t.typename.toString()=='Silvern'){
									this.type=t
								}
							}
						}
						this.user.type=this.type
						for(var o of this.newOrders){
							for(var order of this.user.orders)
							{
								if(o.id==order.id && this.user.type.percent!=0){
									order.price*=(100-this.user.type.percent)/100
								}
							}
						}
							axios.put('rest/userService/'+this.usernameParam,this.user)
							 .then(response => 
										{		
											axios.put('rest/orderService/editOrders',this.user.orders)
							 				.then(response => {	
							 					axios
												.delete('rest/basketService/' + basketHelper.id)
												.then(response =>
												{
													alert("Now, you can make new orders. Enjoy it!")
													router.push(`/rentalView/${this.usernameParam}`)
												})
							 				})							
											//removing basket from basket file
											
											
											
										}
							)
							})
					
											
						}
								
				})		
		
		}
		}
		
		
			
		
	
	},
	mounted()
	{
		this.orders=[]
		this.usernameParam = this.$route.params.username;
		this.startDateTime = this.$route.params.startDateTime;
		this.endDateTime = this.$route.params.endDateTime;
		start = new Date(this.startDateTime);
		end = new Date(this.endDateTime);
		this.duration = (end.getTime() - start.getTime()) / 1000;	//duration [seconds]
		
		axios
			.get('rest/userService/getUserByUsername/' + this.usernameParam)
			.then(response => {
				
				this.user = response.data;
				this.canClick = true;
				if(this.user.basket==null || this.user.basket=="")
					this.canClick = false;
				else if(this.user.basket.vehicles==null || this.user.basket.vehicles=="")
					this.canClick = false;
				else if(this.user.basket.vehicles.length==0)
					this.canClick = false;
				 
				if(this.canClick==true){
						axios
						.get('rest/orderService/getOrdersByStatus') 
						.then(response => {
							this.orders = response.data
							
							if(this.orders!=null)
								for(var order of this.orders){
									var startDate=new Date(order.startDateTime.split('T')[0]) //start date of rental
									var endDate=new Date(new Date(order.startDateTime).getTime() + order.duration * 1000) //end date of rental
									if(start<=endDate && end>=startDate){
										
										if(order.vehicles!=null)
											for(var vehicle of order.vehicles){
												const exists=this.user.basket.vehicles.find(v => v.id==vehicle.id)
												this.user.basket.vehicles=this.user.basket.vehicles.filter(v => v.id!==vehicle.id)
												if(exists)
													this.user.basket.price-=vehicle.price*exists.quantity
											}
										
									}
								}
							
							//remove rented vehicles from customer's basket
							//removing rented vehicles
							axios
								.get('rest/orderService')
								.then(response =>
								{
									var allOrders = response.data;
									if(allOrders!="" && allOrders!=null)
									{
										for(const ord of allOrders)
										{
											if(ord.vehicles!=null)
												for(const veh of ord.vehicles)
												{
													var suggestedVehicle = this.user.basket.vehicles.find(v => v.id==veh.id && veh.status=='Rented')
													if(suggestedVehicle)
													{
														this.user.basket.price -= suggestedVehicle.price*suggestedVehicle.quantity;
														this.user.basket.vehicles = this.user.basket.vehicles.filter(v => v.id != veh.id);
													}
												}
										}
									}
									axios
									.get('rest/vehicleService/getRemovedVehicles')
									.then(response=>{
										var removedVehicles=[]
										removedVehicles=response.data
										var exists=null
										if(removedVehicles!=null){
											for(var vehicle of removedVehicles){
												exists=this.user.basket.vehicles.find(v=>v.id==vehicle.id)
												if(exists){
													this.user.basket.vehicles = this.user.basket.vehicles.filter(v => v.id != vehicle.id);
													this.user.basket.price-=exists.price*exists.quantity
												}
											}
										}
										axios.put('rest/userService/'+this.usernameParam,this.user)
											.then(response => {
												axios.put('rest/basketService/editBasket',this.user.basket)
												.then(response => {
													if(this.user.basket.vehicles.length==0)
														this.canClick = false;
					
												})
											})
										})
									})	
									
									
								})
							
							
							
							
							
							
				}
			})		

		
	}
});