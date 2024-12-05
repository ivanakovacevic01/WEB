Vue.component("vehiclesView", {
	data: function () {
		    return {
			  usernameParam: '',
			  rentalId:null,
			  vehicles:null
		    }
	},
	
	template: ` 
	<div align="center">
		<h2>Rented vehicles</h2>
			
			<table align="center" style = "width: 95%;" class="vehiclesTabel">
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
					<th class = "th1Label">Quantity</th>
					<th class = "th1Label">Image</th>
				</tr>
				<tr class="rowBackground" style="height: 40px;" v-for = "(v, index) in vehicles" v-if = "v.removed == false">
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
					<td style = "text-align: center"><label class="objects1Label">{{v.quantity}}</label></td>
					<td style = "text-align: center"><img class="objectsImage" :src="v.image"/></td>
				</tr>
			</table>
			
			
			
		</div>
`
	, 
	methods : {
		
	},
	mounted()
	{
		this.rentalId = this.$route.params.rentalId;
		axios
			.get('rest/orderService/getVehiclesByOrderId/' + this.rentalId)
			.then(response => (this.vehicles = response.data))			//manager now has rent object and vehicles list
		
	}
});