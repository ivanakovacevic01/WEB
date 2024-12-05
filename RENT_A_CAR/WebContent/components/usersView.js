Vue.component("usersView", {
	data: function () {
		    return {
			  users:null,
			  firstName:"",
			  lastName:"",
			  username:"",
			  adminChecked:false,
			  managerChecked:false,
			  customerChecked:false,
			  goldChecked: false,
			  silvernChecked: false,
			  bronzeChecked: false,
			  selected:"None",
			  user:null,
			  blocked:false,
			  orders:[],
			  newComments:[],
			  comments:[],
			  suspiciousCustomers: null
		    }
	},
	
	template: ` 
	<div id="userProfile" class="objectsViewContainer">
		<table align="center" class="searchTabel" style="text-align: center">
			<tr>
				<td colspan="3" align="center">
					<h4>FILTER & SEARCH</h4>
				</td>
			</tr>			
			<tr>
				<td style = "vertical-align: top;">
					<div>
						<table align="right">	
							<tr>
								<td style = "vertical-align: top;">
										<label class="tabelLabel">Role:</label><br>
										<input type="checkbox" name="roleCheckbox" v-model="customerChecked"/><label class="registration1Label">Customer</label><br>
										<input type="checkbox" name="roleCheckbox" v-model="managerChecked"/><label class="registration1Label">Manager</label><br>
										<input type="checkbox" name="roleCheckbox" v-model="adminChecked"/><label class="registration1Label">Admin</label><br>
								</td>
								<td style = "vertical-align: top;">
										<label class="tabelLabel">User type:</label><br>
										<input type="checkbox" name="userTypeCheckbox" v-model="goldChecked"/><label class="registration1Label">Gold</label><br>
										<input type="checkbox" name="userTypeCheckbox" v-model="silvernChecked"/><label class="registration1Label">Silvern</label><br>
										<input type="checkbox" name="userTypeCheckbox" v-model="bronzeChecked"/><label class="registration1Label">Bronze</label><br>
								</td>
							</tr>
						</table>				
					</div>
				</td>
				<td style = "width: 40px">
				</td>	
				<td style = "vertical-align: top;">
					<table aligh="left">
						<tr>
							<td class="tabelLabel">First name:</td>
							<td><input type="text" class="registration2Input" v-model="firstName"/></td>
						</tr>
						<tr>
							<td class="tabelLabel">Last name:</td>
							<td><input type="text" class="registration2Input" v-model="lastName"/></td>
						</tr>
						<tr>
							<td class="tabelLabel">Username:</td>
							<td><input type="text" class="registration2Input" v-model="username"/></td>
						</tr>
					</table>
				</td>
			</tr>			
			<tr>
				<td colspan="3" style = "text-align: center">
					<input class="id1Submit" type="submit" value="Reset" v-on:click="reset"/>
					<input class="id1Submit" type="submit" value="Filter&Search" v-on:click="search"/>
				</td>
			</tr>
		
		</table>
		
		<div style = "text-align: right; margin-right: 45px">
						<label class="registration1Label">SORT:</label>
						<select v-model="selected" class="tabel1Input" v-on:change="sort">
							<option selected>None</option>
							<option>Sort by: First name ascending</option>
							<option>Sort by: Last name ascending</option>
							<option>Sort by: Username ascending</option>
							<option>Sort by: Collected points ascending</option>
							<option>Sort by: First name descending</option>
							<option>Sort by: Last name descending</option>
							<option>Sort by: Username descending</option>
							<option>Sort by: Collected points descending</option>
						</select>
		</div>
		
		<h2>Users</h2>
		<table class="vehiclesTabel" align="center" style = "width: 95%; margin-top: 2px">
			<tr class="rowBackground">
				<th class = "thLabel">FIRST NAME</th>
				<th class = "thLabel">LAST NAME</th>
				<th class = "thLabel">USERNAME</th>
				<th class = "thLabel">BIRTH DATE</th>
				<th class = "thLabel">GENDER</th>
				<th class = "thLabel">ROLE</th>
				<th class = "thLabel">BLOCK</th>
				<th class = "thLabel">REMOVE</th>
			</tr>
			<tr v-for="(l,index) in users" class="rowBackground" >
				<td style = "text-align: center"><label class="objects1Label">{{l.firstName}}</label></td>
				<td style = "text-align: center"><label class="objects1Label">{{l.lastName}}</label></td>
				<td style = "text-align: center"><label class="objects1Label">{{l.username}}</label></td>
				<td style = "text-align: center"><label class="objects1Label">{{l.birthDate}}</label></td>
				<td style = "text-align: center"><label class="objects1Label">{{l.gender}}</label></td>
				<td style = "text-align: center"><label class="objects1Label">{{l.role}}</label></td>
				<td style = "text-align: center">
					<div>
						 <button class="confirmButton" v-on:click = "block(l.username,l.role)" v-if="l.role!='Admin'" v-bind:disabled="l.blocked==true">Block</button>
					</div>
				</td>
				<td style = "text-align: center">
					<div>
						 <button class="confirmButton" v-on:click = "remove(l.username,l.role)" v-if="l.role!='Admin'">Remove</button>
					</div>
				</td>
			</tr>	
		</table>	
		
		<h2>Suspicious customers</h2>
		<table class="vehiclesTabel" align="center" style = "width: 95%; margin-top: 2px">
			<tr class="rowBackground">
				<th class = "thLabel">FIRST NAME</th>
				<th class = "thLabel">LAST NAME</th>
				<th class = "thLabel">USERNAME</th>
				<th class = "thLabel">BIRTH DATE</th>
				<th class = "thLabel">GENDER</th>
				<th class = "thLabel">ROLE</th>
				<th class = "thLabel">BLOCK</th>
			</tr>
			<tr v-for="(l,index) in suspiciousCustomers" v-if="l.removed!=true" class="rowBackground">
				<td style = "text-align: center"><label class="objectsLabel">{{l.firstName}}</label></td>
				<td style = "text-align: center"><label class="objectsLabel">{{l.lastName}}</label></td>
				<td style = "text-align: center"><label class="objectsLabel">{{l.username}}</label></td>
				<td style = "text-align: center"><label class="objectsLabel">{{l.birthDate}}</label></td>
				<td style = "text-align: center"><label class="objectsLabel">{{l.gender}}</label></td>
				<td style = "text-align: center"><label class="objectsLabel">{{l.role}}</label></td>
				<td style = "text-align: center">
					<div>
						 <button class="confirmButton" v-on:click = "block(l.username,l.role)" v-bind:disabled="l.blocked==true">Block</button>
					</div>
				</td>
			</tr>	
		</table>		
	</div>		  
`
	, 
	methods : {
		search : function(){
			event.preventDefault();
			if((this.goldChecked==true || this.bronzeChecked==true || this.silvernChecked==true) && this.adminChecked==true){
				this.users=[]
			}else if((this.goldChecked==true || this.bronzeChecked==true || this.silvernChecked==true) && this.managerChecked==true){
				this.users=[]
			}else if(this.goldChecked==true || this.bronzeChecked==true || this.silvernChecked==true){
				axios
				.get('rest/userService/')
				.then(response => {
					this.users=response.data.filter(user=>(this.goldChecked==true && user.type.typename.toLowerCase()=='gold')
														|| (this.bronzeChecked==true && user.type.typename.toLowerCase()=='bronze')
														|| (this.silverChecked==true && user.type.typename.toLowerCase()=='silvern')
														|| (this.goldChecked==false && this.bronzeChecked==false && this.silverChecked==false));
														
						this.users = this.users.filter(user=> (this.firstName=="" || user.firstName.toLowerCase().includes(this.firstName.toLowerCase()))&&
								  (this.lastName=="" || user.lastName.toLowerCase().includes(this.lastName.toLowerCase()))&&
								  (this.username=="" || user.username.toLowerCase().includes(this.username.toLowerCase()))
					 );									
														
					this.users = this.users.filter(u => u.removed!=true)
					
					  
					  
					
				})
			}else{
			axios
				.get('rest/allUsersService/')
				.then(response => {(this.users = response.data.filter(user=> (this.firstName=="" || user.firstName.toLowerCase().includes(this.firstName.toLowerCase()))&&
								  (this.lastName=="" || user.lastName.toLowerCase().includes(this.lastName.toLowerCase()))&&
								  (this.username=="" || user.username.toLowerCase().includes(this.username.toLowerCase()))
					 ));
					 
					//filtering by role
					this.users = this.users.filter(user => (this.customerChecked==true && user.role.toLowerCase()=='customer')
														|| (this.managerChecked==true && user.role.toLowerCase()=='manager')
														|| (this.adminChecked==true && user.role.toLowerCase()=='admin')
														|| (this.customerChecked==false && this.managerChecked==false && this.adminChecked==false)) 
					
					  
					  })
					 }
		},
		reset : function(){
			this.firstName = "";
			this.lastName = "";
			this.username = "";
			this.customerChecked = "";
			this.managerChecked = "";
			this.adminChecked = "";
			this.goldChecked = "";
			this.silvernChecked = "";
			this.bronzeChecked = "";
			axios
				.get('rest/allUsersService/')
				.then(response => (this.users = response.data))
		},
		show : function(){
			if(this.visibility==true)
				this.visibility=false;
			else
				this.visibility=true;
		},
		sort : function(){
			
			if(this.selected=="Sort by: First name ascending"){
				function compare(a, b) {
     		 	if (a.firstName < b.firstName)
        			return -1;
      			if (a.firstName > b.firstName)
        			return 1;
      			return 0;
      			}
      			this.users.sort(compare);
			}
			else if(this.selected=="Sort by: First name descending"){
				function compare(a, b) {
     		 	if (a.firstName < b.firstName)
        			return 1;
      			if (a.firstName > b.firstName)
        			return -1;
      			return 0;
      			}
      			this.users.sort(compare);
			}
			else if(this.selected=="None"){
      			axios
				.get('rest/allUsersService/')
				.then(response => (this.users = response.data))
			}
		   	else if(this.selected=="Sort by: Last name descending"){
				function compare(a, b) {
     		 	if (a.lastName < b.lastName)
        			return 1;
      			if (a.lastName > b.lastName)
        			return -1;
      			return 0;
      			}
      			this.users.sort(compare);
			}
			else if(this.selected=="Sort by: Last name ascending"){
				function compare(a, b) {
     		 	if (a.lastName < b.lastName)
        			return -1;
      			if (a.lastName > b.lastName)
        			return 1;
      			return 0;
      			}
      			this.users.sort(compare);
			}
			else if(this.selected=="Sort by: Username ascending"){
				function compare(a, b) {
     		 	if (a.username < b.username)
        			return -1;
      			if (a.username > b.username)
        			return 1;
      			return 0;
      			}
      			this.users.sort(compare);
			}
			else if(this.selected=="Sort by: Username descending"){
				function compare(a, b) {
     		 	if (a.username<b.username)
        			return 1;
      			if (a.username > b.username)
        			return -1;
      			return 0;
      			}
      			this.users.sort(compare);
			}
			else if(this.selected=="Sort by: Collected points ascending"){
				axios
				.get('rest/userService/')
				.then(response => {
					this.users=response.data
					function compare(a, b) {
	     		 	if (a.collectedPoints<b.collectedPoints)
	        			return -1;
	      			if (a.collectedPoints > b.collectedPoints)
	        			return 1;
	      			return 0;
	      			}
	      			if(this.users!=null){
						this.users.sort(compare);
	      				this.users=this.users.filter(u=>u.removed!=true)
					}
				})
				
			}
			else if(this.selected=="Sort by: Collected points descending"){
				axios
				.get('rest/userService/')
				.then(response => {
					this.users=response.data
					function compare(a, b) {
	     		 	if (a.collectedPoints<b.collectedPoints)
	        			return 1;
	      			if (a.collectedPoints > b.collectedPoints)
	        			return -1;
	      			return 0;
	      			}
	      			if(this.users!=null){
						this.users.sort(compare);
	      				this.users=this.users.filter(u=>u.removed!=true)
					}
	      			
	      		})	
			}
		},
		block : function(username,role){
			if(role=='Customer'){
				axios
				.get('rest/userService/getUserByUsername/'+username)
				.then(response => {
					this.user = response.data
					axios
					.put('rest/userService/block/' +username)	
						.then(response => {
								if(this.users!=null){
									for(var u of this.users){
										if(u.username==username){
											u.blocked=true
										}
									}
									if(this.suspiciousCustomers!=null){
										for(var u of this.suspiciousCustomers){
											if(u.username==username){
												u.blocked=true
											}
										}
									}
									

								


									if(this.user!=null && this.user.orders!=null)
									for(var order of this.user.orders){
										if(order.status=='Processing')
											order.status='Declined'
									}
									this.user.blocked=true;
									
									//saving blocked user with declined orders to file
									axios
										.put('rest/userService/' +this.user.username,this.user)	
											.then(response => {
												this.user=response.data;
												
												//saving declined orders to file
												if(this.user.orders!=null){
													axios
														.put('rest/orderService/declineForBlockedUser', this.user.orders)
														.then(response => {
															axios
																.put('rest/commentService/declingForBlockedUser/' + this.user.username)
																.then(response => 
																	{
																		alert("User blocked, comments and rentals declined.")
																	})
														})
													}	
											})
									
								}})
					})

				
			}else{
				axios
				.get('rest/managerService/getUserByUsername/'+username)
				.then(response => {
					(this.user = response.data)
					axios
					.put('rest/managerService/block/' +username)
						.then(response => {
								for(var u of this.users){
									if(u.username==username){
										u.blocked=true
									}
								}
								this.user.blocked=true
								axios
										.put('rest/managerService/' +this.user.username,this.user)	
											.then(response => {
												this.user=response.data;
								
												if(this.user.object!=null){
													this.user.object.removed=true
													if(this.user.object.vehicles!=null){
														for(var vehicle of this.user.object.vehicles)
															vehicle.removed=true
													}
													axios
													.put('rest/vehicleService/newEditVehicles' ,this.user.object.vehicles)
													.then(response=>{
															axios
															.put('rest/managerService/' +username,this.user)
															.then(response => {
																this.user=response.data
																axios
																.put('rest/objectsService/' +this.user.object.id,this.user.object)
																.then(response=>{
																	axios
																	.get('rest/orderService/getOrdersByObjectId/' +this.user.object.id)
																	.then(response =>{
																		var ordersForDeclining=response.data
																		if(ordersForDeclining!=null){
																			ordersForDeclining=ordersForDeclining.filter(o => new Date(o.startDateTime)>new Date())
																			for(var order of ordersForDeclining){
																				if(order.status=='Processing'){
																					order.object.removed=true
																					order.status='Declined'
																					order.reason="Deleted"
																					if(order.vehicles!=null){
																						for(var v of order.vehicles){
																							
																								v.removed=true
																								order.price-=v.price*v.quantity
																								if(order.price<0)
																									order.price=0 //zbog popusta
																							
																							
																						}
																						
																					}	
																				}
																				
																			}
																			axios
																			.put('rest/orderService/editOrders' ,ordersForDeclining)
																			.then(response=>{
																				ordersForDeclining=response.data
		
																				
																			})
																		
																		}
																		
																	})
																})
															})
													})
													
												}
											})
								
							})
				})
			}
			

		},
		remove : function(username, role)
		{
			if(role=='Customer'){
				axios
				.get('rest/userService/getUserByUsername/'+username)
				.then(response => {
					this.user = response.data
					axios
					.put('rest/userService/remove/' +username)	
						.then(response => {
								if(this.users!=null){
									for(var u of this.users){
										if(u.username==username){
											u.removed=true
										}
									}
									if(this.suspiciousCustomers!=null){
										for(var u of this.suspiciousCustomers){
											if(u.username==username){
												u.removed=true
											}
										}
									}
									

								


									if(this.user!=null && this.user.orders!=null)
									for(var order of this.user.orders){
										if(order.status=='Processing')
											order.status='Declined'
									}
									this.user.removed=true;
									
									//saving blocked user with declined orders to file
									axios
										.put('rest/userService/' +this.user.username,this.user)	
											.then(response => {
												this.user=response.data;
												
												//saving declined orders to file

												

												if(this.user.orders!=null){
													axios
														.put('rest/orderService/declineForBlockedUser', this.user.orders)
														.then(response => {
															axios
																.put('rest/commentService/declingForBlockedUser/' + this.user.username)
																.then(response => 
																	{
																		alert("User removed, comments and rentals declined.");
																		this.users = this.users.filter(u => u.removed!=true)
																		this.suspiciousCustomers = this.suspiciousCustomers.filter(u => t.removed!=true)
																		
																	})
														})
													}else{
														this.users = this.users.filter(u => u.removed!=true)
														this.suspiciousCustomers = this.suspiciousCustomers.filter(u => t.removed!=true)
													}
														

											})
									
								}})
					})

				
			}else{
				axios
				.get('rest/managerService/getUserByUsername/'+username)
				.then(response => {
					(this.user = response.data)
					axios
					.put('rest/managerService/remove/' +username)
						.then(response => {
								for(var u of this.users){
									if(u.username==username){
										u.removed=true
									}
								}
								this.user.removed=true;
								
									//saving blocked user with declined orders to file
									axios
										.put('rest/managerService/' +this.user.username,this.user)	
											.then(response => {
												this.user=response.data;
												this.users = this.users.filter(u => u.removed!=true)
												if(this.user.object!=null){
													this.user.object.removed=true
													axios
													.put('rest/managerService/' +username,this.user)
													.then(response => {
														this.user=response.data
														axios
														.put('rest/objectsService/' +this.user.object.id,this.user.object)
														.then(response=>{
															axios
															.get('rest/orderService/getOrdersByObjectId/' +this.user.object.id)
															.then(response =>{
																var ordersForDeclining=response.data
																if(ordersForDeclining!=null){
																	ordersForDeclining=ordersForDeclining.filter(o => new Date(o.startDateTime)>new Date())
																			for(var order of ordersForDeclining){
																				if(order.status=='Processing'){
																					order.object.removed=true
																					order.status='Declined'
																					order.reason="Deleted"
																					if(order.vehicles!=null){
																						for(var v of order.vehicles){
																							
																								v.removed=true
																								order.price-=v.price*v.quantity
																								if(order.price<0)
																									order.price=0 //zbog popusta
																							
																							
																						}
																						
																					}	
																				}
																				
																			}
																			axios
																			.put('rest/orderService/editOrders' ,ordersForDeclining)
																			.then(response=>{
																				ordersForDeclining=response.data
		
																				
																			})
																}
															})
														})
													})
												}
											})
								
							})
				
		})}}

		

	},
	mounted()
	{
		axios
				.get('rest/allUsersService/')
				.then(response => 
				{
					this.users = response.data;
					
					axios
						.get('rest/orderService/getSuspiciousCustomers')
						.then(response => (this.suspiciousCustomers = response.data))
				})
		
		
	}
});