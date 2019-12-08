package ru.osipov.deploy.utils;

import lombok.Data;

@Data
public class ElementType<T> {
    private T element;
    private ElementType<T> next;
}
