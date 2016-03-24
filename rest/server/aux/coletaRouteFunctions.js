var User = require('../models/users');

var postColeta = function(req, res) {

	console.log("FAZENDO PEDIDO DE COLETA");
	var coletorEscolhido = new User();
	var userArray;
	var coletor = new User();
	var date = new Date();
	var i = 0;
	var len = 0;
	var doador = req.body;
	var api_key = "AIzaSyBOL9JDjuKHKvw6QHZ16lo5XXk9ffmfcUo";
	var payload = {
		comando: "update",
		nomedoador: doador.name,
		nomecoletor: "AINDA NAO TEM",
		latitude: doador.latitude,
		longitude: doador.longitude
	};
	//   //sender = API_KEY é unico do aplicativo
	var sender = new gcm.Sender(api_key);

	var message = new gcm.Message({
		contentAvailable: true
	});
	message.addData(payload);

	User.find({
		"tipo": "coletor"
	}, function(err, users) {
		if (err)
			res.send(err);
		else {
			userArray = users;
			for (i = 0, len = users.length; i < len; i++) {
				coletor = users[i];
				// console.log(JSON.stringify(coletor));
				// 				if( (date.getTime() - coletor.updated.getTime()) > 15000 ){  //15 seg so pra testar. Tem q ser mais!
				var token = coletor.gcmToken;
				// 	//Token eh unico de cada aparelho PARA COLETORES dzPJe6-oc-s:APA91bHXNF0f-QCjus6AXXmBobLtKBJgx6zz3Ddux2QAxMQwGThv7CMT5Mce3ZlZPQSLVn-nAh9NEDW41EgfaRXh5c2Aua1EkYZDiFgmILwt4jmEvXYB7WUVuDXQ-64teyyYgoxPolAW
				sender.send(message, token, function(err, result) {

					if (err) {
						// console.log("Result: " + JSON.stringify(err));
						// res.send(err);
					} else {
						// console.log("Result: " + JSON.stringify(result));
						// res.send(result);
					}
				});
				// 	}
			}

			setTimeout(function() {
				coletorEscolhido = findColetor(doador, userArray);

				User.findOne({
					"id": doador.id
				}, function(err, user) {
					doador = user;
				});
				if (doador.tipo === "") {
					console.log("NAO ACHOU DOADOR" + JSON.stringify(doador));
				} else {

					if (coletorEscolhido.name === undefined) {
						console.log("NAO ACHOU UM COLETOR");
						// res.send({comando : "notfound"});
						var senderDoadorNope = new gcm.Sender(api_key);
						var messageDoadorNope = new gcm.Message({
							contentAvailable: true
						});
						var payloadDoadorNope = {
							comando: "notfound"
						};
						messageDoadorNope.addData(payloadDoadorNope);
						var tokenDoadorNope = doador.gcmToken;
						//////////////////////////
						senderDoadorNope.send(messageDoadorNope, tokenDoadorNope, function(err, result) {
							if (err) {
								console.log("Result: " + JSON.stringify(err));
								// res.send(err);
							} else {
								console.log("Result: " + JSON.stringify(result));
								// res.send(result);
							}
						});

					} else {
						console.log("ACHOU UM COLETOR");
						// coletorEscolhido.estado = "ocupado";

						User.findOne({
							"id": coletorEscolhido.id
						}, function(err, user) {
							if (err) {}
							// res.send(err);
							else {
								user.estado = "ocupado";
								console.log(user.estado);
								user.updated = new Date();
								// update the user info
								// save the user
							}
							console.log(user.estado);
							user.save(function(err) {
								if (err) {
									res.send(err);
								} else {
									console.log("SETANDO ESTADO");
									res.json({
										message: 'User updated!'
									});
								}
							});
							console.log(user.estado);
							coletorEscolhido = user;
						});
						console.log("BATATA " + coletorEscolhido.estado);



						console.log("COLETOR FOI ESCOLHIDO AGORA" + coletorEscolhido.name);

						var dist = 0;
						dist = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(coletorEscolhido.latitude), parseFloat(coletorEscolhido.longitude));
						////////PRIMEIRO O DOADOR VAI SABER QUEM EH O COLETOR

						var senderDoador = new gcm.Sender(api_key);
						var messageDoador = new gcm.Message({
							contentAvailable: true
						});
						var jsonColetorString = JSON.stringify(coletorEscolhido);
						var payloadDoador = {
							comando: "coletorEscolhido",
							nomecoletor: coletorEscolhido.name,
							idcoletor: coletorEscolhido.id,
							iddoador: doador.id,
							distancia: dist.toString(),
							latitude: coletorEscolhido.latitude,
							longitude: coletorEscolhido.longitude,
							emailcoletor: coletorEscolhido.email
						};
						messageDoador.addData(payloadDoador);
						var tokenDoador = doador.gcmToken;
						//////////////////////////
						senderDoador.send(messageDoador, tokenDoador, function(err, result) {
							if (err) {
								console.log("Result: " + JSON.stringify(err));
								// res.send(err);
							} else {
								console.log("Result: " + JSON.stringify(result));
								// res.send(result);
							}
						});
						//////////////////////AGORA O COLETOR SABE O DOADOR

						var senderColetor = new gcm.Sender(api_key);
						var messageColetor = new gcm.Message({
							contentAvailable: true
						});
						var jsonDoadorString = JSON.stringify(doador);
						var payloadColetor = {
							comando: "doadorEscolhido",
							nomedoador: doador.name,
							distancia: dist.toString(),
							emaildoador: doador.email,
							iddoador: doador.id,
							latitude: doador.latitude,
							longitude: doador.longitude
						};

						messageColetor.addData(payloadColetor);
						var tokenColetor = coletorEscolhido.gcmToken;
						//////////////////////////
						senderColetor.send(messageColetor, tokenColetor, function(err, result) {
							if (err) {
								console.log("Result: " + JSON.stringify(err));
								// res.send(err);
							} else {
								console.log("Result: " + JSON.stringify(result));
								// res.send(result);
							}
						});
						//////////////////////

					}
				}

			}, 10000);


		}
		// res.send(JSON.stringify(doador) + "\n\n\n" + JSON.stringify(coletorEscolhido));
	});
	// console.log(JSON.stringify(req.body));
}

exports.postColeta = postColeta;