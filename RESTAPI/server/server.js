var express = require('express');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var gcm = require('node-gcm');

var contador = 0;//conta o numero de usuarios do banco de dados
//conectando ao Banco de dados
mongoose.connect("mongodb://localhost/rest_test");

//Criando um servidor Express
var app = express();

function findColetor(doador,users){

	var date = new Date();
	var coletor = new Users();
	var distancia = 0;
	var distanciaMin = 0;
	var primeiro = 0;
	var len = users.length;
	var i = 0;
	while(i < len){
		if( (date.getTime() - users[i].updated.getTime()) < 120000 ){
			coletor = users[i];
			distanciaMin = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(users[i].latitude), parseFloat(users[i].longitude));
			i = len;
		}
		i++;
	}
	if(coletor !== null){
		for(i = 0, len = users.length; i < len; i++){
			if( (date.getTime() - users[i].updated.getTime()) < 300000 ){
				distancia = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(users[i].latitude), parseFloat(users[i].longitude));
				if(distancia < distanciaMin){
					distanciaMin = distancia;
				 	coletor = users[i];
				}
			}
		}
	}
  return coletor;
}

function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
  var R = 6371; // Radius of the earth in km
  var dLat = deg2rad(lat2-lat1);  // deg2rad below
  var dLon = deg2rad(lon2-lon1);
  var a =
    Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
    Math.sin(dLon/2) * Math.sin(dLon/2)
    ;
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
  var d = R * c; // Distance in km
  return d;
}

function deg2rad(deg) {
  return deg * (Math.PI/180)
}
//Criando um leitor de json
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
//Setando a porta do servidor
app.set('port',5000);
//Mensagem padrao do servidor http://localhost/
app.get('/',function(req,res){
	res.json({message: 'vidaloka'});
});

//Criando uma variavel usuario
var Users = require('./models/users');
//Criando o router (navega pelas rotas)
var router = express.Router();

//prefixo das rotas
app.use('/api', router);

//Rotas
router.route('/users')
//Cadastro
//funções da rota /users
//função get
	.get(function(req,res){
	Users.find(function(err, users){
		if(err){
			res.send(err);
		}
		else{
			var user = new Users();
			contador = 0;
			for (user in users){contador = contador+1;}
			res.json(users);
			//res.json({message: contador});
		}
	});
})
//função post
	.post( function(req,res){
		var user = new Users();
		Users.findOne({"id" : req.body.id }, function(err,doc){
			if(err){console.log(err);}
			else{
				if(doc == null){
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
					user.save(function(err){
						if(err){
							res.send("Deu errado!");
						}
						else{
							contador++;
							res.json({message : "" + contador + " Usuarios adicionado!!!!"});
						}
					});
				}
				else{
					res.json({message : "Usuario ja EXISTE!!!"});
				}
			}
		});
	});



