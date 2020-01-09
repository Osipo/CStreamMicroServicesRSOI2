package ru.osipov.deploy.services;

import ru.osipov.deploy.models.CreateCinema;

public interface IQueue {
    public void push(CreateCinema c);
    public CreateCinema get();
    public Integer size();
}
