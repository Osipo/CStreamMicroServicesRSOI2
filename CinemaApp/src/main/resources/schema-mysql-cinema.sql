CREATE TABLE IF NOT EXISTS cinema(cid BIGINT NOT NULL auto_increment,
	cname VARCHAR(127) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    region VARCHAR(50) NULL,
    country VARCHAR(100) NOT NULL,
    constraint cinema_PK PRIMARY KEY(cid)
);
DELIMITER //
CREATE FUNCTION ch1(r int) RETURNS BIT BEGIN
    DECLARE S AS BIT;
    IF r = 0 THEN SET
        S = 1;
        INSERT INTO cinema VALUES(1,'CMax','Kazan','Korolenko','Moscovskyi','The Russian Federation'),
        (2,'Karo','Moscow','Venevskaya','South_Butovo','The Russian Federation');
        INSERT INTO cinema VALUES(3,'Eruption','San Jones','Victory',null,'USA');
    END IF;
    RETURN S;
END //

DELIMITER ;