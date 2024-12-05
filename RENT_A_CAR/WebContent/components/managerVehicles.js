Vue.component("managersVehicles", {
	data: function () {
		    return {
			  usernameParam: '',
			  manager: {object: {location:{address:{}}}}
		    }
	},
	
	template: ` 
		<div style="text-align: center">
			<h2>{{manager.object.name}}</h2>
			<div>
				<button class="loginButton" v-on:click = "add">New vehicle</button>
			</div>
			<table border="1" align="center" style = "width: 95%; margin-top: 2px" class="vehiclesTabel">
				<tr class="rowBackground">
					<td colspan = "14" style = "text-align: center">
						<label class="objects2Label">Vehicles:</label>
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
					<th class = "th1Label">Edit</th>
					<th class = "th1Label">Remove</th>
				</tr>
				<tr class="rowBackground" v-for = "(v, index) in manager.object.vehicles" v-if = "v.removed == false">
					<td style = "text-align: center"><label class="objects1Label">{{v.brand}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.model}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.price}} EUR</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.type}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.kind}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.fuelType}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.consumption}} l/100 km</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.doorsNumber}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.personNumber}}</label></label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.status}}</label></td>
					<td style = "text-align: center"><label class="objects1Label">{{v.description}}</label></td>
					<td style = "text-align: center"><img class="objectsImage" :src="v.image"/></td>
					<td style = "text-align: center">
						<div style="height: 100%;">
							<button class="confirmButton" v-on:click="edit(v.id)">Edit</button>
						</div>
					</td>
					<td style = "text-align: center"><button class="confirmButton" v-on:click="remove(v.id)">Remove</button></td>
				</tr>
			</table>
			
			
			
		</div>
`
	, 
	methods : {
		edit : function(id)
		{
			router.push(`/editVehicle/`+this.usernameParam+`/`+id);
		},
		remove : function(id)
		{
			
			axios
				.delete('rest/vehicleService/' + id)
				.then(response =>
				{
					var removedVehicle = response.data;	//has removed field on true
					axios
						.put('rest/objectsService/removeVehicle/' + removedVehicle.objectId + '/' + removedVehicle.id + '/' + this.usernameParam)
						.then(response => 
						{
							axios
								.get('rest/managerService/getUserByUsername/' + this.usernameParam)

								.then(response => {
									this.manager = response.data;
									//treba proci kroz sve porudzbine
									//naci porudzbine koje sadrze objekat kojem ovo vozilo pripada 
									axios
									.get('rest/orderService/')
									.then(response =>
									{
										var orders=[]
										
										var helper=0
										orders=response.data
										if(orders!=null){
											orders=orders.filter(o => new Date(o.startDateTime)>new Date())
											for(var order of orders){
												if(order.vehicles!=null){
													for(var v of order.vehicles){
														if(v.id==id){
															v.removed=true
															order.price-=v.price*v.quantity
															if(order.price<0)
																order.price=0 //zbog popusta
														}
														if(v.removed==true)
															helper+=1
													}
													if(helper==order.vehicles.length){
														order.status='Declined'
														order.reason='Deleted'
													}
													helper=0
												}	
												
											}
											axios
											.put('rest/orderService/editOrders',orders)
											.then(response=>{})
										}	
									})
									/*var allBaskets = [];
									var basketsForEditing = []
									axios
										.get('rest/basketService')
										.then(response => {
											allBaskets = response.data
											if(allBaskets!=null){
												
											
												for(const basket of allBaskets)
												{
													var removedVehicleExistsInBasket = basket.vehicles.find(v => v.id==id && v.removed!=true)
													if(removedVehicleExistsInBasket)
													{
														var forRemoving = removedVehicleExistsInBasket;
														forRemoving.removed = true;
														basket.vehicles.replace(forRemoving, removedVehicleExistsInBasket);
														basket.price -= (forRemoving.price*forRemoving*quantity);
														basketsForEditing.push(basket);
													}
														
													
												}
												if(basketsForEditing.length>0)
													axios
														.put('rest/basketService/deleteRemovedVehicles', basketsForEditing)
														.then(response => {})
												
											}
											
										})*/
									
									})

								//.then(response => (this.manager = response.data))

						})
				} 
					
				)
		},
		add : function()
		{
			//if there is no rent object, vehicles cant be added
			if(this.manager.object==null || this.manager.object=="")
				alert("There is no RENT A CAR OBJECT, so you can't add new vehicle.'")
			else
				router.push(`/createVehicle/${this.usernameParam}`)
		}
		
	},
	mounted()
	{
		this.usernameParam = this.$route.params.managerUsername;
		axios
			.get('rest/managerService/getUserByUsername/' + this.usernameParam)
			.then(response => (this.manager = response.data))			//manager now has rent object and vehicles list
		
	}
});