$(document).ready(function(){

	var eventNames = {};
	var selectedWorld = -1;
	var selectedMap = -1;

	var update = function() {
		$.get('/data/settings', function(resp){
			if( selectedWorld != resp.world ) {
				/*$('#worldselect option:selected').removeProp("selected");*/
				$('#worldselect option[value="'+resp.world+'"]').prop("selected", true);
				selectedWorld = resp.world;
			}

			if( selectedMap != resp.map ) {
				/*$('#mapselect option:selected').removeProp("selected");*/
				$('#mapselect option[value='+resp.map+']').prop("selected", true);
				selectedMap = resp.map;
			}

			$.get('/data/mapevents', function(resp){
				resp = _.sortBy(resp, function(item){
					if( item.state == 3 ) return -4;
					if( item.state == 5 ) return -3;
					if( item.state == 4 ) return -2;
					if( item.state == 6 ) return -1;

					if( item.state == 2) return 1;

					return 0;
				});
				var list = $('<ul />');
				_.each(resp, function(item){
					var name = eventNames[item.id];
					var item = $('<li />').addClass(name.icon+' state'+item.state).append('<div>' + name.name + '<br /><small>'+item.langstate+'</small></div>');
					list.append(item);
				});

				$('#content').html(list);
				setTimeout(update, 2000);
			}, 'json');
		},'json');
	};
	
	$.get('/data/worlds', function(resp){
		$('#worldselect').empty();
		var us = $('<optgroup />').prop("label", "US server");
		var eu = $('<optgroup />').prop("label", "EU server")
		_.each(resp, function(name, id){
			if( id.substr(0,1) == "1" ) {
				us.append($('<option />').val(id).text(name));
			} else {
				eu.append($('<option />').val(id).text(name));
			}
		});
		$('#worldselect').append(us).append(eu);

		$.get('/data/maps', function(resp){
			$('#mapselect').empty();
			_.each(resp, function(name, id){
				$('#mapselect').append($('<option />').val(id).text(name));
			});

			$.get('/data/events', function(resp){
				eventNames = resp;
				update();
			}, 'json');
		},'json');
	},'json');

	$('header > .settings').css("cursor","pointer").on('click',function(){
		if( $('#settings').is(":hidden") ) {
			$('#settings').show().animate({'left': 0});
		} else {
			$('#settings').animate({'left': '-100%'}, function(){$(this).hide()});
		}
	});

	$('#worldselect').change(function(){
		$.get('/data/setworld',{"world":$('#worldselect option:selected').prop("value")}, function(){});
	});
	$('#mapselect').change(function(){
		$.get('/data/setmap',{"map":$('#mapselect option:selected').prop("value")}, function(){});
	});

});