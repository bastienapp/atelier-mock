package com.wildcodeschool.mockexample.service;

import com.google.common.hash.Hashing;
import com.wildcodeschool.mockexample.entity.User;
import com.wildcodeschool.mockexample.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class UserManager {

    @Autowired
    private UserRepository userRepository;

    /**
     * TODO sign in
     *
     * @param email    of user
     * @param password of user
     * @return user if exists
     * @throws IllegalArgumentException if email or password is empty
     * @throws NotFoundException        if user isn't found
     */
    public User signIn(String email, String password) throws IllegalArgumentException, NotFoundException {

        if (email.isEmpty()) {
            throw new IllegalArgumentException("email is empty");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("password is empty");
        }

        Optional<User> optionalUser = userRepository.findByEmailAndPassword(email, password);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NotFoundException("user not found");
        }
    }

    /**
     * TODO sign up
     *
     * @param user to create
     * @return user created
     */
    public User signUp(User user) {

        user.setPassword(encrypt(user.getPassword()));
        return userRepository.save(user);
    }

    private String encrypt(String password) {
        return Hashing.sha256()
                .hashString("$4|t" + password, StandardCharsets.UTF_8)
                .toString();
    }
}
