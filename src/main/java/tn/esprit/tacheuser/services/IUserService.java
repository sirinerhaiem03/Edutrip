package tn.esprit.tacheuser.services;

import tn.esprit.tacheuser.models.User;
import java.util.List;

public interface IUserService {
    void addUser(User user);
    User login(String email, String password);
    void deleteUser(int userId);
    void updateUser(User user);
    List<User> getAllUsers();
}
