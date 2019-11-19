# Cinema Stream Microservices
[![Build Status](https://travis-ci.org/bmstu-rsoi/lab3-front-end-Osipo.svg?branch=master)](https://travis-ci.org/bmstu-rsoi/lab3-front-end-Osipo) 



Simple application with architecture of the microservices.
<br/>
Services:
<p>ApiGatewayApp - GATEWAY (aggregator)</p>
<p>DiscoveryService - for registration of services.</p>
<p>CinemaService.</p>
<p>GenreService.</p>
<p>FilmSerivce.</p>
<p>SeanceService.</p>
<br/>

Allow users:
    <p>Select films, genres, cinemas, seances</p>
    <p>Add new genre</p>
    <p>Get cinemas with their  current seances</p>
    <p>Update cinema with its seances</p>
    <p>Delete genres </p>

Example of API:<br/> 
    
    GET: /v1/api/genres?page=X&size=Y 
    
    If page was not specified -> page = 1.
    If size was not specified -> size = 1.
    Page and size are both optional.
    
    PATCH: /v1/api/cinemas/{c_id}
    
    Update cinema with id.
    Pass CreateCinema object, which contains
    all fields (except id) of the cinema with c_id
    and aslo CreateSeance[] array for addition
    of new seances to the cinema.
    
    GET: /v1/api/cinemas/{c_id}/seances
    Get all seances of the cinema with c_id.   

<b>Author: Osipov Oleg IU711M</b>