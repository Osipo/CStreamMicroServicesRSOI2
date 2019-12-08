CREATE TABLE IF NOT EXISTS film(fid BIGINT NOT NULL auto_increment,
    fname VARCHAR(100) NOT NULL,
    rating SMALLINT(6) not null,
    gid BIGINT NOT NULL DEFAULT -1,
    CONSTRAINT film_PK PRIMARY KEY(fid),
    CONSTRAINT film_rating_CH CHECK( (rating >= 0) AND (rating <= 100))
);
DELIMITER //
CREATE FUNCTION ch1(r int) RETURNS BIT
BEGIN

	DECLARE s BIT;
    SET S = 0;
    IF r = 0 THEN SET
		S = 1;
        INSERT INTO film VALUES (10,'IT',18,21),(11,'Hellboy',16,23),(12,'Madagaskar',6,24);
    END IF;
    RETURN S;
END //

DELIMITER ;