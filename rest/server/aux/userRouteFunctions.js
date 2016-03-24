
var User = require('../models/users');

var getAll = function(req, res) {
		User.find(function(err, users) {
			if (err) {
				res.send(err);
			} else {
				var user = new User();
				contador = 0;
				for (user in users) {
					contador = contador + 1;
				}
				res.json(users);
			}
		});
	}
var postUser = function(req, res) {
		var user = new User();
		User.findOne({
			"id": req.body.id
		}, function(err, doc) {
			if (err) {
				console.log(err);
			} else {
				if (doc == null) {
					user.last_name = req.body.last_name;
					user.id = req.body.id;
					user.gender = req.body.gender;
					user.first_name = req.body.first_name;
					user.email = req.body.email;
					user.link = req.body.link;
					user.name = req.body.name;
					user.tipo = req.body.tipo;
					user.gcmToken = req.body.gcmToken;
					user.latitude = req.body.latitude;
					user.longitude = req.body.longitude;
					user.avaliarDoador = "0";
					user.avaliarColetor = "0";
					user.iddoador = "";
					user.idcoletor = "";
					user.estado = "livre";
					user.save(function(err) {
						if (err) {
							console.log("deu errado");
							res.send("Deu errado!");
						} else {
							contador++;
							console.log("Adicionando" + user.name);
							res.json({
								message: "" + contador + " Usuarios adicionado!!!!"
							});
						}
					});
				} else {
					console.log("Adicionando " + req.body.name);
					res.json({
						message: "Usuario ja EXISTE!!!"
					});
				}
			}
		});
	}


var getUserId = function(req, res) {

		// use our bear model to find the bear we want
		Users.findOne({
			"id": req.params.id
		}, function(err, user) {
			if (err)
				res.send(err);
			else {
				// user.last_name = req.body.last_name;
				// user.id = req.body.id;
				// user.gender = req.body.gender;
				// user.first_name = req.body.first_name;
				// user.email = req.body.email;
				// user.link = req.body.link;
				// user.name = req.body.name;
				var pao = req.body.tipo;
				if (pao == undefined) {
					pao = "";
				}
				user.tipo = pao;
				user.gcmToken = req.body.gcmToken;
				user.latitude = req.body.latitude;
				user.longitude = req.body.longitude;
				var pao = req.body.iddoador;
				if (pao == undefined) {
					pao = "";
				}
				user.iddoador = pao;

				var pao = req.body.idcoletor;
				if (pao == undefined) {
					pao = "";
				}
				user.idcoletor = pao;

				var pao = req.body.avaliarDoador;
				if (pao == undefined) {
					pao = "0";
				}
				var numD = parseFloat(user.avaliarDoador) + parseFloat(pao);

				user.avaliarDoador = numD.toString();
				var pao = req.body.avaliarColetor;
				if (pao == undefined) {
					pao = "0";
				}
				var numC = parseFloat(user.avaliarColetor) + parseFloat(pao);
				user.avaliarColetor = numC.toString();
				user.updated = new Date();

				// var pao = req.body.estado;
				// if(pao == undefined){ pao = "livre";}
				// user.estado = pao;
				// update the user info
				// save the user

			}
			user.save(function(err) {
				if (err)
					res.send(err);
				res.json({
					message: 'User updated!'
				});
			});
		});
	}
var avaliarDoadorPost = function(req, res) {

		// use our bear model to find the bear we want
		Users.findOne({
			"id": req.params.id
		}, function(err, user) {
			if (err)
				res.send(err);
			else {
				var pao = req.body.avaliarDoador;
				if (pao == undefined) {
					pao = "0"
				}
				var numD = parseFloat(user.avaliarDoador) + parseFloat(pao);
				console.log("AVALIAÇAO DO DOADOR");
				user.avaliarDoador = numD.toString();
				user.updated = new Date();
				// update the user info
				// save the user
			}
			user.save(function(err) {
				if (err)
					res.send(err);
				// res.json({ message: 'User updated!' });
			});
		});

		Users.findOne({
			"id": req.body.id
		}, function(err, user) {
			if (err)
				res.send(err);
			else {
				console.log(JSON.stringify(user));
				user.estado = "livre";
				console.log("ATUALIZANDO o estado do coletor");
				user.updated = new Date();
				// update the user info
				// save the user
			}
			user.save(function(err) {
				if (err)
					res.send(err);
				res.json({
					message: 'User updated!'
				});
			});
		});
	}

var avaliarColetorPost = function(req, res) {
		Users.findOne({
			"id": req.params.id
		}, function(err, user) {
			if (err)
				res.send(err);
			else {
				var pao = req.body.avaliarColetor;
				if (pao == undefined) {
					pao = "0"
				}
				var numC = parseFloat(user.avaliarColetor) + parseFloat(pao);
				console.log("AVALIAÇAO DO COLETOR");
				user.avaliarColetor = numC.toString();
				user.updated = new Date();
				// update the user info
				// save the user
			}
			user.save(function(err) {
				if (err)
					res.send(err);
				res.json({
					message: 'User updated!'
				});
			});
		});
	}
var findColetorPost = function(req,res){
		var doador = new Users();
		doador = req.body;
		doador.updated = new Date();
		Users.find( { "tipo" : "coletor" }, function(err, users) {
				if (err)
					res.send(err);
				else{
					// coletor = users[0];
					// distanciaMin = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(users[0].latitude), parseFloat(users[0].longitude));
					// for(i = 0, len = users.length; i < len; i++){
					// 	distancia = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(users[i].latitude), parseFloat(users[i].longitude));
					// 	if(distancia < distanciaMin){
					// 		distanciaMin = distancia;
					// 	 	coletor = users[i];
					// 	}
					// }
					// res.json(coletor);
          res.json(findColetor(doador,users));
				}
		});
	}

exports.getAll = getAll;
exports.postUser = postUser;
exports.getUserId = getUserId;
exports.avaliarDoadorPost = avaliarDoadorPost;
exports.avaliarColetorPost = avaliarColetorPost;
exports.findColetorPost = findColetorPost;