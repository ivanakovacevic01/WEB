Vue.component("creatingObject", {
	data: function () {
		    return {
		      title: "Add new object - form",
		      usernameParam: "",
		      object: {name:"",grade:"",startTime:"",endTime:"", state:"Opened",logo:"",location: {address: {}}},
		      managers: null,
		      manager: {gender: 'Male'},
		      mustAddNewManager: false,
		      optionManager: {}
		    }
	},
	
	
	template: ` 
<div id="registrationForm">
	<div class="creatingVehicleContainer">
		<div>
			<h2>{{title}}</h2>
		</div>
		<div>
			<form>
			
			<table align="center" style="text-align: center; margin-top: 80px">
				<tr>
					<td style="width: 50%; vertical-align: top; margin-top: 20px;">
						<div>
							<label class="registrationLabel">Name:</label>
							<input class="registrationInput" type="text" name="name" v-model = "object.name">
						</div>
						<div>
							<label class="registrationLabel">Start time:</label>
							<input class="registrationInput" type="time" name="startTime" v-model = "object.startTime">
						</div>
						<div>
							<label class="registrationLabel">End time:</label>
							<input class="registrationInput" type="time" name="endTime" v-model = "object.endTime">
						</div>
						<div>
							<label class="registrationLabel">Logo URL:</label>
							<input class="registrationInput" type="text" name="logoUrl" v-model = "object.logo">
						</div>
						
						<div>
							<table align="center">
								<tr>
									<td>
										<label class="registrationLabel">Manager:</label>
									</td>
									<td style="width: 320px; vertical-align: top; margin-top: 2px">
										<span id="chooseManager">
											<select name="manager" v-model="manager" class="registrationInput" v-bind:hidden = "mustAddNewManager == true">
												<option  v-for = "(m, index) in managers" :value="m">{{m.firstName}} {{m.lastName}}</option>
											</select>
										</span>
									</td>
								</tr>
							</table>
						</div>
						
					</td>
					<td style="width: 30%; vertical-align: top; margin-top: 20px; text-align: left" >
					<div>
						<div id="map" class="addingObjectMap">
						</div>
						<div>
							<label class="registrationLabel" style="height: 30px; margin-top: 10px">Location URL:</label>
						</div>
						<div>
							<input class="registrationInput" type="text" name="locationUrl" v-model = "object.location.image">
						</div>
					</div>
					</td>
					
					<td v-bind:hidden = "mustAddNewManager == false" >
						<label class="registrationLabel">New manager:</label><br>
						<div>
							<label class="registration1Label">Username:</label>
							<input class="registration1Input" type="text" name="username" v-model = "manager.username">
						</div>
						<div>
							<label class="registration1Label">Password:</label>
							<input class="registration1Input" type="password" name="password1">
						</div>
						<div>
							<label class="registration1Label">Password:</label>
							<input class="registration1Input" type="password" name="password2" v-model = "manager.password">
						</div>
						<div>
							<label class="registration1Label">First name:</label>
							<input class="registration1Input" type="text" name="firstName" v-model = "manager.firstName">
						</div>
						<div>
							<label class="registration1Label">Last name:</label>
							<input class="registration1Input" type="text" name="lastName" v-model = "manager.lastName">
						</div>
						<div>
							<label class="registration1Label">Birth date:</label>
							<input class="registration1Input" type="Date" name="birthDate" v-model = "manager.birthDate">
						</div>
						<div>
							<table align="center">
								<tr>
									<td>
										<label class="registration1Label">Gender:</label>
									</td>
									<td>
										<table>
											<tr>
												<td>
													<input class="radioStyle" style="height: 12px" type="radio" name="gender" checked v-model = "manager.gender" value="Male">
													<label class="radio1Label" style="height: 12px">Male</label>
												</td>
											</tr>
											<tr>
												<td style="width: 100px">
													<input class="radioStyle" style="height: 12px" type="radio" name="gender" v-model = "manager.gender" value="Female">
													<label class="radio1Label" style="height: 12px">Female</label>			
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
						
					</td>
				</tr>
				<tr style="height: 80px"></tr>
				<tr>
				<td colspan="5" style="text-align:center">
					<input class="idSubmit" type="submit" value="Create" v-on:click="create">
				</td>
				</tr>
				
			</table>
						
				
					
			</form>
		</div>
	</div>
</div>		  
`
	, 
	methods : {
		create : function () {
			event.preventDefault();
			
			
			//front
				var isValid=true;
				if(document.getElementsByName("name")[0].value==""){
					document.getElementsByName("name")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("name")[0].style.background='lightgray';
										
				
					
				if(document.getElementsByName("startTime")[0].value==""){
					document.getElementsByName("startTime")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("startTime")[0].style.background='lightgray';
					
				if(document.getElementsByName("endTime")[0].value==""){
					document.getElementsByName("endTime")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("endTime")[0].style.background='lightgray';
						
				if(document.getElementsByName("logoUrl")[0].value==""){
					document.getElementsByName("logoUrl")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("logoUrl")[0].style.background='lightgray';
					
				if(document.getElementsByName("locationUrl")[0].value==""){
					document.getElementsByName("locationUrl")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("locationUrl")[0].style.background='lightgray';
					
				if(this.object.location.longitude==null || this.object.location.longitude=="" || this.object.location.address.country==null || this.object.location.address.country==""){
					alert("Choose location.")
					isValid=false;
					return;
				}
				if(this.object.location.address.city=="" || this.object.location.address.city==null || this.object.location.address.zipCode=="" || this.object.location.address.zipCode==null || this.object.location.address.country=="" || this.object.location.address.country==null)
					{
						alert("Please zoom map to be able to choose desired address.")
						isValid = false;
						return;
					}
				if(this.object.location.address.street=="" || this.object.location.address.street==null)
				{
					this.object.location.address.street="N/A";
				}
				if(this.object.location.address.number=="" || this.object.location.address.number==null)
				{
					this.object.location.address.number="N/A";
				}
					
				//check if must add new manager - needeed validation for manager's fields
				if(this.mustAddNewManager)
				{
					if(document.getElementsByName("username")[0].value==""){
						document.getElementsByName("username")[0].style.background='red';
						isValid=false;
					}
					else
						document.getElementsByName("username")[0].style.background='lightgray';
					if(document.getElementsByName("firstName")[0].value==""){
						document.getElementsByName("firstName")[0].style.background='red';
						isValid=false;
					}
					else
						document.getElementsByName("firstName")[0].style.background='lightgray';
					if(document.getElementsByName("lastName")[0].value==""){
						document.getElementsByName("lastName")[0].style.background='red';
						isValid=false;
					}
					else
						document.getElementsByName("lastName")[0].style.background='lightgray';
					if(document.getElementsByName("birthDate")[0].value==""){
						document.getElementsByName("birthDate")[0].style.background='red';
						isValid=false;
					}
					else
						document.getElementsByName("birthDate")[0].style.background='lightgray';
					if(document.getElementsByName("password1")[0].value=="" || document.getElementsByName("password2")=="" || document.getElementsByName("password1")[0].value!=document.getElementsByName("password2")[0].value){
						document.getElementsByName("password1")[0].style.background='red';
						document.getElementsByName("password2")[0].style.background='red';
						isValid=false;
					}
					else{
						document.getElementsByName("password1")[0].style.background='lightgray';
						document.getElementsByName("password2")[0].style.background='lightgray';
					}
				}
				else
				{
					if(document.getElementsByName("manager")[0].value==""){
						document.getElementsByName("manager")[0].style.background='red';
						isValid=false;
					}
					else
						document.getElementsByName("manager")[0].style.background='lightgray';
				}
				
				if(isValid)
				{
					if(this.mustAddNewManager)
					{
						//validation if username alredy exists
						axios
							.get('rest/allUsersService/usernameExists1/' + this.manager.username)
							.then(response =>
							{
								if(response.data==true)
									alert("Username alredy exists!")
								else
								{
									this.manager.object = this.object;
									axios.post('rest/objectsService', this.manager)
									.then(response => 
									{
										if(response.data==null || response.data==""){
											alert("Creating is impossible!");
										}else{
											//showToast('New rent object created!', 'success');
											router.push(`/adminProfileView/${this.usernameParam}`);
										}
									});
								}
							})
					}
					
					else
					{
								this.manager.object = this.object;
								axios.post('rest/objectsService', this.manager)
								.then(response => 
								{
									if(response.data==null || response.data==""){
										alert("Creating is impossible!");
									}else{
										//showToast('New rent object created!', 'success');
										router.push(`/adminProfileView/${this.usernameParam}`)}
								});
					}
					
				}
				
			},
		
		getLocationDetails : function (coordinates)
		{
			fetch(
				"http://nominatim.openstreetmap.org/reverse?format=json&lon=" + coordinates[0] + "&lat=" + coordinates[1]
			)
				.then((response) =>
				{
					return response.json();
				})
				.then((json) => 
				{
					console.log(json);
					var address = json.address;
					
					this.object.location.address.street = address.road || address.hamlet;
					this.object.location.address.country = address.country;
					this.object.location.address.number = address.house_number;
					this.object.location.address.zipCode = address.postcode;
					this.object.location.address.city = address.city || address.town || address.village;
					
					this.object.location.latitude = coordinates[0];
					this.object.location.longitude = coordinates[1];
					
				})
				.catch((error) =>
				{
					console.log(error)
				});
		}
		},
	mounted()
	{
		this.usernameParam = this.$route.params.adminUsername;
		const map = new ol.Map(
				{
			      target: 'map', // ID of div where map is contained
			      layers: [
			        new ol.layer.Tile(
					{
			          source: new ol.source.OSM(), // data source for main map
			        }),
			      ],
			      view: new ol.View(
					{
			        // Start view for map
			        center: ol.proj.fromLonLat([17,43]), // Map center, BiH centered :D
			        zoom: 6, // zoom level
			      }),
			    });
			    
		var addingLayer = new ol.layer.Vector({
					source: new ol.source.Vector({
					})
				});
	    
		map.on('click', (event) => {
			map.removeLayer(addingLayer);
			var coordinate = ol.proj.toLonLat(event.coordinate);
			this.getLocationDetails(coordinate);
			addingLayer.getSource().clear();
			
			//marker selected location
			var marker = new ol.Feature(
			{
				geometry: new ol.geom.Point(event.coordinate),
			});
			addingLayer.getSource().addFeature(marker);
			map.addLayer(addingLayer);
		})
		
		
		axios
			.get('rest/managerService/getManagersWithoutRentObject')
			.then(response => {
				this.managers = response.data;
				if(this.managers!=null && this.managers!="")
					this.managers = this.managers.filter(m => m.removed!=true)
				
				this.mustAddNewManager = (this.managers==null || this.managers=="" || this.managers==[]);
				if(this.mustAddNewManager)
				{
					var label = document.createElement('label');
					label.textContent = 'No available managers. Must add new.';
					chooseManager.appendChild(label);
				}
				});
			
		
	}
	}
);