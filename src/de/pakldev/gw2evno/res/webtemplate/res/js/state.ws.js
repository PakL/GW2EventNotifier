var stateSort = function(item){
	if( item.state == 3 ) return -4;
	if( item.state == 5 ) return -3;
	if( item.state == 4 ) return -2;
	if( item.state == 6 ) return -1;

	if( item.state == 2) return 1;

	return 0;
};
var RstateSort = function(item){
	if( item.state == 3 ) return 4;
	if( item.state == 5 ) return 3;
	if( item.state == 4 ) return 2;
	if( item.state == 6 ) return 1;

	if( item.state == 2) return 1;

	return 0;
};
$(function(){
	var mapopen = false;
	var eventNames = {};
	var selectedWorld = -1;
	var selectedMap = -1;

	var ws = new WebSocket("ws://{host}:{wsport}/");
	ws.sendJSON = function(json) {
		this.send(JSON.stringify(json));
	};

	ws.onopen = function(){
		ws.sendJSON({"request":"worlds"});
		ws.sendJSON({"request":"maps"});
		ws.sendJSON({"request":"settings"});
		ws.sendJSON({"request":"eventnames"});
	};
	ws.onmessage = function(msg){
		var data = $.parseJSON(msg.data);

		if( data.type == "worlds" ) {
			$('#worldselect').empty();
			var us = $('<optgroup />').prop("label", "US server");
			var eu = $('<optgroup />').prop("label", "EU server")
			_.each(data.worlds, function(name, id){
				if( id.substr(0,1) == "1" ) {
					us.append($('<option />').val(id).text(name));
				} else {
					eu.append($('<option />').val(id).text(name));
				}
			});
			$('#worldselect').append(us).append(eu);
		} else if( data.type == "maps" ) {
			$('#mapselect').empty();
			_.each(data.maps, function(name, id){
				$('#mapselect').append($('<option />').val(id).text(name));
			});
		} else if( data.type == "settings" ) {
			if( selectedWorld != data.settings.world ) {
				$('#worldselect option[value="'+data.settings.world+'"]').prop("selected", true);
				selectedWorld = data.settings.world;
			}

			if( selectedMap != data.settings.map ) {
				$('#mapselect option[value='+data.settings.map+']').prop("selected", true);
				selectedMap = data.settings.map;
			}

			if( $('#interestingonly').is(":checked") != data.settings.interestingonly ) {
				if( data.settings.interestingonly ) {
					$('#interestingonly').prop("checked", true);
				} else {
					$('#interestingonly').prop("checked", false);
				}
			}
		} else if( data.type == "eventnames" ) {
			eventNames = data.events;
			ws.sendJSON({"request":"interestingevents"});
			ws.sendJSON({"request":"events"});
		} else if( data.type == "interestingevents" ) {
			data = _.sortBy(data.events, RstateSort);
			var list = $("#content").children("ul");
			if( typeof(list.html()) == "undefined" ) { list = $('<ul />');$("#content").html(list) }

			list.find("li.interesting").remove();
			list.prepend($('<li />').addClass("interesting sparator"));
			_.each(data, function(item){
				var name = eventNames[item.id];
				if( typeof(name) == "undefined" ) name = {"name": item.id, "icon": "star"};
				var item = $('<li />').addClass(name.icon+' state'+item.state+" interesting").append('<div><a class="location"><input type="hidden" value="'+item.id+'" /></a>' + name.name + '<br /><small>'+item.langstate+'</small></div>');
				list.prepend(item);
			});
		} else if( data.type == "events" ) {
			data = _.sortBy(data.events, stateSort);
			var list = $("#content").children("ul");
			if( typeof(list.html()) == "undefined" ) { list = $('<ul />');$("#content").html(list) }

			list.find("li").not(".interesting").remove();
			_.each(data, function(item){
				var name = eventNames[item.id];
				if( typeof(name) == "undefined" ) name = {"name": item.id, "icon": "star"};
				var item = $('<li />').addClass(name.icon+' state'+item.state).append('<div><a class="location"><input type="hidden" value="'+item.id+'" /></a>' + name.name + '<br /><small>'+item.langstate+'</small></div>');
				list.append(item);
			});
		}
	};

	$('#worldselect').change(function(){
		$.get('/data/setworld',{"world":$('#worldselect option:selected').prop("value")}, function(){});
	});
	$('#mapselect').change(function(){
		$.get('/data/setmap',{"map":$('#mapselect option:selected').prop("value")}, function(){});
	});
	$('#interestingonly').change(function(){
		if( $('#interestingonly').is(":checked") ) {
			$.get('/data/setinterestingonly',{"interestingonly":"true"}, function(){});
		} else {
			$.get('/data/setinterestingonly',{"interestingonly":"false"}, function(){});
		}
	});

	$('header > .settings').on('click',function(){
		if( $('#settings').is(":hidden") ) {
			$('#settings').show().animate({'left': 0});
		} else {
			$('#settings').animate({'left': '-100%'}, function(){$(this).hide()});
		}
	});
	$(document).on('click','.location', function() {
		var id = $(this).children("input").val();

		var map = $('<div />').addClass("map");
		map.append('<div class="closemap"></div>').append('<iframe src="map.html?eventid='+id+'"></iframe>');
		$("body").append(map);
		mapopen = true;
		map.animate({ left: 0 });
		map.children(".closemap").animate({ left: 0 });
	});

	$(document).on('click','.closemap', function() {
		var map = $(this).parent();
		mapopen = false;
		map.animate({left: "100%"}, function(){ map.remove(); });
	});

});