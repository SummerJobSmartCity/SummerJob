var express = require('express');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var gcm = require('node-gcm');
var aux = require('./aux/aux');
var urf = require('./aux/userRouteFunctions');
var crf = require('./aux/coletaRouteFunctions')
var contador = 0; //conta o numero de usuarios do banco de dados
var User = require('./models/users');
//conectando ao Banco de dados
mongoose.connect("mongodb://localhost/rest_test");

//Criando um servidor Express
var app = express();

// //Criando um leitor de json
app.use(bodyParser.urlencoded({
	extended: true
}));
app.use(bodyParser.json());
//Setando a porta do servidor
app.set('port', 5000);
//Mensagem padrao do servidor http://localhost/
app.get('/', function(req, res) {
	// res.json({message: 'vidaloka'});
	res.send("it works!");
});


//Criando uma variavel usuario
//Criando o router (navega pelas rotas)
var router = express.Router();

//prefixo das rotas
app.use('/api', router);

//Rotas
router.route('/users')
	.get(urf.getAll)
	.post(urf.postUser);

// Pedido de Coleta
router.route('/postColeta')
	.post(crf.postColeta);

// Update dos Coletores e Doadores
router.route('/users/:id')
	.post(urf.getUserId);

router.route('/avaliarDoador/:id')
	.post(urf.avaliarDoadorPost);

router.route('/avaliarColetor/:id')
	.post(urf.avaliarColetorPost);

//Achar o Coletor mais proximo dado um Doador
router.route('/findColetor')
	.post(urf.findColetorPost);

app.listen(app.get('port'), function() {
	console.log("rodando na porta " + app.get('port'));
});