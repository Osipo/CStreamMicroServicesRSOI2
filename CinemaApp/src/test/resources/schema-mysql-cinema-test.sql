CREATE TABLE IF NOT EXISTS cinema(cid BIGINT NOT NULL auto_increment,
	cname VARCHAR(127) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    region VARCHAR(50) NULL,
    country VARCHAR(100) NOT NULL,
    constraint cinema_PK PRIMARY KEY(cid)
);