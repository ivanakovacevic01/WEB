Vue.component("commentsView", {
	data: function () {
		    return {
			  comments:[],
			  usernameParam:"",
			  role:"",
			  manager:true,
			  object:[],
			  comment:null,
			  newObject:[],
			  managerr:[]
		    }
	},
	
	template: ` 
	<div id="userProfile" class="objectsViewContainer">
		
		<h2>Comments</h2>
		
		<table class="vehiclesTabel" align="center" style = "width: 95%; margin-top: 2px">
			<tr class="rowBackground">
				<th class = "thLabel">OBJECT</th>
				<th class = "thLabel">CUSTOMER</th>
				<th class = "thLabel">GRADE</th>
				<th class = "thLabel">STATUS</th>
				<th class = "thLabel">COMMENT</th>
				<th class = "thLabel" v-if="this.manager==true">APPROVE</th>
				<th class = "thLabel" v-if="this.manager==true">DECLINE</th>
			</tr>
			<tr v-for="(l,index) in comments" class="rowBackground" v-if = "l.removed!=true">
				<td style = "text-align: center"><label class="objectsLabel">{{l.object.name}}</label></td>
				<td style = "text-align: center"><label class="objectsLabel">{{l.customer.firstName}} {{l.customer.lastName}}</label></td>
				<td style = "text-align: center"><label class="objectsLabel">{{l.grade}}</label></td>
				<td style = "text-align: center"><label class="objectsLabel">{{l.status}}</label></td>
				<td style = "text-align: center"><textarea class="commentTextStyle" readonly>{{l.text}}</textarea></td>
				<td style = "text-align: center" v-if="manager==true">
							<div>

								<button class="approveButton" v-on:click = "approve(l.orderId,l.object.id)" v-if="l.object.id==object.id" v-bind:disabled="l.status!='Processing'">Approve</button>

							</div>
						</td>
				<td style = "text-align: center"  v-if="manager==true">
							<div>
								<button class="declineButton" v-on:click = "decline(l.orderId)" v-if="l.object.id==object.id" v-bind:disabled="l.status!='Processing'">Decline</button>
							</div>
						</td>
			</tr>	
		</table>		
	</div>		  
`
	, 
	methods : {
		approve:function(id,objectId){
			var helper=0
			for(var c of this.comments){
				if(c.status=='Processing' && c.orderId==id){
					c.status='Approved'
					this.comment=c
				}
			}
			//potrebno je azurirati prosjecnu ocjenu
			axios
				.put('rest/commentService/'+id,this.comment)
				.then(response =>
				{
					this.comment=response.data
					//potrebno je dobaviti objekat
					axios
					.get('rest/objectsService/getObject/'+objectId)
					.then(response =>
					{
						this.newObject=response.data
						this.newObject.grade=0
						for(var comment of this.comments){
							if(comment.object.id==objectId && comment.status=='Approved'&& comment.removed!=true){
								this.newObject.grade+=comment.grade
								helper++
							}
						}
						if(helper!=0)
							this.newObject.grade/=helper
						axios
						.put('rest/objectsService/'+objectId,this.newObject)
						.then(response => 
						{
							this.newObject=response.data
							this.managerr.object.grade=this.newObject.grade
							axios
							.put('rest/managerService/'+this.managerr.username,this.managerr)
							.then(response => this.managerr=response.data)
						})
					}
					)
				})
				
		},
		decline:function(id){
			for(var c of this.comments){
				if(c.status=='Processing' && c.orderId==id){
					c.status='Declined'
					this.comment=c
				}
			}
			axios
				.put('rest/commentService/'+id,this.comment)
				.then(response =>this.comment=response.data)
		}
	},
	mounted()
	{
		this.usernameParam = this.$route.params.username;
		axios
				.get('rest/commentService/')
				.then(response => 
				{
					this.comments = response.data
					
					
					axios
						.get('rest/allUsersService/findUser/' + this.usernameParam)
						.then(response => 
						{
							this.role = response.data
							if(this.role=='Manager'){
								this.manager=true
								axios
								.get('rest/managerService/getByManager/' + this.usernameParam)
								.then(response => {
									this.object=response.data
									axios
									.get('rest/managerService/getUserByUsername/' + this.usernameParam)
									.then(response => this.managerr=response.data)
								})
							}
							else
								this.manager=false
							
						})
				})
		
		
	}
});