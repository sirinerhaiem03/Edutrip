package tn.esprit.tacheuser.services;

import tn.esprit.tacheuser.models.User;

import java.util.List;

public interface IUserService {
    void addUser(User user);
    User login(String email, String password);
    User getUserByEmail(String email);
    void deleteUser(int userId);
    void updateUser(User user);
    List<User> getAllUsers();
    List<User> searchUser(String keyword);  // Ajout de la méthode searchUser
}
