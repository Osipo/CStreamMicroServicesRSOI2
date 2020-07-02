package ru.osipov.deploy.entities;

import java.io.Serializable;

public class SeancePK implements Serializable {
    protected Long sid;
    protected Long rid;

    public SeancePK(){}

    public SeancePK(Long sid, RoomsCinema rid){
        this.sid = sid;
        this.rid = rid.getRid();
    }
}
