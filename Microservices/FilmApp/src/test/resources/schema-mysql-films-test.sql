CREATE TABLE IF NOT EXISTS film(fid BIGINT NOT NULL auto_increment,
    fname VARCHAR(100) NOT NULL,
    rating SMALLINT(6) not null,
    gid BIGINT NOT NULL DEFAULT -1,
    CONSTRAINT film_PK PRIMARY KEY(fid),
    CONSTRAINT film_rating_CH CHECK( (rating >= 0) AND (rating <= 100))
);