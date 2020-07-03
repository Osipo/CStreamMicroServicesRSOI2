CREATE TABLE IF NOT EXISTS rooms_cinema(
  room_id bigint not null,
  cid bigint not null,
  CONSTRAINT rooms_cinema_PK PRIMARY KEY(room_id)
);
CREATE TABLE IF NOT EXISTS seance(
    sid bigint not null,
    room_id bigint not null,
    fid bigint not null,
    begining_date DATE not null,
    begining_time TIME(2) not null,
    CONSTRAINT seance_PK PRIMARY KEY(sid, room_id),
    CONSTRAINT seance_FK_room_id FOREIGN KEY(room_id)
    REFERENCES rooms_cinema(room_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ticket(
     sid bigint not null,
     room_id bigint not null,
     seat_id bigint not null,
     price DECIMAL(20,10) NOT NULL,
     payment_type VARCHAR(20) NOT NULL DEFAULT 'Cash',
     CONSTRAINT ticket_PK PRIMARY KEY(sid, seat_id),
     CONSTRAINT ticket_FK FOREIGN KEY(sid, room_id) REFERENCES seance(sid, room_id)
     ON UPDATE CASCADE     ON DELETE CASCADE
);