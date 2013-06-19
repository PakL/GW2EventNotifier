$(document).ready(function(){

	var eventNames = {};

	var updateStates = function() {
		$.get('/data/mapevents/', function(resp){
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
			setTimeout(updateStates, 2000);
		}, 'json');
	};
	
	$.get('/data/maps', function(resp){
		$('#mapselect').html('');
		_.each(resp, function(name, id){
			$('#mapselect').append($('<option />').val(id).text(name));
		});

		$.get('/data/events', function(resp){
			eventNames = resp;
			updateStates();
		}, 'json');
	},'json');

	$('header > .settings').css("cursor","pointer").on('click',function(){
		if( $('#settings').is(":hidden") ) {
			$('#settings').show().animate({'left': 0});
		} else {
			$('#settings').animate({'left': '-100%'}, function(){$(this).hide()});
		}
	});

});