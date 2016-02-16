Raspberry Pi Quiz
========================

A little game for a raspberry pi. You have 6 riddles, if you solve one it will shoot out one led.
The riddles are getting harder as far as you go.


System Requirements
-------------------

The project is an maven project developed for a tomcat server running on a raspberry pie.

The riddles must be placed in the resources folder as JSON-file.


API Documentation
-------

1. Method: GET
   URL:     ../quiz/init
   
   Answer:
        Status-code: 200
        Header:
            X-riddle: URL for first riddle

2. Method: GET
   URL: ../quiz/riddle/{riddleId}
   
   Answer Success:
        Status-code: 200
        Header:
            X-answer: URL for answer of the riddle
        Data:
            Riddle-Object:
            {"title":"TestTitle", "id":"0","question":"2+2"}
   Answer Failure:
        Status-code: 404
        
3. Method: PUT
   URL: ../quiz/riddle/{riddleId}
   
   Answer Success:
           riddleId < 5
                Status-code: 200
                Header:
                    X-riddle: URL of next riddle
           riddleId = 5
                 Status-code: 204
                         
                 Payload:
                    {"answer":"myAnswer"}
   Answer Failure:                 
           Status-code: 400