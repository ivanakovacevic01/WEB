Vue.component("rentACarObjectsView", {
	data: function () {
		    return {
			  layerToRemove: null,
			  objects: null,
		      naziv: "",
		      tip: "",
		      searchingCity: "",
		      searchingCountry: "",
		      ocjena1: "",
		      ocjena2: "",
		      state:"",
		      selected:"None",
		      manualChecked: false,
		      automaticChecked: false,
		      openedChecked: false,
		      dieselChecked: false,
		      petrolChecked: false,
		      hybridChecked: false,
		      electricChecked: false,
		      mapa:null
		    }
	},
	
	template: ` 
	<div id="userProfile" class="objectsViewContainer">
		<table border="0" style="width: 100%">
			<tr style = "height: 60px">
				<td style = "text-align: center">
					<div style = "text-align: center">
						<button class="loginButton" v-on:click = "login">LOGIN</button>
					</div>
				</td>
			</tr>
		</table>
		
		<table align="center" class="searchTabel">
			<tr>
				<td colspan="4" align="center">
					<h4>FILTER & SEARCH</h4>
				</td>
			</tr>			
			<tr>
				<td>
					<div>
						<table>	
							<tr>
								<td style = "vertical-align: top;">
									<label class="tabelLabel">Vehicle kind:</label><br>
									<input type="checkbox" name="typeCheckbox" v-model="manualChecked"/><label class="registration1Label">Manual</label><br>
									<input type="checkbox" name="typeCheckbox" v-model="automaticChecked"/><label class="registration1Label">Automatic</label>
								</td>
								<td style = "vertical-align: top">						
									<label class="tabelLabel">Fuel type:</label><br>
									<input name="fuelCheckbox" type="checkbox" v-model="dieselChecked"/><label class="registration1Label">Diesel</label><br>
									<input name="fuelCheckbox" type="checkbox" v-model="petrolChecked"/><label class="registration1Label">Petrol</label><br>
									<input name="fuelCheckbox" type="checkbox" v-model="hybridChecked"/><label class="registration1Label">Hybrid</label><br>
									<input name="fuelCheckbox" type="checkbox" v-model="electricChecked"/><label class="registration1Label">Electric</label>
								</td>
								<td style = "vertical-align: top">	
									<label class="tabelLabel">Only opened objects:</label><br>							
									<input name="state" type="checkbox" v-model="openedChecked"/>
									<label class="registration1Label">Opened</label>
								</td>
							</tr>
						</table>				
					</div>
				</td>
				<td style = "width: 50px">
				</td>	
				<td style = "vertical-align: top">
					<table align="center">
						<tr>
							<td><label class="tabelLabel">Name:</label></td>
							<td><input class="registration2Input" type="text" v-model="naziv"/></td>
							
						</tr>
						<tr>
							<td><label class="tabelLabel">Type:</label></td>
							<td><input class="registration2Input" type="text" v-model="tip"/></td>
						</tr>
						<tr>
							<td><label class="tabelLabel">Average grade:</label></td>
							<td><input class="gradeInput" type="number" v-model="ocjena1" placeholder="Min"/>
								<input class="gradeInput" type="number" v-model="ocjena2" placeholder="Max"/>
							</td>
						</tr>	
					</table>	
				</td>
				<td rowspan="3" style = "vertical-align: top; padding-left: 100px">
					<label class="tabelLabel">Location:</label>
					<div id="map" class="searchMap"></div>
				</td>
			</tr>
			<tr>
				<td colspan="7" style = "text-align: center">
					<input class="id1Submit" type="submit" value="Reset" v-on:click="reset"/>
					<input class="id1Submit" type="submit" value="Filter&Search" v-on:click="search"/>
				</td>
			</tr>
		
		</table>
		<div style = "text-align: right; margin-right: 45px">
						<label class="registration1Label">SORT:</label>
						<select v-model="selected" class="tabel1Input" v-on:change="changed">
							<option>None</option>
							<option>Sort by: Name ascending</option>
							<option>Sort by: Location ascending</option>
							<option>Sort by: Average grade ascending</option>
							<option>Sort by: Name descending</option>
							<option>Sort by: Location descending</option>
							<option>Sort by: Average grade descending</option>
						</select>
		</div>
		<h1 class="rentObjectsViewTitle">Rent a car objects</h1>
			<table align = "center" class="objectsTabel">
				<tr class="rowBackground">
					<th class = "thLabel">NAME</th>
					<th class = "thLabel">GRADE</th>
					<th class = "thLabel">LOGO</th>
					<th class = "thLabel">LOCATION</th>
				</tr>
				<tr v-for="(l,index) in objects" v-if="l.removed!=true" class="rowBackground">		
					<td style = "text-align: center"><label class="objectsLabel"><button class="objectDetailsButton" v-on:click = "viewObject(l.id)">{{l.name}}</button></label></td>
					<td style = "text-align: center"><label class="objectsLabel">{{l.grade}}</label></td>
					<td align = "center" style = "text-align: center"><img class="objectsImage" :src="l.logo"/></td>
					<td class="locationContainer">
						<div>
							<table align = "center">
								<tr>
									<td><img :src="l.location.image" width="100" height="100"/></td>
									<td>
										<span>
											<div>
												<label class="objectsLabel">{{l.location.address.street}}</label>
												<label class="objectsLabel">{{l.location.address.number}}</label>
											</div>
											<div>
												<label class="objectsLabel">{{l.location.address.city}}</label>
												<label class="objectsLabel">{{l.location.address.zipCode}}</label>
											</div>
											<div>
												<label class="objectsLabel">{{l.location.latitude}}</label>
												<label class="objectsLabel">{{l.location.longitude}}</label>
											</div>
										</span>
									</td>
									<td align = "right">
										<img src="resources/images/arrow.png" width="50" height="50"/>
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>	
			</table>
			
	</div>		  
`
	, 
	methods : {
		
		login : function()
		{
			router.push(`/login`);
		},
		search : function()
		{
			event.preventDefault();
			axios
				.get('rest/objectsService')
				.then(response => 
				{	
					//searching
					(this.objects = response.data.filter(object => (this.naziv=="" || object.name.toLowerCase().includes(this.naziv.toLowerCase()))&&
					(this.ocjena1=="" || object.grade>=parseFloat(this.ocjena1)) && (this.ocjena2=="" || object.grade<=parseFloat(this.ocjena2))));
					;
					this.objects = this.objects.filter(object => (object.vehicles!=null && (this.tip=="" || object.vehicles.some(vehicle => vehicle.type.toLowerCase().includes(this.tip.toLowerCase())))))	//if both unchecked -> cool
					//searching by location
					if(this.searchingCountry!="" && this.searchingCity!="" && this.searchingCountry!=null && this.searchingCity!=null)
					{
						this.objects = this.objects.filter(object => (object.location.address.country.toLowerCase()==this.searchingCountry.toLowerCase()) && (object.location.address.city.toLowerCase()==this.searchingCity.toLowerCase()));
					}
					else if((this.searchingCountry!="" && this.searchingCity=="") || (this.searchingCountry!="" && this.searchingCity==null))
					{
						this.objects = this.objects.filter(object => (object.location.address.country.toLowerCase()==this.searchingCountry.toLowerCase()));
					}
					
					
					//filtering by kind
					this.objects = this.objects.filter(object => (this.manualChecked==true && object.vehicles!=null && object.vehicles.some(vehicle => vehicle.kind.toLowerCase()=='manual'))
														|| (this.automaticChecked==true && object.vehicles!=null && object.vehicles.some(vehicle => vehicle.kind.toLowerCase()=='automatic'))
														|| (this.manualChecked==false && this.automaticChecked==false) 	//if both unchecked -> cool
					)
			
					//filtering by fuel type
					this.objects = this.objects.filter(object => (this.dieselChecked==true && object.vehicles!=null && object.vehicles.some(vehicle => vehicle.fuelType.toLowerCase()=='diesel'))
														|| (this.petrolChecked==true && object.vehicles!=null && object.vehicles.some(vehicle => vehicle.fuelType.toLowerCase()=='petrol'))
														|| (this.hybridChecked==true && object.vehicles!=null && object.vehicles.some(vehicle => vehicle.fuelType.toLowerCase()=='hybrid'))
														|| (this.electricChecked==true && object.vehicles!=null && object.vehicles.some(vehicle => vehicle.fuelType.toLowerCase()=='electric'))
														|| (this.dieselChecked==false && this.petrolChecked==false && this.hybridChecked==false && this.electricChecked==false) 	//if all unchecked -> cool
					)
					
					//filtering only opened rent a car objects
					this.objects = this.objects.filter(object => (this.openedChecked==true && object.state.toLowerCase().includes("opened"))
														|| (this.openedChecked==false))
					
					if(this.objects!=null)
						this.objects=this.objects.filter(object => object.removed!=true)
				})
		
			
		},
		reset : function()
		{
			this.objects=this.sorted;
			this.selected=null;
			this.naziv="";
			this.tip="";
			this.searchingCity = "";
			this.searchingCountry = "";
			this.ocjena1="";
			this.ocjena2="";
			this.manualChecked = false;
		    this.automaticChecked = false;
		    this.openedChecked = false;
		    this.dieselChecked = false;
		    this.petrolChecked = false;
		    this.hybridChecked = false;
		    this.electricChecked = false;
		    if(this.layerToRemove!=null)
  				this.mapa.removeLayer(this.layerToRemove);

			
			axios
				.get('rest/objectsService')
				.then(response => {
					(this.objects = response.data)
					if(this.objects!=null)
						this.objects=this.objects.filter(object => object.removed!=true)
				})
		},
		viewObject : function(id)
		{
			router.push(`/objectView/${id}`);
		},
		changed:function(){
			
			if(this.selected=="Sort by: Name ascending"){
				function compare(a, b) {
     		 	if (a.name < b.name)
        			return -1;
      			if (a.name > b.name)
        			return 1;
      			return 0;
      			}
      			this.objects.sort(compare);
			}
			else if(this.selected=="Sort by: Name descending"){
				function compare(a, b) {
     		 	if (a.name < b.name)
        			return 1;
      			if (a.name > b.name)
        			return -1;
      			return 0;
      			}
      			this.objects.sort(compare);
			}
			else if(this.selected=="None"){
      			axios
			.get('rest/objectsService')
			.then(response => (this.objects = response.data))
			}
		   	else if(this.selected=="Sort by: Average grade descending"){
				function compare(a, b) {
     		 	if (a.grade < b.grade)
        			return 1;
      			if (a.grade > b.grade)
        			return -1;
      			return 0;
      			}
      			this.objects.sort(compare);
			}
			else if(this.selected=="Sort by: Average grade ascending"){
				function compare(a, b) {
     		 	if (a.grade < b.grade)
        			return -1;
      			if (a.grade > b.grade)
        			return 1;
      			return 0;
      			}
      			this.objects.sort(compare);
			}
			else if(this.selected=="Sort by: Location ascending"){
				function compare(a, b) {
     		 	if (a.location.address.city < b.location.address.city)
        			return -1;
      			if (a.location.address.city > b.location.address.city)
        			return 1;
      			return 0;
      			}
      			this.objects.sort(compare);
			}
			else if(this.selected=="Sort by: Location descending"){
				function compare(a, b) {
     		 	if (a.location.address.city < b.location.address.city)
        			return 1;
      			if (a.location.address.city > b.location.address.city)
        			return -1;
      			return 0;
      			}
      			this.objects.sort(compare);
			}
			if(this.objects!=null)
				this.objects=this.objects.filter(object => object.removed!=true)
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
				.then((json) => {
					console.log(json);
					
					var address = json.address;
					
					this.searchingCity = address.city || address.town || address.village;
					this.searchingCountry = address.country;
				})
				.catch((error) =>
				{
					console.log(error)
				});
		}
	},
	mounted()
	{
		this.state=this.$route.params.state;
		
		axios
			.put('rest/adminService/logOutAll')
			.then(response =>
			{
				axios
					.put('rest/managerService/logOutAll')
					.then(response =>
					{
						axios
							.put('rest/userService/logOutAll')
							.then(response =>
							{
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
							        center: ol.proj.fromLonLat([18.2319923,42.8860038]), // Map center, BiH centered :D
							        zoom: 9, // zoom level
							      }),
							    });
							    
						this.mapa = map;
							    
						var addingLayer = new ol.layer.Vector(
								{
									source: new ol.source.Vector(
									{
									})
								});
					    
						map.on('click', (event) => {
							
							//remove last layer from map
							map.removeLayer(addingLayer);
							
							//get location
							var coordinate = ol.proj.toLonLat(event.coordinate);
							this.getLocationDetails(coordinate);
							addingLayer.getSource().clear();
							
							//marker selected location
							var marker = new ol.Feature({
								geometry: new ol.geom.Point(event.coordinate),
							});
							addingLayer.getSource().addFeature(marker);
							
							map.addLayer(addingLayer);
							
							//needed for reset button (to delete last layer)
							this.layerToRemove = addingLayer;
						})
						
					
						if(this.naziv=="" && this.tip=="" && this.ocjena1=="" && this.ocjena2=="" && this.state==null){
							axios
								.get('rest/objectsService')
								.then(response => {
									(this.objects = response.data)
									if(this.objects!=null)
										this.objects=this.objects.filter(object => object.removed!=true)
								})
						}
							})
						
							})
						
				
					})
				

		
	}
});