//Pedido de Coleta
router.route('/postColeta')
	.post(function(req,res){


		var coletorEscolhido = new Users();
		var userArray;
		var coletor = new Users();
		var date = new Date();
		var i = 0;
		var len = 0;
		var doador = req.body;
		var api_key = "AIzaSyBOL9JDjuKHKvw6QHZ16lo5XXk9ffmfcUo";
		var payload = {
			comando : "update" ,
			nomedoador : doador.name,
			nomecoletor : "AINDA NAO TEM",
			latitude : doador.latitude,
			longitude : doador.longitude
		};
	//   //sender = API_KEY é unico do aplicativo
		var sender = new gcm.Sender(api_key);

		var message = new gcm.Message({  contentAvailable : true });
		message.addData(payload);

		Users.find( { "tipo" : "coletor" }, function(err, users) {
				if (err)
					res.send(err);
				else{
					userArray = users;
					for(i=0, len = users.length; i < len ; i++ ){
						coletor = users[i];
						console.log(JSON.stringify(coletor));
		// 				if( (date.getTime() - coletor.updated.getTime()) > 15000 ){  //15 seg so pra testar. Tem q ser mais!
							var token = coletor.gcmToken;
		// 	//Token eh unico de cada aparelho PARA COLETORES dzPJe6-oc-s:APA91bHXNF0f-QCjus6AXXmBobLtKBJgx6zz3Ddux2QAxMQwGThv7CMT5Mce3ZlZPQSLVn-nAh9NEDW41EgfaRXh5c2Aua1EkYZDiFgmILwt4jmEvXYB7WUVuDXQ-64teyyYgoxPolAW
							sender.send(message, token , function(err, result) {

								if (err) {
									// console.log("Result: " + JSON.stringify(err));
									// res.send(err);
						    }
								else {
									// console.log("Result: " + JSON.stringify(result));
						    	// res.send(result);
						   	}
						  });
					// 	}
					}

					setTimeout(function(){
						coletorEscolhido = findColetor(doador,userArray);

						Users.findOne({ "id" : doador.id }, function(err, user){ doador = user; });
							if(doador.tipo === ""){
								console.log("NAO ACHOU DOADOR" + JSON.stringify(doador));
							}
							else{

								if(coletorEscolhido.name === undefined){
									console.log("NAO ACHOU UM COLETOR");
									var senderDoadorNope = new gcm.Sender(api_key);
									var messageDoadorNope = new gcm.Message({  contentAvailable : true });
									var payloadDoadorNope = {
										comando : "notfound"
									};
									messageDoadorNope.addData(payloadDoadorNope);
									var tokenDoadorNope = doador.gcmToken;
									//////////////////////////
									senderDoadorNope.send(messageDoadorNope, tokenDoadorNope , function(err, result) {
										if (err) {
											console.log("Result: " + JSON.stringify(err));
											// res.send(err);
										}
										else {
											console.log("Result: " + JSON.stringify(result));
											// res.send(result);
										}
									});

								}
								else{
									console.log("ACHOU UM COLETOR");
									console.log(JSON.stringify(coletorEscolhido));
									var dist = 0;
									dist = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(coletorEscolhido.latitude), parseFloat(coletorEscolhido.longitude));
									////////PRIMEIRO O DOADOR VAI SABER QUEM EH O COLETOR

									var senderDoador = new gcm.Sender(api_key);
									var messageDoador = new gcm.Message({  contentAvailable : true });
									var jsonColetorString = JSON.stringify(coletorEscolhido);
									var payloadDoador = {
										comando : "coletorEscolhido",
										nomecoletor : coletorEscolhido.name,
										idcoletor : coletorEscolhido.id,
										iddoador : doador.id,
										distancia : dist.toString(),
										latitude : coletorEscolhido.latitude,
										longitude : coletorEscolhido.longitude,
										emailcoletor : coletorEscolhido.email
									};
									messageDoador.addData(payloadDoador);
									var tokenDoador = doador.gcmToken;
									//////////////////////////
									senderDoador.send(messageDoador, tokenDoador , function(err, result) {
										if (err) {
											console.log("Result: " + JSON.stringify(err));
											// res.send(err);
										}
										else {
											console.log("Result: " + JSON.stringify(result));
											// res.send(result);
										}
									});
									//////////////////////AGORA O COLETOR SABE O DOADOR

									var senderColetor = new gcm.Sender(api_key);
									var messageColetor = new gcm.Message({  contentAvailable : true });
									var jsonDoadorString = JSON.stringify(doador);
									var payloadColetor = {
										comando : "doadorEscolhido",
										nomedoador : doador.name ,
										distancia : dist.toString(),
										emaildoador : doador.email,
										iddoador : doador.id,
										latitude : doador.latitude,
										longitude : doador.longitude
									};
									console.log("PALHACOOOOO"+doador.id);
									console.log(doador.id);
									console.log(doador.id);
									messageColetor.addData(payloadColetor);
									var tokenColetor = coletorEscolhido.gcmToken;
									//////////////////////////
									senderColetor.send(messageColetor, tokenColetor , function(err, result) {
										if (err) {
											console.log("Result: " + JSON.stringify(err));
											// res.send(err);
										}
										else {
											console.log("Result: " + JSON.stringify(result));
											// res.send(result);
										}
									});
									//////////////////////

								}
							}
						},10000);


					}
				// res.send(JSON.stringify(doador) + "\n\n\n" + JSON.stringify(coletorEscolhido));
		});
	// console.log(JSON.stringify(req.body));
	});



