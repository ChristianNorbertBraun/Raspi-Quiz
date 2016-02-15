var riddle;
var answerUrl;

var getRiddle = function (level){
	$.get(constants.baseurl + "/api/riddle/lvl/" + level,function(data)
	{
		riddle = data;
		$("#title").text("Riddle on Level " + data.level);
		$("#content").append(data.question);
		answerUrl = data.getResponseHeader('X-answer');
		console.log(answerUrl);
	}); 
}

$( document ).ready(function( )
{
	getRiddle(1);

	$("#submit-button").click(function(){
		var attributes = $("#answer").val();

		$.ajax({
			url: constants.baseurl + "/riddle/" + riddle.id + "/lvl/" + riddle.level,
			type: 'PUT',
			data: attributes,
			contentType:"application/json",
			success: function(result){
				console.log("Nice One");
			}
		})
	})	


});