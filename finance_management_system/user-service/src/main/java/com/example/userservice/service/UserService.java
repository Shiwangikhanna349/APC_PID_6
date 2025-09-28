package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(String name) {
        User user = new User(name);
        return userRepository.save(user);
    }

    public User addUser(String name, String email, String phoneNumber) {
        User user = new User(name, email, phoneNumber);
        return userRepository.save(user);
    }

    public User addUser(String name, String email, String phoneNumber, String address, String bankAccountNumber) {
        User user = new User(name, email, phoneNumber, address, bankAccountNumber);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, String name) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(name);
            return userRepository.save(user);
        }
        return null;
    }

    public User updateUser(Long id, String name, String email, String phoneNumber, String address, String bankAccountNumber) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setAddress(address);
            user.setBankAccountNumber(bankAccountNumber);
            return userRepository.save(user);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
