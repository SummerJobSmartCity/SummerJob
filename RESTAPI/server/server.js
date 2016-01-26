var express = require('express');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var gcm = require('node-gcm');

var contador = 0;//conta o numero de usuarios do banco de dados
//conectando ao Banco de dados
mongoose.connect("mongodb://localhost/rest_test");

//Criando um servidor Express
var app = express();


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
		user.save(function(err){
			if(err){
				res.send("Deu errado!");
			}
			else{
				contador++;
				res.json({message : "" + contador + " Usuario adicionado!!!!"});
			}
		});

	});

//Pedido de Coleta
router.route('/getColeta').get(function(req,res){
	//var token = "dlOXTfhpn_o:APA91bEvQuu4KH3bWdl23fDrfBjDnwoEl8_0rL7kfBbp5iHZ_NhdgvT8Vonx8oQDQSvnAqcCbgsA7yI412dnbreNBwQ1l5W553YsM13IxengvlxPoBb07cbCM80-C2S2swOr_opGqFp7";
	var payload = {
		title : "Enviando Dados ao Servidor" ,
		message : "O RUBAO EH VIDA LOKA",
		command : "update"
	};
	var sender = new gcm.Sender("AIzaSyBOL9JDjuKHKvw6QHZ16lo5XXk9ffmfcUo"); //    AIzaSyBmfw1eEDkMQCPVwQPFC43ywxSoLDCfVDA
	var message = new gcm.Message({  //AIzaSyDW7CS4GIXuggF-zvuvya7ZXfoAk20CKGw
			contentAvailable : true
	});
	message.addData(payload);
	var user = new Users();
	//for(){    //ESSE FOR TEM QUE SER SI PARA COLETORES dzPJe6-oc-s:APA91bHXNF0f-QCjus6AXXmBobLtKBJgx6zz3Ddux2QAxMQwGThv7CMT5Mce3ZlZPQSLVn-nAh9NEDW41EgfaRXh5c2Aua1EkYZDiFgmILwt4jmEvXYB7WUVuDXQ-64teyyYgoxPolAW
	sender.send(message, "dzPJe6-oc-s:APA91bHXNF0f-QCjus6AXXmBobLtKBJgx6zz3Ddux2QAxMQwGThv7CMT5Mce3ZlZPQSLVn-nAh9NEDW41EgfaRXh5c2Aua1EkYZDiFgmILwt4jmEvXYB7WUVuDXQ-64teyyYgoxPolAW" , function(err, result) {
     if (!err) {  //dlOXTfhpn_o:APA91bEvQuu4KH3bWdl23fDrfBjDnwoEl8_0rL7kfBbp5iHZ_NhdgvT8Vonx8oQDQSvnAqcCbgsA7yI412dnbreNBwQ1l5W553YsM13IxengvlxPoBb07cbCM80-C2S2swOr_opGqFp7
       console.log("Result: " + JSON.stringify(result));
       res.send(result);
     } else {
       res.send(err);
     }
   });
// }
	});



//Update dos Coletores e Doadores
router.route('/users/:id')
//funções da rota
//função get
	.post(function(req, res) {

				// use our bear model to find the bear we want
				Users.findOne(req.params.id, function(err, user) {
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
								user.tipo = req.body.tipo;
								user.gcmToken = req.body.gcmToken;
								user.latitude = req.body.latitude;
								user.longitude = req.body.longitude;
						  // update the user info
						// save the bear
					}
						user.save(function(err) {
								if (err)
										res.send(err);
								res.json({ message: 'User updated!' });
						});
				});
	 });



//Achar o Coletor mais proximo dado um Doador
router.route('/findColetor')
//funções da rota
//função post
	.post( function(req,res){
		var doador = new Users();
		doador = req.body;
		var coletor = new Users();
		var distancia = 0;
		var distanciaMin = 0;
		Users.find( { "tipo" : "coletor" }, function(err, users) {
				if (err)
					res.send(err);
				else{
					coletor = users[0];
					distanciaMin = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(users[0].latitude), parseFloat(users[0].longitude));
					for(i = 0, len = users.length; i < len; i++){
						distancia = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(users[i].latitude), parseFloat(users[i].longitude));
						if(distancia < distanciaMin){
							distanciaMin = distancia;
						 	coletor = users[i];
						}
					}
					res.json(coletor);
				}

		});

	});



//
app.listen(app.get('port'), function(){
	console.log("rodando na porta "+ app.get('port'));
});
