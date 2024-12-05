Vue.component("creatingVehicle", {
	data: function () {
		    return {
		      title: "Add new vehicle - form",
		      usernameParam: "",
		      vehicle: {brand:"",model:"",price:"",type:"",kind:"Manual",fuelType:"Diesel",consumption:"",object:null,doorsNumber:"",personNumber:"",description:"",image:""},
		      rentACarObject: {location: {address: {}}}
		    }
	},
	
	
	template: ` 
<div  id="registrationForm">
	<div class="creatingVehicleContainer">
		<div style="height: 100px;">
			<h2>{{title}}</h2>
		</div>
		<div>
			<form>
				<table border="0" style = "width: 100%; text-align: center; margin-top: 50px" align="center">
					<tr>
						<td style = "width: 40%; text-align: center;vertical-align: top;">
							<div>
								<label class="registrationLabel">Brand:</label>
								<input class="registrationInput" type="text" name="brand" v-model = "vehicle.brand">
							</div>
							<div>
								<label class="registrationLabel">Model:</label>
								<input class="registrationInput" type="text" name="model" v-model="vehicle.model">
							</div>
							<div>
								<label class="registrationLabel">Price (EUR):</label>
								<input class="registrationInput" type="number" name="price" v-model="vehicle.price">
							</div>
							<div>
								<label class="registrationLabel">Type:</label>
								<input class="registrationInput" type="text" name="type" v-model="vehicle.type">
							</div>
							<div>
								<table align="center">
									<tr>
										<td>
											<label class="registrationLabel">Fuel type:</label>
										</td>
										<td style="width: 320px">
											<table>
												<tr>
													<td>
														<input class="radioStyle" type="radio" name="fuelType" v-model = "vehicle.fuelType" value="Diesel">
														<label class="radioLabel">Diesel</label>
													</td>
												</tr>
												<tr>
													<td>
													<input class="radioStyle" type="radio" name="fuelType" v-model = "vehicle.fuelType" value="Petrol">
													<label class="radioLabel">Petrol</label>
													</td>
												</tr>
												<tr>
													<td>
														<input class="radioStyle" type="radio" name="fuelType" v-model = "vehicle.fuelType" value="Hybrid">
														<label class="radioLabel">Hybrid</label>
													</td>
												</tr>
												<tr>
													<td>
														<input class="radioStyle" type="radio" name="fuelType" v-model = "vehicle.fuelType" value="Electric">
														<label class="radioLabel">Electric</label>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
							
						</td>
						<td style = "width: 40%; text-align: center; vertical-align: top;">
							<div>
								<label class="registrationLabel">Consumption (l/100 km):</label>
								<input class="registrationInput" type="number" name="consumption" v-model="vehicle.consumption">
							</div>
							<div>
								<label class="registrationLabel">Doors:</label>
								<input class="registrationInput" type="number" name="doors" v-model = "vehicle.doorsNumber">
							</div>
							
							<div>
								<label class="registrationLabel">Capacity:</label>
								<input class="registrationInput" type="number" name="person" v-model="vehicle.personNumber">
							</div>
							<div>
								<label class="registrationLabel">Description:</label>
								<input class="registrationInput" type="text" name="description" v-model="vehicle.description">
							</div>
							<div>
								<label class="registrationLabel">Image:</label>
								<input class="registrationInput" type="text" name="image" v-model="vehicle.image">
							</div>
							<div>
								<table align="center">
									<tr>
										<td>						
											<label class="registrationLabel">Kind:</label>
										</td>
										<td style="width: 320px">
											<table>
												<tr>
													<td>
														<input class="radioStyle" type="radio" name="kind" v-model = "vehicle.kind" value="Manual">
														<label class="radioLabel">Manual</label>
													</td>
												</tr>
												<tr>
													<input class="radioStyle" type="radio" name="kind" v-model = "vehicle.kind" value="Automatic">
													<label class="radioLabel">Automatic</label>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
							
						</td>
					</tr>
					<tr>
						<td style = "width: 100%" colspan="2">
							<input class="idSubmit" type="submit" value="Create" v-on:click="create"><br>
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
			
			//front validation
			var isValid=true;
				if(document.getElementsByName("brand")[0].value==""){
					document.getElementsByName("brand")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("brand")[0].style.background='lightgray';
				
				if(document.getElementsByName("model")[0].value==""){
					document.getElementsByName("model")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("model")[0].style.background='lightgray';
					
				if(document.getElementsByName("price")[0].value=="" || document.getElementsByName("price")[0].value<0){
					document.getElementsByName("price")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("price")[0].style.background='lightgray';
				if(document.getElementsByName("type")[0].value==""){
					document.getElementsByName("type")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("type")[0].style.background='lightgray';
				if(document.getElementsByName("kind")[0].value==""){
					document.getElementsByName("kind")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("kind")[0].style.background='lightgray';
				if(document.getElementsByName("fuelType")[0].value==""){
					document.getElementsByName("fuelType")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("fuelType")[0].style.background='lightgray';
				if(document.getElementsByName("consumption")[0].value=="" || document.getElementsByName("consumption")[0].value<0){
					document.getElementsByName("consumption")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("consumption")[0].style.background='lightgray';
				if(document.getElementsByName("doors")[0].value=="" || document.getElementsByName("doors")[0].value<0){
					document.getElementsByName("doors")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("doors")[0].style.background='lightgray';
				if(document.getElementsByName("person")[0].value=="" || document.getElementsByName("person")[0].value<0){
					document.getElementsByName("person")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("person")[0].style.background='lightgray';
				if(document.getElementsByName("image")[0].value==""){
					document.getElementsByName("image")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("image")[0].style.background='lightgray';
				
					
			if(isValid)
			{
				this.vehicle.objectId = this.rentACarObject.id;
					axios.post('rest/vehicleService', this.vehicle)
								.then(response => 
								{
									if(response.data==null || response.data==""){
										alert("Creating is impossible!");
									}else{
										//add vehicle to vehicles list to rent object
										this.vehicle = response.data;	//to get new vehicle id
										axios
											.put('rest/objectsService/addVehicle/' + this.vehicle.objectId + '/' + this.vehicle.id + '/' + this.usernameParam)
											.then(response => (router.push(`/managerProfileView/${this.usernameParam}`)))
										}
								});
				
				
			}
				
				
			}
		},
	mounted()
	{
		this.usernameParam = this.$route.params.managerUsername;
		axios
			.get('rest/managerService/getByManager/' + this.usernameParam)
			.then(response => (this.rentACarObject = response.data) )		
	}
	}
);