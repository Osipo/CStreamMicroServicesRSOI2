package ru.osipov.deploy.entities;

import java.io.Serializable;

public class SeancePK implements Serializable {
    protected Long cid;
    protected Long fid;

    public SeancePK(){}

    public SeancePK(Long cid, Long fid){
        this.cid = cid;
        this.fid = fid;
    }

}
