package tn.edutrip.services;

import java.util.List;

public interface Iservice<T> {
    void add(T entity);
    boolean update(T entity);
    void remove(int id);
    List<T> afficher();
}
