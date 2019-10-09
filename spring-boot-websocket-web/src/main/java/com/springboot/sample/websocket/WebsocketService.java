package com.springboot.sample.websocket;

import java.util.Collection;

public interface WebsocketService<T> {

    public void broadCast(String message, Collection<T> collection);

    public void add(T t, Collection<T> collection);

    public void remove(T t, Collection<T> collection);
}
