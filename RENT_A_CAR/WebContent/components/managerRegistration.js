Vue.component("registrateManager", {
	data: function () {
		    return {
		      title: "Add new manager - form",
		      usernameParam: "",
		      user: { firstName: '', lastName: '', username: '', password: '', gender: 'Male', birthDate: null}
		    }
	},
	
	
	template: ` 
<div id="registrationForm">
	<div class="registrationFormContainer">
		<div style="height: 100px;">
			<h1>{{title}}</h1>
		</div>
		<div>
			<form id="formInputSignUp">
				<div>
					<label class="registrationLabel">Username:</label>
					<input class="registrationInput" type="text" name="username" v-model = "user.username">
				</div>
				<div>
					<label class="registrationLabel">Password:</label>
					<input class="registrationInput" type="password" name="password1">
				</div>
				<div>
					<label class="registrationLabel">Password:</label>
					<input class="registrationInput" type="password" name="password2" v-model = "user.password">
				</div>
				<div>
					<label class="registrationLabel">First name:</label>
					<input class="registrationInput" type="text" name="firstName" v-model = "user.firstName">
				</div>
				<div>
					<label class="registrationLabel">Last name:</label>
					<input class="registrationInput" type="text" name="lastName" v-model = "user.lastName">
				</div>
				<div>
					<label class="registrationLabel">Birth date:</label>
					<input class="registrationInput" type="Date" name="birthDate" v-model = "user.birthDate">
				</div>
				<div>
					<table align="center">
						<tr>
							<td>
								<label class="registrationLabel">Gender:</label>
							</td>
							<td style="width: 320px">
								<table>	
									<tr>
										<td style="width: 320px">
											<input class="radioStyle" type="radio" name="gender" v-model = "user.gender" value="Male">
											<label class="registrationLabel">Male</label>
										</td>
									</tr>
									<tr>
										<td>
											<input class="radioStyle" type="radio" name="gender" v-model = "user.gender" value="Female">
											<label class="registrationLabel">Female</label>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
					<input class="idSubmit"type="submit" value="Sign up" v-on:click = "registrate">
			</form>
		</div>
	</div>
</div>		  
`
	, 
	methods : {
		registrate : function () {
			event.preventDefault();
			
			
			//front
				var isValid=true;
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
				if(isValid)
				{
					//validation if username alredy exists
					axios
						.get('rest/allUsersService/usernameExists1/' + this.user.username)
						.then(response =>
						{
							if(response.data==true)
								alert("Username alredy exists!")
							else
								axios.post('rest/managerService', this.user)
								.then(response => 
								{
									if(response.data==null || response.data==""){
										alert("Registration is impossible!");
									}else
										(axios.get('rest/managerService').then(response => (router.push(`/adminProfileView/${this.usernameParam}`))))
								});
						}
						)
				}
				
			}
		},
	mounted()
	{
		this.usernameParam = this.$route.params.adminUsername;		
	}
	}
);