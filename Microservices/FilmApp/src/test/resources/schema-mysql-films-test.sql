CREATE TABLE IF NOT EXISTS film(fid BIGINT NOT NULL auto_increment,
    fname VARCHAR(100) NOT NULL,
    rating SMALLINT(6) not null,
    CONSTRAINT film_PK PRIMARY KEY(fid),
    CONSTRAINT film_rating_CH CHECK( (rating >= 0) AND (rating <= 100))
);

CREATE TABLE IF NOT EXISTS genre(gid BIGINT NOT NULL auto_increment,
    gname VARCHAR(100) NOT NULL,
    remarks TEXT null,
    CONSTRAINT genre_PK PRIMARY KEY(gid)
);

CREATE TABLE IF NOT EXISTS film_genres(
    fid bigint not null,
    gid bigint not null,
    CONSTRAINT film_genres_PK PRIMARY KEY(fid,gid),
    CONSTRAINT film_genres_FK_fid FOREIGN KEY(fid) REFERENCES film(fid) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT film_genres_FK_gid FOREIGN KEY(gid) REFERENCES genres(gid) ON UPDATE CASCADE ON DELETE CASCADE
);