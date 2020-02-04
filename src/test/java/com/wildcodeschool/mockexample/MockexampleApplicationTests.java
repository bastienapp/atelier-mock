package com.wildcodeschool.mockexample;

import com.wildcodeschool.mockexample.entity.User;
import com.wildcodeschool.mockexample.repository.UserRepository;
import com.wildcodeschool.mockexample.service.UserManager;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class MockexampleApplicationTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserManager userManager;

    // TODO
    @Test
    public void testSignInEmptyMail() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userManager.signIn("", "tacos");
        });
        verify(userRepository, times(0)).findByEmailAndPassword("email", "password");
    }

    @Test
    public void testSignInEmptyPassword() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userManager.signIn("bastien@wildcodeschool.fr", "");
        });
    }

    @Test
    public void testSignInUserDoesntExists() {

        String email = "bastien@wildcodeschool.fr";
        String password = "poulpe";
        Assertions.assertThrows(NotFoundException.class, () -> {
            when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.empty());

            userManager.signIn(email, password);

            verify(userRepository, times(1)).findByEmailAndPassword(email, password);
        });
    }

    @Test
    public void testSignInOK() throws NotFoundException {

        String email = "bastien@wildcodeschool.fr";
        String password = "tacostacos";

        User expected = new User();
        expected.setId(1L);
        expected.setEmail(email);
        expected.setPassword(password);

        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(expected));

        assertEquals(expected, userManager.signIn(email, password));
    }

    @Test
    public void signUpUserNull() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userManager.signUp(null);
        });
    }

    @Test
    public void signUpOK() {

        String email = "bastien@wildcodeschool.fr";
        String password = "tacostacos";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        User expected = new User();
        expected.setId(1L);
        expected.setEmail(email);
        expected.setPassword(password);

        when(userRepository.save(user)).thenReturn(expected);

        assertEquals(expected, userManager.signUp(user));
        verify(userRepository, times(1)).save(user);
    }
}
