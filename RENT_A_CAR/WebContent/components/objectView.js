Vue.component("objectView", {
	data: function () {
		    return {
			  object: {name:"",startTime:"", endTime:"",state:"",location:{address:{}},logo:"",grade:""}, //treba jos dodati komentar i listu vozila
			  objectParam: null,
			  gradeExists: null,
			  commentsExist: false,
			  comments: null,
			  logged: null,
			  manager: null
		      
		    }
	},
	
	template: ` 
	<div>
		<div>					
			<h2>Rent a car object - {{object.name}}<img class="logoImage" :src="object.logo"/></h2>		
			<div>
				<table align="center" class="objectDataTabel">
					<tr>
						<td style = "text-align: center; vertical-align: center; margin-left: 50px; width: 35%">
							<label class="registrationLabel">Name:</label>
							<label class="userDataLabel">{{object.name}}</label>
							<br><br>
							<label class="registrationLabel">Start time:</label>
							<label class="userDataLabel">{{object.startTime}}h</label>
							<br><br>
							<label class="registrationLabel">End time:</label>
							<label class="userDataLabel">{{object.endTime}}h</label>
							<br><br>
							<label class="registrationLabel">State:</label>
							<label class="userDataLabel">{{object.state}}</label>
							<br>
							<div v-bind:hidden = "gradeExists != true">
								<label class="registrationLabel">Grade:</label>
								<label class="userDataLabel">{{object.grade}}</label>
							</div>
						</td>
						<td style="width: 100px"></td>
						<td style = "text-align: left; vertical-align: top; margin-left: 350px; margin-top: 100px;">
								<label class="locationLabel">Location:</label><br>
								
								<div class="locationInObjectContainer">
									<div>
										<table align = "center">
											<tr>
												<td><img :src="object.location.image" width="100" height="100"/></td>
												<td>
													<span>
														<div>
															<label class="objectsLabel">{{object.location.address.street}}</label>
															<label class="objectsLabel">{{object.location.address.number}}</label>
														</div>
														<div>
															<label class="objectsLabel">{{object.location.address.city}}</label>
															<label class="objectsLabel">{{object.location.address.zipCode}}</label>
														</div>
														<div>
															<label class="objectsLabel">{{object.location.latitude}}</label>
															<label class="objectsLabel">{{object.location.longitude}}</label>
														</div>
													</span>
												</td>
												<td align = "right">
													<img src="resources/images/arrow.png" width="50" height="50"/>
												</td>
											</tr>
										</table>
									</div>	
								</div>
								<div id="map" class="objectMap">
								</div>
						</td>
					</tr>
				</table>
				<br>
				
				<h2>Vehicles</h2>
				<table class="vehiclesTabel" align="center">
					<tr class="rowBackground">
						<th class = "thLabel">Brand</th>
						<th class = "thLabel">Model</th>
						<th class = "thLabel">Price</th>
						<th class = "thLabel">Type</th>
						<th class = "thLabel">Kind</th>
						<th class = "thLabel">Fuel type</th>
						<th class = "thLabel">Consumption</th>
						<th class = "thLabel">Doors number</th>
						<th class = "thLabel">Capacity</th>
						<th class = "thLabel">Status</th>
						<th class = "thLabel">Description</th>
						<th class = "thLabel">Image</th>
						</tr>
					<tr v-for = "(v, index) in object.vehicles" v-if = "v.removed!=true" class="rowBackground">
						<td style="text-align: center"><label class="objects1Label">{{v.brand}}</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.model}}</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.price}} EUR</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.type}}</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.kind}}</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.fuelType}}</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.consumption}} l/100 km</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.doorsNumber}}</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.personNumber}}</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.status}}</label></td>
						<td style="text-align: center"><label class="objects1Label">{{v.description}}</label></td>
						<td style="text-align: center"><img class="objectsImage" :src="v.image"/></td>
					</tr>
				</table>
				<br><br><br>
				
				<h2 v-if="this.logged && this.commentsExist">Comments</h2>
				<table v-if="this.logged && this.commentsExist" class="vehiclesTabel" align="center">
					<tr class="rowBackground">
						<th class = "thLabel">CUSTOMER</th>
						<th class = "thLabel">GRADE</th>
						<th class = "thLabel">STATUS</th>
						<th class = "thLabel">COMMENT</th>
						<th class = "thLabel">REMOVE</th>
					</tr>
					<tr v-for="(l,index) in comments" class="rowBackground">
						<td style = "text-align: center"><label class="objectsLabel">{{l.customer.firstName}} {{l.customer.lastName}}</label></td>
						<td style = "text-align: center"><label class="objectsLabel">{{l.grade}}</label></td>
						<td style = "text-align: center"><label class="objectsLabel">{{l.status}}</label></td>
						<td style = "text-align: center"><textarea class="commentTextStyle" readonly>{{l.text}}</textarea></td>
						<td style = "text-align: center">
							<div>
								<button class="declineButton" v-on:click = "removeComment(l.orderId, l.customer.id)" v-if="l.customer.username==logged.username" v-bind:disabled="l.removed==true">Remove</button>
							</div>
						</td>
					</tr>	
				</table>
				<br><br><br>
					
			</div>
			
		</div>
	</div>	  
`
	, 
	methods : {
		removeComment(orderId, customerId)
		{
			var commentToRemove = {removed:""}
			axios
				.get('rest/commentService/getByOrderId/' + orderId)
				.then(response => {
					commentToRemove = response.data;
					commentToRemove.removed = true;
					
					axios
						.put('rest/commentService/edit', commentToRemove)
						.then(response =>
						{
							this.comments = response.data
							if(this.comments!=null)
								this.comments = this.comments.filter(c => c.removed!=true)
								
							//potrebno je azurirati prosjecnu ocjenu
							
									//potrebno je dobaviti objekat
									
										this.object.grade = 0;
										helper=0;
										for(var comment of this.comments){
											
												this.object.grade+=comment.grade;
												helper++;
											
										}
										if(helper!=0)
											this.newObject.grade/=helper
										axios
										.put('rest/objectsService/'+this.object.id,this.object)
										.then(response => 
										{
											this.object=response.data;
											
											//update manager (object)
											this.manager.object = this.object
												axios
												.put('rest/managerService/'+this.manager.username,this.manager)
												.then(response => this.manager=response.data)
										})
									
									
				
						})
				})
		}
		
	},
	mounted()
	{
		this.objectParam = this.$route.params.objectId;
		
		const map = new ol.Map({
			      target: 'map', 
			      layers: [
			        new ol.layer.Tile({
			          source: new ol.source.OSM(), 
			        }),
			      ],
			      view: new ol.View({
			        
			        center: ol.proj.fromLonLat([0,0]), 
			        zoom: 8, // Zoom level
			      }),
			    });
		axios
			.get('rest/objectsService/getObject/' + this.objectParam)
			.then(response => 
			{
				this.object = response.data;
				this.gradeExists = false;
				if(this.object.grade!=0)
					this.gradeExists=true;
					
		
				var sign = new ol.Feature(
				{
				geometry: new ol.geom.Point(ol.proj.fromLonLat([this.object.location.latitude, this.object.location.longitude]))
				});
				
				//add icon for location sign
				var locationSign = new ol.style.Icon(
				{
					src: 'https://cdn.rawgit.com/openlayers/ol3/master/examples/data/icon.png',
					anchor: [0.5, 1]
				});
				
				sign.setStyle(new ol.style.Style({
					image: locationSign
				}));
				
				var addingLayer = new ol.layer.Vector(
				{
					source: new ol.source.Vector(
					{
						features: [sign]
					})
				});
				
				map.addLayer(addingLayer);
				map.getView().fit(addingLayer.getSource().getExtent(), {
					
				});
				
				map.getView().setZoom(8);
				
				axios
					.get('rest/allUsersService/isLoggedIn')
					.then(response => {
						this.logged = response.data;
						
						if(this.logged)
						axios
							.get('rest/commentService/getApprovedCommentsByObjectId/' + this.object.id)
							.then(response =>
							{
								if(response.data.length > 0)	//0 approved comments
								{
									this.commentsExist = true;
								}
		
								this.comments = response.data;
								if(this.comments!=null)
									this.comments = this.comments.filter(c => c.removed!=true)
									
								axios
								
												.get('rest/managerService/getManagerByObjectsId/'+ this.object.id)
												.then(response =>
												{
													this.manager = response.data;
												})
							});
					})
							
				
				
			    
				
				
			   
						
		
		
	})
	}
});