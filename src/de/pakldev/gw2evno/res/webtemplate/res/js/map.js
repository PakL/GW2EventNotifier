var map;

function unproject(coord) {
	return map.unproject(coord, map.getMaxZoom());
}
function m2inch(num) {
	return (num*39.3700787);
}
function inch2m(num) {
	return (num/39.3700787);
}

$(function () {
	var southWest, northEast;

	$.getJSON("https://api.guildwars2.com/v1/event_details.json?event_id="+eventId, function(events) {

		var e = events.events[eventId];

		var mapId = e.map_id;

		$.getJSON("https://api.guildwars2.com/v1/maps.json?map_id="+mapId, function (data) {
			var m = data.maps[mapId];

			map = L.map("map", {
				zoom: 0,
				minZoom: 0,
				maxZoom: 7,
				crs: L.CRS.Simple
			});

			southWest = unproject([0, 32768]);
			northEast = unproject([32768, 0]);

			map.setMaxBounds(new L.LatLngBounds(southWest, northEast));

			L.tileLayer("https://tiles.guildwars2.com/1/1/{z}/{x}/{y}.jpg", {
				minZoom: 0,
				maxZoom: 7,
				continuousWorld: true,
				reuseTiles: true,
				attribution: 'Images by ArenaNet'
			}).addTo(map);

			var eX = e.location.center[0];
			var eY = e.location.center[1];

			var pX = (eX - m.map_rect[0][0]) / (m.map_rect[1][0] - m.map_rect[0][0]);
			var pY = (eY - m.map_rect[0][1]) / (m.map_rect[1][1] - m.map_rect[0][1]);

			var cX = m.continent_rect[0][0] + (m.continent_rect[1][0] - m.continent_rect[0][0]) * pX;
			var cY = m.continent_rect[0][1] + (m.continent_rect[1][1] - m.continent_rect[0][1]) * pY;

			var p = unproject([cX,cY]);
			L.marker( p, { title: e.name } ).addTo(map);
			map.setView(p, 5)
		});

	});
});