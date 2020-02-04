package com.wildcodeschool.mockexample;

import com.wildcodeschool.mockexample.entity.User;
import com.wildcodeschool.mockexample.repository.UserRepository;
import com.wildcodeschool.mockexample.service.UserManager;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class MockexampleApplicationTests {

    @InjectMocks
    private UserManager userManager;

    @Mock
    private UserRepository userRepository;

    @Test
    void signUp() {

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

        User actual = userManager.signUp(user);

        assertEquals(expected, actual);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void signIn() throws NotFoundException {
        String email = "bastien@wildcodeschool.fr";
        String password = "tacostacos";

        User expected = new User();
        expected.setId(1L);
        expected.setEmail(email);
        expected.setPassword(password);
        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(expected));

        User actual = userManager.signIn(email, password);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmailAndPassword(email, password);
    }

    @Test
    void signInEmailEmpty() {
        String email = "";
        String password = "tacostacos";

        assertThrows(IllegalArgumentException.class, () -> userManager.signIn(email, password));
        verify(userRepository, times(0)).findByEmailAndPassword(email, password);
    }

    @Test
    void signInPasswordEmpty() {
        String email = "bastien@wildcodeschool.fr";
        String password = "";

        assertThrows(IllegalArgumentException.class, () -> userManager.signIn(email, password));
        verify(userRepository, times(0)).findByEmailAndPassword(email, password);
    }

    @Test
    void signInNotFound() {
        String email = "bastien@wildcodeschool.fr";
        String password = "lol";
        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userManager.signIn(email, password));
        verify(userRepository, times(1)).findByEmailAndPassword(email, password);
    }
}
