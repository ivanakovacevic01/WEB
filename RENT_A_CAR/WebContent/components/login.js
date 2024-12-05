Vue.component("logIn", {
	data: function () {
		    return {
		      title: "Rent a car",
		      username:null,
		      password:null,
		      
		    }
	},
	
	template: ` 
	<div id="loginPage">
		<div class="col">
			<img class="image" src="resources/images/car.png"/>
		</div>
		<div class="col" id="loginForm">
			<div style="height: 200px;">
				<h1>{{title}}</h1>
			</div>
			<form>
				<div>
					<label class="registrationLabel">Username:</label>
					<input class="registrationInput" type="text" name="username1" v-model = "username">
				</div>
				<div>
					<label class="registrationLabel">Password:</label>
					<input class="registrationInput" type="password" name="password11" v-model="password"> 
				</div>
				<input class="idSubmit" type="submit" value="Log in" v-on:click = "login">
			</form>
			<br><br><br><br>
			<hr style="width: 500px; height: 2px; background-color: teal; border: none;">
			<br>
			<div style = "text-align: center">
				<label id="linkLabel">Don't have an account?</label>
				<a id="linkSignUp" href="#" v-on:click = "signUp">Sign up</a>
			</div>
			<br><br>
		</div>
	</div>		  
`
	, 
	methods : {
		login : function () {
			event.preventDefault();
			var isValid=true;
			if(document.getElementsByName("username1")[0].value==""){
					document.getElementsByName("username1")[0].style.background='red';
					isValid=false;
			}
			else
				document.getElementsByName("username1")[0].style.background='lightgray';
			if(document.getElementsByName("password11")[0].value==""){
				document.getElementsByName("password11")[0].style.background='red';
				isValid=false;
			}
			else
				document.getElementsByName("password11")[0].style.background='lightgray';
			if(isValid){
				//pronalazimo da li postoji
				axios
					.get('rest/allUsersService/userExists/' + this.username + '/' + this.password)
					.then(response=>
					{
						if(response.data==false)
							alert("User doesn't exist.");
						else
							{
								//pronadji ulogu
								
								axios
								.get('rest/allUsersService/isBlocked/' + this.username)
								.then(response=>
								{
									if(response.data==true)
										alert("You are blocked.");
									else
										{
											//pronadji ulogu
											axios
												.get('rest/allUsersService/findUser/' + this.username)
												.then(response =>
												{
													if(response.data=="Customer")
													{
														axios
															.get('rest/userService/getUserByUsername/' + this.username)
															.then(response=> {
																if(response.data.removed==true)
																	alert("User doesn't exist.'")
																else
																(router.push(`/userProfileView/${this.username}`))
																})
													}
														
														
													else if(response.data=="Admin")
														(router.push(`/adminProfileView/${this.username}`))
													else if(response.data=="Manager")
													{
														axios
															.get('rest/managerService/getUserByUsername/' + this.username)
															.then(response=> {
																if(response.data.removed==true)
																	alert("User doesn't exist.'")
																else
																	(router.push(`/managerProfileView/${this.username}`))
																})
													}
														
												}
												)
										}
										
								})
								
								
							}
							
					})
				}
			}
				
				
				
				,
			
		signUp : function(){
			router.push(`/signUpForm`);
		}
	},
	mounted()
	{
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
							})
					})
			});
	}
});