const LoginForm = { template: '<logIn></logIn>' }
const RegistrationForm = { template: '<registrate></registrate>' }
const UserProfileView = { template: '<userProfileView></userProfileView>'}
const AdminProfileView = { template: '<adminProfileView></adminProfileView>'}
const ManagerProfileView = { template: '<managerProfileView></managerProfileView>'}
const EditProfileView = { template: '<editProfileView></editProfileView>'}
const RentACarObjectsView = { template: '<rentACarObjectsView></rentACarObjectsView>'}
const ObjectView = { template: '<objectView></objectView>'}
const UsersView = { template: '<usersView></usersView>'}
const ManagerRegistration = { template: '<registrateManager></registrateManager>'}
const CreatingObject = { template: '<creatingObject></creatingObject>'}
const CreatingVehicle = { template: '<creatingVehicle></creatingVehicle>'}
const ManagersVehicles = { template: '<managersVehicles></managersVehicles>'}
const EditVehicle = { template: '<editVehicle></editVehicle>'}
const RentalView = { template: '<rentalView></rentalView>'}
const RentACarObject = { template: '<rentACarObject></rentACarObject>'}
const ViewBasket = { template: '<viewBasket></viewBasket>'}
const VehiclesView = { template: '<vehiclesView></vehiclesView>'}
const ManagersRentals = { template: '<managersRentals></managersRentals>'}
const CommentsView = { template: '<commentsView></commentsView>'}
const Home = {template: '<homeView></homeView>'}

const router = new VueRouter({
	mode: 'hash',
	  routes: [
		{ path: '/login', name: 'home', component: LoginForm},
		{ path: '/signUpForm', name: 'signUp', component: RegistrationForm	},
		{ path: '/userProfileView/:username', name: 'profile', component: UserProfileView	},
		{ path: '/adminProfileView/:username', name: 'profileAdmin', component: AdminProfileView	},
		{ path: '/edit/:username', name: 'edit', component: EditProfileView	},
		{ path: '/', name: 'objects', component: RentACarObjectsView	},
		{ path: '/objectView/:objectId', name: 'objectView', component: ObjectView	},
		{ path: '/getAll', name: 'usersView', component: UsersView	},
		{ path: '/registrateManager/:adminUsername', name: 'registrateManagers', component: ManagerRegistration  },
		{ path: '/managerProfileView/:username', name: 'profileManager', component: ManagerProfileView	},
		{ path: '/createObject/:adminUsername', name: 'creatingObjects', component: CreatingObject  },
		{ path: '/createVehicle/:managerUsername', name: 'creatingVehicle', component: CreatingVehicle  },
		{ path: '/managersVehicles/:managerUsername', name: 'managerVehicles', component: ManagersVehicles  },
		{ path: '/editVehicle/:managerUsername/:vehicleId', name: 'editVehicle', component: EditVehicle  },
		{ path: '/rentalView/:username', name: 'rental', component: RentalView  },
		{ path: '/rentACarObject/:username', name: 'renting', component: RentACarObject  },
		{ path: '/basketView/:username/:startDateTime/:endDateTime', name: 'basket', component: ViewBasket  },
		{ path: '/vehiclesView/:rentalId', name: 'vehicles', component: VehiclesView  },
		{ path: '/managersRentals/:managerUsername', name: 'managerRentals', component: ManagersRentals  },
		{ path: '/commentView/:username', name: 'commentView', component: CommentsView  },
		{ path: '/homePage/:username', name: 'homePage', component: Home  },
	  ]
});

var app = new Vue({
	
	router,
	el: '#homePage'
});