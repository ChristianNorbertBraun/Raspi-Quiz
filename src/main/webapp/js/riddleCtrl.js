var riddle;
var answerUrl;
var nextRiddleUrl;


var init = function(url)
{
	$.ajax(
	{
		url: url,
		type: 'GET',
		accept:"application/json",
		success: function( data, textStatus, request )
		{
			getRiddle( request.getResponseHeader( 'X-riddle' ) );
		}
	});
}

var getRiddle = function( url )
{
	
	$.ajax(
	{
		url: url,
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

var sendAnswer = function( url, data )
{

	$.ajax(
	{
		url: url,
		type: 'PUT',
		data: JSON.stringify( data ),
		contentType:"application/json",
		success: function( data, textStatus, request )
		{
			if( request.status == 204 )
				alert( "Du hast gewonnen" );
			else
				getRiddle( request.getResponseHeader( 'X-riddle' ) );

			$( "#answer" ).val("");
		},
		error: function( error )
		{
			alert("Falsche Antwort!");
		}
	});
}

$( document ).ready(function( )
{
	
	init( window.location.protocol + "/quiz/api/init");

	$( "#submit-button") .click( function()
	{
		var answer = { answer: $( "#answer" ).val() };
	});

	$( "#answer" ).keyup(function ( e ) 
	{
    	if ( e.keyCode == 13 ) {
        	var answer = { answer: $( "#answer" ).val() };
			sendAnswer( answerUrl, answer );
		
    	}
	});
});