Vue.component("managerProfileView", {
	data: function () {
		    return {
			  manager: {username:"",firstName:"",lastName:"",birthDate:"",gender:"", role:"Manager", blocked:null},
			  usernameParam: ''
		    }
	},
	
	template: ` 
	<div style = "display: flex; flex-direction: column; align-items: center; height: 700px;">
		<div class="adminProfileDataContainer">
			<table style = "height: 100%">
				<tr style = "height: 100px">
					<td>
						<button class="menuButton" v-on:click = "goHome">HOME</button>
					</td>
					<td>
						<button class="menuButton" v-on:click = "editProfile">EDIT PROFILE</button>
					</td>
					<td>
						<button class="menuButton" v-on:click = "showAllVehicles">VEHICLES LIST</button>
					</td>
					<td>
						<button class="menuButton"  v-on:click = "showAllRentals">RENTAL LIST</button>
					</td>
					<td>
						<button class="menuButton"  v-on:click = "showRentObject">RENT OBJECT</button>
					</td>
					<td>
						<button class="menuButton"  v-on:click = "comments">COMMENTS</button>
					</td>
					<td>
						<button class="menuButton" v-on:click = "logOut">LOG OUT</button>
					</td>
				</tr>
				<tr>
					<td colspan="7" style = "text-align: center;">
						<h1>Profile</h1>
						<div>
							<div>
								<label class="registrationLabel">Username:</label>
								<label  class="userDataLabel">{{manager.username}}</label>
								
							</div>
							<div>
								<label class="registrationLabel">First name:</label>
								<label  class="userDataLabel">{{manager.firstName}}</label>
							</div>
							<div>
								<label class="registrationLabel">Last name:</label>
								<label class="userDataLabel">{{manager.lastName}}</label>
							</div>
							<div>
								<label class="registrationLabel">Birth date:</label>
								<label class="userDataLabel">{{manager.birthDate}}</label>
							</div>
							<div>
								<label class="registrationLabel">Gender:</label>
								<label class="userDataLabel">{{manager.gender}}</label>
							</div>
							<div>
								<label class="registrationLabel">Role:</label>
								<label class="userDataLabel">{{manager.role}}</label>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>	  
`
	, 
	methods : {
		editProfile : function()
		{
			event.preventDefault();
			router.push(`/edit/${this.usernameParam}`)
		},
		showAllVehicles()
		{
			event.preventDefault();
			if(this.manager.object==null || this.manager.object=="" || this.manager.object.removed==true)
				alert("No rent a car object")
			else
				(router.push(`/managersVehicles/${this.usernameParam}`));
		},
		goHome : function()
		{
			event.preventDefault();
			router.push(`/homePage/${this.usernameParam}`);
		},
		showAllRentals : function()
		{
			event.preventDefault();
			if(this.manager.object==null || this.manager.object=="" || this.manager.object.removed==true)
				alert("No rent a car object")
			else
				router.push(`/managersRentals/${this.usernameParam}`);
		},
		showRentObject : function()
		{
			if(this.manager.object==null || this.manager.object=="" || this.manager.object.removed==true)
				alert("No rent a car object")
			else
				router.push(`/objectView/${this.manager.object.id}`);	
		},
		comments : function()
		{
			event.preventDefault();
			router.push(`/commentView/`+this.usernameParam)
		},
		logOut : function ()
		{
			event.preventDefault();
			this.manager.loggedIn = false;
			axios
					.put('rest/managerService/' + this.usernameParam, this.manager)
					.then(response => (router.push(`/`)))
		}
		
	},
	mounted()
	{
		this.usernameParam = this.$route.params.username;
		axios
			.get('rest/managerService/getUserByUsername/' + this.usernameParam)
			.then(response => {
				this.manager = response.data;
				this.manager.loggedIn = true;
				axios
					.put('rest/managerService/' + this.usernameParam, this.manager)
					.then(response => {})
				
				})
		
	}
});