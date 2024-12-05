Vue.component("userProfileView", {
	data: function () {
		    return {
			  user: {username:"",firstName:"",lastName:"",birthDate:"",gender:"",role:"Customer"},
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
						<button class="menuButton" v-on:click = "allRental">ALL RENTALS</button>
					</td>
					<td>
						<button class="menuButton" v-on:click = "logOut">LOG OUT</button>
					</td>						
				</tr>
				<tr>
					<td colspan="4" style = "text-align: center;">
						<h1>Profile</h1>
						<div>
							<div>
								<label class="registrationLabel">Username:</label>
								<label  class="userDataLabel">{{user.username}}</label>
								
							</div>
							<div>
								<label class="registrationLabel">First name:</label>
								<label  class="userDataLabel">{{user.firstName}}</label>
							</div>
							<div>
								<label class="registrationLabel">Last name:</label>
								<label class="userDataLabel">{{user.lastName}}</label>
							</div>
							<div>
								<label class="registrationLabel">Birth date:</label>
								<label class="userDataLabel">{{user.birthDate}}</label>
							</div>
							<div>
								<label class="registrationLabel">Gender:</label>
								<label class="userDataLabel">{{user.gender}}</label>
							</div>
							<div>
								<label class="registrationLabel">Role:</label>
								<label class="userDataLabel">{{user.role}}</label>
							</div>
							<div>
								<label class="registrationLabel">Points:</label>
								<label  class="userDataLabel">{{user.collectedPoints}}</label>
								
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
		goHome : function()
		{
			event.preventDefault();
			router.push(`/homePage/${this.usernameParam}`);
		},
		allRental:function(){
			event.preventDefault();
			router.push(`/rentalView/`+this.usernameParam)
		},
		logOut : function ()
		{
			event.preventDefault();
			this.user.loggedIn = false;
			axios
					.put('rest/userService/' + this.usernameParam, this.user)
					.then(response => (router.push(`/`)))
		}
		
	},
	mounted()
	{
		this.usernameParam = this.$route.params.username;
		axios
			.get('rest/userService/getUserByUsername/' + this.usernameParam)
			.then(response => {
				this.user = response.data;
				this.user.loggedIn = true;
				
				//log in user (save to file)
				 axios
					.put('rest/userService/' + this.usernameParam, this.user)
					.then(response => {})
				})
		
	}
});