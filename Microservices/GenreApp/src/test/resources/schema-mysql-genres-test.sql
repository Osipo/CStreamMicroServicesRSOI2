CREATE TABLE IF NOT EXISTS genre(gid BIGINT NOT NULL auto_increment,
    gname VARCHAR(100) NOT NULL,
    remarks TEXT null,
    CONSTRAINT genre_PK PRIMARY KEY(gid)
);