//Update dos Coletores e Doadores
router.route('/users/:id')
//funções da rota
//função get
	.post(function(req, res) {

				// use our bear model to find the bear we want
				Users.findOne({"id" : req.params.id}, function(err, user) {
						if (err)
								res.send(err);
						else{
								// user.last_name = req.body.last_name;
								// user.id = req.body.id;
								// user.gender = req.body.gender;
								// user.first_name = req.body.first_name;
								// user.email = req.body.email;
								// user.link = req.body.link;
								// user.name = req.body.name;
								console.log("CACHORRO" + req.body.tipo);
								user.tipo = req.body.tipo;
								user.gcmToken = req.body.gcmToken;
								user.latitude = req.body.latitude;
								user.longitude = req.body.longitude;
								var pao = req.body.iddoador;
								if(pao == undefined){ pao = ""}
								user.iddoador = pao;

								var pao = req.body.idcoletor;
								if(pao == undefined){ pao = ""}
								user.idcoletor = pao;

								var pao = req.body.avaliarDoador;
								if(pao == undefined){ pao = "0"}
								var numD = parseFloat(user.avaliarDoador) + parseFloat(pao);
								console.log("ARROZ" + req.body.avaliarDoador);
								user.avaliarDoador = numD.toString();
								var pao = req.body.avaliarColetor;
								if(pao == undefined){ pao = "0"}
								var numC = parseFloat(user.avaliarColetor) + parseFloat(pao);
								user.avaliarColetor = numC.toString();
								user.updated = new Date();
						  // update the user info
						// save the user

					}
						user.save(function(err) {
								if (err)
										res.send(err);
								res.json({ message: 'User updated!' });
						});
				});
	 });
//////////////////////////////////////////////////
router.route('/avaliarDoador/:id')
//funções da rota
//função get
	.post(function(req, res) {

				// use our bear model to find the bear we want
				Users.findOne({"id" : req.params.id}, function(err, user) {
						if (err)
								res.send(err);
						else{
							console.log("PATOOOOOOO" + req.body.tipo);
							var pao = req.body.avaliarDoador;
							if(pao == undefined){ pao = "0"}
							var numD = parseFloat(user.avaliarDoador) + parseFloat(pao);
							console.log("ARROZ" + req.body.avaliarDoador);
							user.avaliarDoador = numD.toString();
							user.updated = new Date();
					  // update the user info
					// save the user
					}
						user.save(function(err) {
								if (err)
										res.send(err);
								res.json({ message: 'User updated!' });
						});
				});
	 });
///////////////////////////

//////////////////////////////////////////////////
router.route('/avaliarColetor/:id')
//funções da rota
//função get
	.post(function(req, res) {

				// use our bear model to find the bear we want
				Users.findOne({"id" : req.params.id}, function(err, user) {
						if (err)
								res.send(err);
						else{
								var pao = req.body.avaliarColetor;
								if(pao == undefined){ pao = "0"}
								var numC = parseFloat(user.avaliarColetor) + parseFloat(pao);
								console.log("ARROZ" + req.body.avaliarColetor);
								user.avaliarColetor = numC.toString();
								user.updated = new Date();
						  // update the user info
						// save the user
					}
						user.save(function(err) {
								if (err)
										res.send(err);
								res.json({ message: 'User updated!' });
						});
				});
	 });
///////////////////////////




//Achar o Coletor mais proximo dado um Doador
router.route('/findColetor')
//funções da rota
//função post
	.post( function(req,res){
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
  );



//
app.listen(app.get('port'), function(){
	console.log("rodando na porta "+ app.get('port'));
});
