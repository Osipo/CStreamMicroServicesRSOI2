CREATE TABLE customer_orders(
    oid bigint not null auto_increment,
    totalSum decimal(20,10) not null,
    createdAt DATE NOT NULL,
    creationTime TIME(2) NOT NULL,
    updatedAt DATE NOT NULL,
    ostatus VARCHAR(10) NOT NULL DEFAULT 'SELECTED',
    uid bigint not null,
    CONSTRAINT customer_orders primary key (oid)
);
CREATE TABLE order_details(
    id bigint not null auto_increment,
    oid bigint not null,
    price decimal(20,10) not null,
    discount decimal(20,10) not null,
    sid bigint not null,
    seat_id bigint not null,
    CONSTRAINT order_details_PK PRIMARY KEY(id),
    CONSTRAINT order_details_FK FOREIGN KEY(oid) REFERENCES customer_orders(oid) ON UPDATE CASCADE ON DELETE CASCADE
);
