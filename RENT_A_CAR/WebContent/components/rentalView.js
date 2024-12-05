Vue.component("rentalView", {
	data: function () {
		    return {
			  rental:null,
		      username:"",
		      user:{orders: {}},
		      rentObject: "",
		      minPrice: "",
		      maxPrice: "",
		      startDate: "",
		      endDate: "",
		      selected:"None",
		      commentText: "",
		      commentGrade: null,
		      hidden: true,
		      idForComment: "",
		      objectIdForComment: "",
		      statusForComment: "",
		      points:null,
		      type:null,
		      forGold:null,
		      forBronze:null,
		      forSilver:null
		      
		    }
	},
	
	template: ` 
	<div align="center">
		
		<button class="loginButton" v-on:click = "rent">New rental</button>
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
							<td><label class="tabelLabel">Rent a car object:</label></td>
							<td><input class="registration3Input" type="text" v-model="rentObject"/></td>				
						</tr>
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
							<option>Sort by: Rent object ascending</option>
							<option>Sort by: Price ascending</option>
							<option>Sort by: Date ascending</option>
							<option>Sort by: Rent object descending</option>
							<option>Sort by: Price descending</option>
							<option>Sort by: Date descending</option>
						</select>
		</div>
		
		<h2>All rentals</h2>
		<table class="vehiclesTabel" align="center" style = "width: 95%; margin-top: 2px">
			
					<tr class="rowBackground">
						<th class = "thLabel">Object</th>
						<th class = "thLabel">Start time</th>
						<th class = "thLabel">Duration</th>
						<th class = "thLabel">Price</th>
						<th class = "thLabel">Status</th>
						<th class = "thLabel">Vehicles</th>
						<th class ="thLabel">Cancel</th>
						<th class ="thLabel">Comment</th>
						<th class ="thLabel">Declining reason</th>
					</tr>
					<tr class="rowBackground" style="height: 60px;" v-for = "(v, index) in rental">
						<td style = "text-align: center"><label class="objectsLabel">{{v.object.name}}</label></td>
						<td style = "text-align: center"><label class="objectsLabel">{{v.startDateTime}}</label></td>
						<td style = "text-align: center"><label class="objectsLabel">{{v.duration/3600.0}} h</label></td>
						<td style = "text-align: center"><label class="objectsLabel">{{v.price}} EUR</label></td>
						<td style = "text-align: center"><label class="objectsLabel">{{v.status}}</label></td>
						<td style = "text-align: center">
							<div style="height: 100%;">
								<button class="confirmButton" v-on:click = "viewVehicles(v.id)">Vehicles</button>
							</div>
						</td>
						<td style = "text-align: center">
							<div style="height: 100%;">
								<button class="declineButton" v-on:click = "cancel(v.id, v.status, v.price)" v-bind:disabled="v.status!='Processing'">Cancel</button>
							</div>
						</td>
						<td style = "text-align: center">
							<div style="height: 100%;">
								<button class="commentButton" v-on:click = "comment(v.id, v.status, v.objectId)" v-bind:disabled="v.status!='Returned'">Comemnt</button>
							</div>
						</td>
						<td style = "text-align: center" v-if="v.status=='Declined'"><textarea class="commentTextStyle" readonly>{{v.reason}}</textarea></td>
					</tr>
					
				
		</table>
		<div v-bind:hidden="this.hidden==true" style="text-align:center; margin-top: 5px">
				<label class="registration2Label">Comment and grade:</label><br>
				<textarea class="reasonStyle" name="commentTextInput" v-model="commentText"></textarea><br>
				<input class="registration2Input" style="border: 3px solid teal; margin-top: 2px" type="number" name="commentGradeInput" v-model="commentGrade"><br>
				<button class="approveButton" v-on:click = "confirmComment">Confirm</button>
				<button class="declineButton" v-on:click = "cancelComment">Cancel</button>
		</div>
		
	</div>
`
	, 
	methods : {
		rent:function()
		{
			event.preventDefault();
			router.push(`/rentACarObject/`+this.username)
		},
		viewVehicles:function(id)
		{
			event.preventDefault();
			router.push(`/vehiclesView/`+id)
		},
		cancelComment : function()
		{
			event.preventDefault();
			this.hidden=true;
			this.commentGrade = null;
			this.commentText = "";
			 document.getElementsByName("commentTextInput")[0].style.background='white';
		    document.getElementsByName("commentGradeInput")[0].style.background='white';
		},
		comment : function(id, status, objectId)
		{
			event.preventDefault();
			axios
				.get('rest/commentService/getByOrderId/' + id)
				.then(response =>
				{
					if(response.data!=null && response.data!="")
						alert("Comment already exists!")
					else
					{
						this.hidden=false;
						this.idForComment=id;
						this.objectIdForComment = objectId;
						this.statusForComment = status;
					}
				})
			
			
		},
		
		cancel: function(id, status, price)
		{
			event.preventDefault();	
			if(status=='Processing')
			{
				axios
					.put('rest/orderService/cancel/'+id+'/'+status)	//sending status for backend validation
					.then(response => {
						if(response.data==false)
							alert("Can't be canceled.'") 
						else
						{
							
							//update user
							
							axios
								.get('rest/orderService/getByUsername/' + this.user.username)
								.then(response =>{
								 (this.rental = response.data);
								 this.user.orders = this.rental;
								 this.user.collectedPoints -= (price/1000.0*133*4);
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
									 axios
										.put('rest/userService/' + this.username, this.user)
										.then(response =>
										{
											this.user=response.data;
											this.rental = this.user.orders;
										})
									})
							})
							
						}
					})
			}
			
		},
		reset : function()
		{
			this.rentObject = "";
		    this.minPrice = "";
		    this.maxPrice = "";
		    this.startDate = "";
		    this.endDate = "";
		    this.selected = "";
		    document.getElementsByName("commentTextInput")[0].style.background='white';
		    document.getElementsByName("commentGradeInput")[0].style.background='white';
			axios
				.get('rest/orderService/getByUsername/' + this.user.username)
				.then(response =>{
				 (this.rental = response.data)
			})
			
		},
		search : function()
		{
			event.preventDefault();
			axios
				.get('rest/orderService/getByUsername/' + this.user.username)
				.then(response => 
				{	
					//searching
					(this.rental = response.data.filter(rent => (this.rentObject=="" || rent.object.name.toLowerCase().includes(this.rentObject.toLowerCase()))&&
					(this.minPrice=="" || this.minPrice==null || rent.price>=parseFloat(this.minPrice)) && (this.maxPrice=="" || this.minPrice==null || rent.price<=parseFloat(this.maxPrice)) && 
					(this.startDate=="" || rent.startDateTime.split('T')[0] == this.startDate) && (this.endDate=="" || (new Date(new Date(rent.startDateTime).getTime() + rent.duration * 1000)).toISOString().split('T')[0]==this.endDate)));
					;				
					
				})
		
			
		},
		sort:function(){
			
			if(this.selected=="Sort by: Rent object ascending"){
				function compare(a, b) {
     		 	if (a.object.name < b.object.name)
        			return -1;
      			if (a.object.name > b.object.name)
        			return 1;
      			return 0;
      			}
      			this.rental.sort(compare);
			}
			else if(this.selected=="Sort by: Rent object descending"){
				function compare(a, b) {
     		 	if (a.object.name < b.object.name)
        			return 1;
      			if (a.object.name > b.object.name)
        			return -1;
      			return 0;
      			}
      			this.rental.sort(compare);
			}
			else if(this.selected=="None"){
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
		
		confirmComment : function()
		{
			event.preventDefault();
			//front validation
			var isValid=true;
			if(document.getElementsByName("commentTextInput")[0].value=="" || document.getElementsByName("commentTextInput")[0].value==null)
			{
				isValid = false;
				document.getElementsByName("commentTextInput")[0].style.background='red';
			}
			else
				document.getElementsByName("commentTextInput")[0].style.background='white';
			if(document.getElementsByName("commentGradeInput")[0].value=="" || document.getElementsByName("commentGradeInput")[0].value==null)
			{
				isValid = false;
				document.getElementsByName("commentGradeInput")[0].style.background='red';
			}
			else if(document.getElementsByName("commentGradeInput")[0].value.indexOf('.')!==-1){
				isValid = false;
				document.getElementsByName("commentGradeInput")[0].style.background='red';
			}
			else if(parseInt(document.getElementsByName("commentGradeInput")[0].value)<1 || parseInt(document.getElementsByName("commentGradeInput")[0].value)>5){
				isValid = false;
				document.getElementsByName("commentGradeInput")[0].style.background='red';
			}
			else
				document.getElementsByName("commentGradeInput")[0].style.background='white';
			
			
			if(isValid)
			{
						if(this.statusForComment=='Returned')
						{
							var comment = {}
							comment.orderId = this.idForComment;
							comment.text = this.commentText;
							comment.customer = this.user;
							comment.grade = this.commentGrade;
							comment.status = 'Processing';
							
							axios	
								.get('rest/objectsService/getObject/' + this.objectIdForComment)
								.then(response =>
								{
									if(response.data==null || response.data==""){
										alert("Can't add comment.'")
										this.hidden=true;
										this.commentGrade = null;
										this.commentText = "";
									}	
									else
									{
										comment.object = response.data; //now, we have completed comment
										
										axios
											.post('rest/commentService/ifReturned/'+this.statusForComment, comment)	//sending status for backend validation
											.then(response => {
												if(response.data==null || response.data=="")
												{
													
													alert("Can't add comment.'") 
												}
													
												else
												{
													
													//comment is added
													alert("Comment written. Manager can approve/decline it.")
													
												}
												this.hidden=true;
												this.commentGrade = null;
												this.commentText = "";
											})
										
										
									}
								})
							
							
					}
					else
					{
						alert("Can't add comment.'")
						this.hidden=true;
						this.commentGrade = null;
						this.commentText = "";
					}
				}
			
	  }
		
	},
	mounted()
	{
		this.username=this.$route.params.username;
		axios
			.get('rest/userService/getUserByUsername/' + this.username)
			.then(response => {
				this.user=response.data;
				axios
					.get('rest/orderService/getByUsername/' + this.user.username)
					.then(response =>{
					 (this.rental = response.data)
				})
			})
				

		
	}
});