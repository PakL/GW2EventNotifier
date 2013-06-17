$(document).ready(function(){

	var eventNames = {};

	var updateStates = function() {
		$.get('/data/mapevents/', function(resp){
			resp = _.sortBy(resp, function(item){
				if( item.state == 3 ) return -4;
				if( item.state == 5 ) return -3;
				if( item.state == 4 ) return -2;
				if( item.state == 6 ) return -1;

				return 1;
			});
			var list = $('<ul />');
			_.each(resp, function(item){
				var name = eventNames[item.id];
				var item = $('<li />').addClass(name.icon).append('<div class="state'+item.state+'">' + name.name + '<br /><small>'+item.langstate+'</small></div>');
				list.append(item);
			});

			$('#content').html(list);
			setTimeout(updateStates, 2000);
		}, 'json');
	};

	$.get('/data/events/', function(resp){
		eventNames = resp;
		updateStates();
	}, 'json');

});