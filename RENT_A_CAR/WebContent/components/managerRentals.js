Vue.component("managersRentals", {
	data: function () {
		    return {
			  usernameParam: '',
			  manager: {object: {location:{address:{}}}},
			  object:null,
			  rental:null,
		      minPrice: "",
		      maxPrice: "",
		      startDate: "",
		      endDate: "",
		      selected:"None",
		      visibility:true,
		      rentalId:"",
		      rentalStatus:"",
		      reason:"",
		      user:null,
		      vehicles:null,
		      order:null,
		      newVehicles:null,
		      newObject:null
		    }
	},
	
	template: ` 
		<div style="text-align: center">
		<table align="center" class="searchTabel" style="margin-top: 2px; text-align: center">
			<tr>
				<td align="center">
					<h4>SEARCH</h4>
				</td>
			</tr>			
			<tr>	
				<td style = "vertical-align: top">
					<table align="center">
						<tr>
							<td><label class="tabelLabel">Price:</label></td>
							<td><input class="grade1Input" type="number" v-model="minPrice" placeholder="Min"/>
								<input class="grade1Input" type="number" v-model="maxPrice" placeholder="Max"/>
							</td>
						</tr>	
						<tr>
							<td><label class="tabelLabel">Date:</label></td>
							<td><input class="grade1Input" type="date" v-model="startDate" placeholder="Start"/>
								<input class="grade1Input" type="date" v-model="endDate" placeholder="End"/>
							</td>
						</tr>	
					</table>	
				</td>
			</tr>
			<tr style="height: 20px"></tr>
			<tr>
				<td style = "text-align: center">
					<input class="id1Submit" type="submit" value="Reset" v-on:click="reset"/>
					<input class="id1Submit" type="submit" value="Search" v-on:click="search"/>
				</td>
			</tr>
		</table>
		<div style = "text-align: right; margin-right: 45px">
						<label class="registration1Label">SORT:</label>
						<select v-model="selected" class="tabel1Input"v-on:change="sort" >
							<option>None</option>
							<option>Sort by: Price ascending</option>
							<option>Sort by: Date ascending</option>
							<option>Sort by: Price descending</option>
							<option>Sort by: Date descending</option>
						</select>
		</div>
			<h2>Manager's rentals</h2>
			<table border="1" align="center" style = "width: 95%; margin-top: 2px" class="vehiclesTabel">
				<tr class="rowBackground">
						<th class = "thLabel">Customer</th>
						<th class = "thLabel">Start time</th>
						<th class = "thLabel">Duration</th>
						<th class = "thLabel">Price</th>
						<th class = "thLabel">Status</th>
						<th class = "thLabel">Code</th>
						<th class = "thLabel">Vehicles</th>
						<th class = "thLabel">Approve</th>
						<th class = "thLabel">Decline</th>
						<th class = "thLabel">Receive</th>
						<th class = "thLabel">Return</th>
					</tr>
					<tr class="rowBackground" v-for = "(v, index) in rental">
						<td style = "text-align: center"><label class="objects1Label">{{v.customer}}</label></td>
						<td style = "text-align: center"><label class="objects1Label" style="width: 180px">{{v.startDateTime}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.duration/3600.0}} h</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.price}} EUR</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.status}}</label></td>
						<td style = "text-align: center"><label class="objects1Label">{{v.id}}</label></td>
						<td style = "text-align: center">
							<div>
								<button class="confirmButton" v-on:click = "viewVehicles(v.id)">Vehicles</button>
							</div>
						</td>
						<td style = "text-align: center">
							<div>
								<button class="approveButton" v-on:click = "approveOrder(v.id,v.status)" v-bind:disabled="v.status!='Processing'">Approve</button>
							</div>
						</td>
						<td style = "text-align: center">
							<div>
								<button class="declineButton" v-on:click = "declineOrder(v.id,v.status)" v-bind:disabled="v.status!='Processing'">Decline</button>
							</div>
						</td>
						<td style = "text-align: center">
							<div>
								<button class="receiveButton" v-on:click = "receiveOrder(v.id,v.status)" v-bind:disabled="v.status!='Approved'">Receive</button>
							</div>
						</td>
						<td style = "text-align: center">
							<div>
								<button class="returnButton" v-on:click = "returnOrder(v.id,v.status)" v-bind:disabled="v.status!='Received'">Return</button>
							</div>
						</td>
					</tr>
			</table>
			<div v-bind:hidden="this.visibility==true" style="text-align:center; margin-top: 5px">
				<label class="registration2Label">Reason:</label><br>
				<textarea class="reasonStyle" name="reasonArea" v-model="reason"></textarea><br>
				<button class="approveButton" v-on:click = "decline">Confirm</button>
				<button class="declineButton" v-on:click = "cancelDeclining">Cancel</button>
			</div>
			
			
		</div>
`
	, 
	methods : {
		
		viewVehicles:function(id){
			event.preventDefault();
			router.push(`/vehiclesView/`+id)
		},
		reset : function()
		{
		    this.minPrice = "";
		    this.maxPrice = "";
		    this.startDate = "";
		    this.endDate = "";
		    this.selected = "";
			axios
				.get('rest/orderService/getOrdersByObjectId/' + this.object.id)
				.then(response => this.rental=response.data)
			
		},
		search : function()
		{
			event.preventDefault();
			axios
				.get('rest/orderService/getOrdersByObjectId/' + this.object.id)
				.then(response =>
				{	
					//searching
					(this.rental = response.data.filter(rent =>	(this.minPrice=="" || this.minPrice==null || rent.price>=parseFloat(this.minPrice)) && (this.maxPrice=="" || this.minPrice==null || rent.price<=parseFloat(this.maxPrice)) && 
					(this.startDate=="" || rent.startDateTime.split('T')[0]==this.startDate) && (this.endDate=="" || (new Date(new Date(rent.startDateTime).getTime() + rent.duration * 1000)).toISOString().split('T')[0]==this.endDate)));
					;				
					
				})
		
			
		},
		sort:function(){
			
			if(this.selected=="None"){
      			axios
					.get('rest/orderService/getByUsername/' + this.user.username)
					.then(response => (this.rental = response.data))
			}
		   	else if(this.selected=="Sort by: Price descending"){
				function compare(a, b) {
     		 	if (a.price < b.price)
        			return 1;
      			if (a.price > b.price)
        			return -1;
      			return 0;
      			}
      			this.rental.sort(compare);
			}
			else if(this.selected=="Sort by: Price ascending"){
				function compare(a, b) {
     		 	if (a.price < b.price)
        			return -1;
      			if (a.price > b.price)
        			return 1;
      			return 0;
      			}
      			this.rental.sort(compare);
			}
			else if(this.selected=="Sort by: Date ascending"){
				function compare(a, b) {
     		 	if (a.startDateTime < b.startDateTime)
        			return -1;
      			if (a.startDateTime > b.startDateTime)
        			return 1;
      			return 0;
      			}
      			this.rental.sort(compare);
			}
			else if(this.selected=="Sort by: Date descending"){
				function compare(a, b) {
     		 	if (a.startDateTime < b.startDateTime)
        			return 1;
      			if (a.startDateTime > b.startDateTime)
        			return -1;
      			return 0;
      			}
      			this.rental.sort(compare);
			}
		},
		approveOrder:function(id,status){
			event.preventDefault();
			if(status=='Processing')
			{
				axios
					.put('rest/orderService/approve/'+id+'/'+status)	//sending status for backend validation
					.then(response => {
						if(response.data==false)
							alert("Can't be approved.'") 
						else
						{
							axios
							.get('rest/orderService/getOrdersByObjectId/' + this.object.id)
							.then(response => {
								this.rental=response.data
								axios
								.get('rest/orderService/getCustomer/' + id)
								.then(response => {
									this.user=response.data
									for(var o of this.user.orders){
										if(o.id==id){
											o.status='Approved'
										}
									}
									axios
									.put('rest/userService/' + this.user.username,this.user)
									.then(response => this.user=response.data)
										
									})
							})
						}
					})
			}
			
		},
		cancelDeclining:function()
		{
			event.preventDefault();
			this.visibility = true;
			this.reason = "";
			document.getElementsByName("reasonArea")[0].style.background='white';
			
		},
		declineOrder:function(id,status){
			event.preventDefault();
			this.visibility=false;
			this.rentalId=id;
			this.rentalStatus=status;
		},
		decline:function(){
			event.preventDefault();
			//front validation
			var isValid=true;
			if(document.getElementsByName("reasonArea")[0].value=="" || document.getElementsByName("reasonArea")[0].value==null)
			{
				isValid = false;
				document.getElementsByName("reasonArea")[0].style.background='red';
			}
			else
				document.getElementsByName("reasonArea")[0].style.background='white';
			if(this.rentalStatus=='Processing')
			{
				if(isValid){
				axios
				.put('rest/orderService/decline/' + this.rentalId+'/'+this.rentalStatus+'/'+this.reason)	//sending status for backend validation
					.then(response => {
						if(response.data==false)
						{
							this.visibility = true;
							this.reason = "";
							alert("Can't be declined.'")
						}
							 
						else
						{
							axios
							.get('rest/orderService/getOrdersByObjectId/' + this.object.id)
							.then(response => {
								this.rental=response.data
								axios
								.get('rest/orderService/getCustomer/' + this.rentalId)
								.then(response => {
									this.user=response.data;
									
									if(this.user!=null && this.user.orders!=null)
									for(var o of this.user.orders){
										if(o.id==this.rentalId){
											o.status='Declined'
										}
									}
									axios
									.put('rest/userService/' + this.user.username,this.user)
									.then(response => {
										this.user=response.data;
										this.visibility = true;
										this.reason = "";
			
									})
										
									})
							})	
						}
					})
					
					}
			}
			else
			{
				alert("Can't decline.'")
				this.visibility = true;
				this.reason = "";
			
			}
			
			
		},
		receiveOrder:function(id,status){
			event.preventDefault();
			if(status=='Approved')
			{
				axios
					.put('rest/orderService/receive/'+id+'/'+status)	//sending status for backend validation
					.then(response => {
						if(response.data==false)
							alert("Can't be received.'") 
						else
						{
							axios
							.get('rest/orderService/getOrdersByObjectId/' + this.object.id)
							.then(response => {
								this.rental=response.data
								axios
								.get('rest/orderService/getCustomer/' + id)
								.then(response => {
									this.user=response.data;
									
									if(this.user!=null && this.user.orders!=null)
										for(var o of this.user.orders){
											if(o.id==id){
												o.status='Received'
											
												for(var veh of o.vehicles){
													veh.status='Rented';
													
												}
											
											}
										}
									
									//save customer with rented vehicles and received order
									axios
									.put('rest/userService/' + this.user.username,this.user)
									.then(response => {
										this.user=response.data
										axios
										.get('rest/orderService/' + id)
										.then(response => {
												this.order=response.data;
												
												if(this.order!=null && this.order.vehicles!=null)
													for(var veh of this.order.vehicles){
															veh.status='Rented';			
													}
													
													axios
													.put('rest/orderService/' + id,this.order)
													.then(response => {
														this.order=response.data;
													/*axios
													.get('rest/orderService/getObject/'+id)
													.then(response => {
														this.newObject=response.data
														
														axios
														.put('rest/objectsService/'+ this.newObject.id,this.newObject)
														.then(response => {
															this.newObject=response.data
														})
													})*/
												})
											})
										})
										
									})
							
								
							})
						}
					})
			}
			else
				alert("Can't be received.'") 
			
		},
		returnOrder(id,status){
			event.preventDefault();
			if(status=='Received')
			{
				axios
					.put('rest/orderService/return/'+id+'/'+status)	//sending status for backend validation
					.then(response => {
						if(response.data==false)
							alert("Can't be returned.'") 
						else
						{
							axios
							.get('rest/orderService/getOrdersByObjectId/' + this.object.id)
							.then(response => {
								this.rental=response.data
								axios
								.get('rest/orderService/getCustomer/' + id)
								.then(response => {
									this.user=response.data;
									
									if(this.user!=null && this.user.orders!=null)
										for(var o of this.user.orders){
											if(o.id==id){
												o.status='Returned'
											
												for(var veh of o.vehicles){
													veh.status='Available';
													
												}
											
											}
										}
									
									//save customer with available vehicles and returned order
									axios
									.put('rest/userService/' + this.user.username,this.user)
									.then(response => {
										this.user=response.data
										axios
										.get('rest/orderService/' + id)
										.then(response => {
												this.order=response.data;
												
												if(this.order!=null && this.order.vehicles!=null)
													for(var veh of this.order.vehicles){
															veh.status='Available';			
													}
													
													axios
													.put('rest/orderService/' + id,this.order)
													.then(response => {
														this.order=response.data;
													})
											})
										})
										
									})
							
								
							})
						}
					})
			}
			else
				alert("Can't be returned.'") 
			
		}
	},
	mounted()
	{
		this.usernameParam = this.$route.params.managerUsername;
		axios
			.get('rest/managerService/getUserByUsername/' + this.usernameParam)
			.then(response => 
			{
				this.manager = response.data	
				axios
				.get('rest/managerService/getByManager/' + this.manager.username)
				.then(response =>{
					 this.object=response.data //now we have object of this manager
					 axios
					.get('rest/orderService/getOrdersByObjectId/' + this.object.id)
					.then(response => this.rental=response.data)
				})
			})
		
	}
});