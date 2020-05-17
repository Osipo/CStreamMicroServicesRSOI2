CREATE TABLE IF NOT EXISTS cinema(cid BIGINT NOT NULL auto_increment,
	cname VARCHAR(127) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    region VARCHAR(50) NULL,
    country VARCHAR(100) NOT NULL,
    constraint cinema_PK PRIMARY KEY(cid)
);
CREATE TABLE IF NOT EXISTS room(rid BIGINT NOT NULL auto_increment,
    cid BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL DEFAULT 'Standard',
    seats INT NOT NULL DEFAULT 0,
    CONSTRAINT room_PK PRIMARY KEY(rid),
    CONSTRAINT room_FK FOREIGN KEY(cid) REFERENCES cinema(cid) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT room_seats_ch CHECK(seats >= 0)
);