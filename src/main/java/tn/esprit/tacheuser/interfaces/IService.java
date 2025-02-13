package tn.esprit.tacheuser.interfaces;

import java.util.List;

public interface IService<T> {
    //CRUD
    public void add(T t);
    public void update(T t);
    public void delete (T t);
    public List<T> getAll();
   // getOne(int id);


}
