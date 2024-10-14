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
        // Criação de um usuário de teste
        testUser = new User();
        testUser.setName("Jordan Cruz");
        testUser.setEmail("jordan@example.com");
        userRepository.save(testUser);
    }

    @Test
    void testFindByEmail() {
        // Executa o método findByEmail
        User foundUser = userRepository.findByEmail("jordan@example.com");

        // Verifica se o usuário retornado não é nulo e se as informações estão corretas
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(foundUser.getName()).isEqualTo(testUser.getName());
    }

    @Test
    void testFindByEmailNotFound() {
        // Busca por um email que não existe
        User foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Verifica se o resultado é nulo
        assertThat(foundUser).isNull();
    }
}
