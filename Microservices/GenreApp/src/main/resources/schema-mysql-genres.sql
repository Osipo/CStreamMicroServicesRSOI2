CREATE TABLE IF NOT EXISTS genre(gid BIGINT NOT NULL auto_increment,
    gname VARCHAR(100) NOT NULL,
    remarks TEXT null,
    CONSTRAINT genre_PK PRIMARY KEY(gid)
);
--ALTER TABLE genre
--ADD CONSTRAINT genre_gcid_FK FOREIGN KEY(gcid) REFERENCES gcategories.genre_category(gcid) ON UPDATE CASCADE ON DELETE CASCADE;

DELIMITER //
CREATE FUNCTION ch1(r int) RETURNS BIT
BEGIN

	DECLARE s BIT;
    SET S = 0;
    IF r = 0 THEN SET
		S = 1;
        INSERT INTO genre(gid,gname,remarks) VALUES
            (21,'Horror','Very scary story.'),
            (22,'Thriller','A story with feelings of terror and thrill.'),
            (23,'Action',null),
            (24,'Cartoon','A drawn fantasy movie.');

    END IF;
    RETURN S;
END //

DELIMITER ;