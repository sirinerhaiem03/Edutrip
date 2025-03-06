package tn.edutrip.services;
import java.util.List;

public interface IService <T>{
    void add(T t);
    void remove(int id);
    void update(T t);
    List<T> getAll();
}
