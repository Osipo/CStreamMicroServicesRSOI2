package ru.osipov.deploy.entities;

import java.io.Serializable;

public class TicketPK implements Serializable {
    protected SeancePK sid;
    protected Long seatId;

    public TicketPK(){}

        public TicketPK(SeancePK sid, Long seatId){
        this.sid = sid;
        this.seatId = seatId;
    }
}
