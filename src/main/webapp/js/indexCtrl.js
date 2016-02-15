var riddle;
var answerUrl;
var level;

var getRiddle = function( url )
{
	
	$.ajax(
	{
		url: constants.baseurl + "/api/riddle/lvl/" + level,
		type: 'GET',
		accept:"application/json",
		success: function( data, textStatus, request )
		{
			riddle = data;
			$( "#title" ).text( data.title );
			$( "#content" ).html( data.question );
			answerUrl = request.getResponseHeader( 'X-answer' );
		}
	});

}

$( document ).ready(function( )
{
	level = 1;
	getRiddle( constants.baseurl + "/api/riddle/lvl/" + level );

	$( "#submit-button") .click( function()
	{
		var answer = { answer: $( "#answer" ).val() };

		$.ajax(
		{
			url: answerUrl,
			type: 'PUT',
			data: JSON.stringify( answer ),
			contentType:"application/json",
			success: function( data, textStatus, request )
			{
				getRiddle(request.getResponseHeader("X-lvl-" + level))
			},
			error: function( error )
			{
				console.log( "No!" );
			}
		});
	})	


});