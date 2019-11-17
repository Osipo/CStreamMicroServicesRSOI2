CREATE TABLE IF NOT EXISTS seance(
	cid BIGINT NOT NULL,
    fid BIGINT NOT NULL,
    begining DATE NOT NULL,
    CONSTRAINT seance_PK PRIMARY KEY(cid,fid),
    CONSTRAINT date_check CHECK( begining > CURDATE())
);