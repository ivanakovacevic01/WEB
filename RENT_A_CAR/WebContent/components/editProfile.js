Vue.component("editProfileView", {
	data: function () {
		    return {
			  user: {username:"",firstName:"",lastName:"",birthDate:"",gender:"", password:""},
			  usernameParam: '',
			  password: ''
		      
		    }
	},
	
	template: ` 
	<div id="registrationForm">
		<div class="registrationFormContainer">
			<div style="height: 100px;">
				<h1>Edit profile</h1>
			</div>
			<div>
				<form id="formInputSignUp">
					<div>
						<label class="registrationLabel">Username:</label>
						<input class="registrationInput" type="text" name="username2" v-model = "user.username">
					</div>
					<div>
						<label class="registrationLabel">Password:</label>
						<input class="registrationInput" type="password" name="password12">
					</div>
					<div>
						<label class="registrationLabel">Password:</label>
						<input class="registrationInput" type="password" name="password22" v-model = "password">
					</div>
					<div>
						<label class="registrationLabel">First name:</label>
						<input class="registrationInput" type="text" name="firstName2" v-model = "user.firstName">
					</div>
					<div>
						<label class="registrationLabel">Last name:</label>
						<input class="registrationInput" type="text" name="lastName2" v-model = "user.lastName">
					</div>
					<div>
						<label class="registrationLabel">Birth date:</label>
						<input class="registrationInput" type="Date" name="birthDate2" v-model = "user.birthDate">
					</div>
					<div>
						<table align="center">
							<tr>
								<td>		
									<label class="registrationLabel">Gender:</label>
								</td>
								<td  style="width: 320px">
									<table>
										<tr>
											<td style="width: 320px">
												<input class="radioStyle" type="radio" name="gender2" v-model = "user.gender" value="Male">
												<label class="radioLabel">Male</label>
											</td>
										</tr>
										<tr>
											<td>
												<input class="radioStyle" type="radio" name="gender2" v-model = "user.gender" value="Female">
												<label class="radioLabel">Female</label>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</div>	
				<input class="idSubmit" type="submit" value="Confirm" v-on:click = "confirm">
				</form>
			</div>
		</div> 
	</div>
`
	, 
	methods : {
		confirm : function()
		{
			event.preventDefault();
			//front validation
			var isValid=true;
				if(document.getElementsByName("username2")[0].value==""){
					document.getElementsByName("username2")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("username2")[0].style.background='lightgray';
				if(document.getElementsByName("firstName2")[0].value==""){
					document.getElementsByName("firstName2")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("firstName2")[0].style.background='lightgray';
				if(document.getElementsByName("lastName2")[0].value==""){
					document.getElementsByName("lastName2")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("lastName2")[0].style.background='lightgray';
				if(document.getElementsByName("birthDate2")[0].value==""){
					document.getElementsByName("birthDate2")[0].style.background='red';
					isValid=false;
				}
				else
					document.getElementsByName("birthDate2")[0].style.background='lightgray';
				if(document.getElementsByName("password12")[0].value!=document.getElementsByName("password22")[0].value){
					document.getElementsByName("password12")[0].style.background='red';
					document.getElementsByName("password22")[0].style.background='red';
					isValid=false;
				}
				else{
					document.getElementsByName("password12")[0].style.background='lightgray';
					document.getElementsByName("password22")[0].style.background='lightgray';
				}
				if(isValid)
				{
					if(this.password!="")
						this.user.password = this.password;
						
					axios
						.get('rest/allUsersService/usernameExistsForEditing1/' + this.user.username + '/' + this.usernameParam)
						.then(response =>
						{
							if(response.data==true)
								alert("Username alredy exists!")
							else if(this.user.role=="Customer")
								axios.put('rest/userService/' + this.usernameParam, this.user).
								then(response => 
								{
									if(response.data==null || response.data==""){
										alert("Editing is impossible!");
									}else
										(router.push(`/userProfileView/${this.user.username}`))
								});
							else if(this.user.role=="Admin")
								axios.put('rest/adminService/' + this.usernameParam, this.user).
								then(response => 
								{
									if(response.data==null || response.data==""){
										alert("Editing is impossible!");
									}else
										(router.push(`/adminProfileView/${this.user.username}`))
								});
							else if(this.user.role=="Manager")
								axios.put('rest/managerService/' + this.usernameParam, this.user).
								then(response => 
								{
									if(response.data==null || response.data==""){
										alert("Editing is impossible!");
									}else
										(router.push(`/managerProfileView/${this.user.username}`))
								});
						});
								
				}
					
			
			
		}
		
	},
	mounted()
	{
		this.usernameParam = this.$route.params.username;
		//uzeti ulogu
		axios
			.get('rest/allUsersService/findUser/' + this.usernameParam)
			.then(response => 
			{
				if(response.data=="Customer")
					axios
						.get('rest/userService/getUserByUsername/' + this.usernameParam)
						.then(response => (this.user = response.data))
				else if(response.data=="Admin")
					axios
						.get('rest/adminService/getUserByUsername/' + this.usernameParam)
						.then(response => (this.user = response.data))
				else if(response.data=="Manager")
					axios
						.get('rest/managerService/getUserByUsername/' + this.usernameParam)
						.then(response => (this.user = response.data))
					
			})
		
	}
});