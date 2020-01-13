package ru.osipov.deploy.services;

import ru.osipov.deploy.models.UpdateCinema;

public interface IQueue {
    public void push(UpdateCinema c);
    public UpdateCinema get();
    public Integer size();
}
