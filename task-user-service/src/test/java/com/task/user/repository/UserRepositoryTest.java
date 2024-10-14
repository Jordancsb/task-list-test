package com.task.user.repository;

import com.task.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") 
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setName("Jordan Cruz");
        testUser.setEmail("jordan@example.com");
        userRepository.save(testUser);
    }

    @Test
    void testFindByEmail() {

        User foundUser = userRepository.findByEmail("jordan@example.com");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(foundUser.getName()).isEqualTo(testUser.getName());
    }

    @Test
    void testFindByEmailNotFound() {
        User foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertThat(foundUser).isNull();
    }
}
