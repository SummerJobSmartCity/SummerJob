	var findColetor = function(doador, coletores) {

		var data = new Date();
		var coletor = new User();
		var distancia = 0;
		var distanciaMin = 0;
		for (var i = 0; i < coletores.length; i++) {
			if (((date.getTime() - coletores[i].updated.getTime()) < 120000) && (coletores[i].estado !== "ocupado")) {
				coletor = coletores[i];
				distanciaMin = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(coletor.latitude), parseFloat(coletor.latitude), parseFloat(coletor.longitude));
			}
		}
		if (coletor !== null) {
			for (var i = 0; i < coletores.length; i++) {
				if ((date.getTime() - coletores.updated.getTime() < 120000) && (coletores[i].estado !== "ocupado")) {
					distancia = getDistanceFromLatLonInKm(parseFloat(doador.latitude), parseFloat(doador.longitude), parseFloat(coletores[i].latitude), parseFloat(coletores[i].longitude));
					if (distancia < distanciaMin) {
						distanciaMin = distancia;
						coletor = coletores[i];
					}
				}
			}
		}

		return coletor;
	}


	var getDistanceFromLatLonInKm = function(lat1, lon1, lat2, lon2) {
		var R = 6371; // Radius of the earth in km
		var dLat = deg2rad(lat2 - lat1); // deg2rad below
		var dLon = deg2rad(lon2 - lon1);
		var a =
			Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
			Math.sin(dLon / 2) * Math.sin(dLon / 2);
		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		var d = R * c; // Distance in km
		return d;
	}

	var deg2rad = function(deg) {
		return deg * (Math.PI / 180)
	}

exports.findColetor = findColetor;
exports.getDistanceFromLatLonInKm = getDistanceFromLatLonInKm;
exports.deg2rad = deg2rad;
