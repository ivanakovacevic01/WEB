Vue.component("adminProfileView", {
	data: function () {
		    return {
			  admin: {username:"",firstName:"",lastName:"",birthDate:"",gender:"", role:"Admin", blocked:false},
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
						<button class="menuButton" v-on:click = "users">USERS LIST</button>
					</td>
					<td>
						<button class="menuButton"  v-on:click = "addNewManager">ADD NEW MANAGER</button>
					</td>
					<td>
						<button class="menuButton" v-on:click = "addObject">ADD NEW RENT-OBJECT</button>
					</td>
					<td>
						<button class="menuButton" v-on:click = "comments">COMMENTS</button>
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
								<label  class="userDataLabel">{{admin.username}}</label>
								
							</div>
							<div>
								<label class="registrationLabel">First name:</label>
								<label  class="userDataLabel">{{admin.firstName}}</label>
							</div>
							<div>
								<label class="registrationLabel">Last name:</label>
								<label class="userDataLabel">{{admin.lastName}}</label>
							</div>
							<div>
								<label class="registrationLabel">Birth date:</label>
								<label class="userDataLabel">{{admin.birthDate}}</label>
							</div>
							<div>
								<label class="registrationLabel">Gender:</label>
								<label class="userDataLabel">{{admin.gender}}</label>
							</div>
							<div>
								<label class="registrationLabel">Role:</label>
								<label class="userDataLabel">{{admin.role}}</label>
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
		
		users:function()
		{
			event.preventDefault();
			router.push(`/getAll`)
		},
		
		addNewManager : function()
		{
			router.push(`/registrateManager/${this.usernameParam}`);	//sending admin username to be able to return to his profile
		},
		addObject : function()
		{
			router.push(`/createObject/${this.usernameParam}`);	//sending admin username to be able to return to his profile
		},
		goHome : function()
		{
			event.preventDefault();
			router.push(`/homePage/${this.usernameParam}`)
		},
		comments : function()
		{
			event.preventDefault();
			router.push(`/commentView/`+this.usernameParam)
		},
		logOut : function ()
		{
			event.preventDefault();
			this.admin.loggedIn = false;
			axios
					.put('rest/adminService/' + this.usernameParam, this.admin)
					.then(response => (router.push(`/`)))
		}
	},
	mounted()
	{
		this.usernameParam = this.$route.params.username;
		axios
			.get('rest/adminService/getUserByUsername/' + this.usernameParam)
			.then(response => {
				this.admin = response.data;
				this.admin.loggedIn = true;
				axios
					.put('rest/adminService/' + this.usernameParam, this.admin)
					.then(response => {})
			})
		
	}
});