
**1. Create hotel.**

	route: 	      /hotels

	json(POST):   {"name": "test", "city": "test"}

**2. Create user (customer).**

	route: 	      /users

	json(POST):   {"email": "test"}

**3. Hotel registers new room.**

	route: 	      /hotels/:id/register

	json(POST):   {"price": 90}

**4. Hotel removes a room.**

	route(DELETE): /hotels/:hotelId/room/:roomId

**5. User searches for available hotel rooms**

	route(GET):   /hotels/find/:from/:to/:city/:price (date format: YYYmmDD)

**6. User asks for room reservation for specified period.**

	route(POST):  /book

	jsonFormat:  {"roomId":2,"userId":1,"period":{"from":"2016-01-01","to":"2016-02-01"}}

**7. Hotel confirms a reservation.**

	response from point 6

**8. Hotel rejects a reservation.**

	response from point 6

**9. User can check their reservations.**

	route(GET):   /users/:id/reservations


**RUN:  sbt run**

**TEST: sbt test**