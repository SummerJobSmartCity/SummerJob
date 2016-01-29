var mongoose = require('mongoose');

var UserSchema = mongoose.Schema({

	last_name : String,
	id : String,
	gender : String,
	first_name : String,
	email : String,
	link : String,
	name : String,
	tipo: String,
	gcmToken : String,
	api_key : String,
	latitude : String,
	longitude : String,
	avaliarDoador : String,
	avaliarColetor : String,
	updated: {type: Date, default: Date.now}

});

module.exports = mongoose.model('Users', UserSchema);
