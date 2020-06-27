INSERT INTO genres(gid,gname,remarks) VALUES
(21,'Horror','Very scary story.'),
(22,'Thriller','A story with feelings of terror and thrill.'),
(23,'Action',null),
(24,'Cartoon','A drawn fantasy movie.');

INSERT INTO film VALUES (10,'IT',18),(11,'Hellboy',16),(12,'MYST',22);

INSERT INTO film_genres (fid, gid) VALUES (10,21), (11,21),(12,24);