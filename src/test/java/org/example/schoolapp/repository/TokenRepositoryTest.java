package org.example.schoolapp.repository;

import org.example.schoolapp.entity.Token;
import org.example.schoolapp.entity.User;
import org.example.schoolapp.enums.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;
    private Token validToken;
    private Token expiredToken;
    private Token revokedToken;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        tokenRepository.deleteAll();
        entityManager.clear();

        // Create a test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setLastName("Test");
        testUser.setFirstName("Test");
        testUser = entityManager.persist(testUser);

        // Create test tokens
        validToken = Token.builder()
                .token("valid-token")
                .tokenType(TokenType.BEARER)
                .user(testUser)
                .expired(false)
                .revoked(false)
                .build();

        expiredToken = Token.builder()
                .token("expired-token")
                .tokenType(TokenType.BEARER)
                .user(testUser)
                .expired(true)
                .revoked(false)
                .build();

        revokedToken = Token.builder()
                .token("revoked-token")
                .tokenType(TokenType.BEARER)
                .user(testUser)
                .expired(false)
                .revoked(true)
                .build();

        entityManager.persist(validToken);
        entityManager.persist(expiredToken);
        entityManager.persist(revokedToken);
        entityManager.flush();
    }

    @Test
    void findByToken_ShouldReturnTokenWhenExists() {
        Optional<Token> foundToken = tokenRepository.findByToken("valid-token");

        assertTrue(foundToken.isPresent());
        assertEquals("valid-token", foundToken.get().getToken());
        assertEquals(testUser.getId(), foundToken.get().getUser().getId());
    }

    @Test
    void findByToken_ShouldReturnEmptyWhenTokenNotExists() {
        Optional<Token> foundToken = tokenRepository.findByToken("non-existent-token");

        assertFalse(foundToken.isPresent());
    }

    @Test
    void deleteByToken_ShouldRemoveToken() {
        long countBefore = tokenRepository.count();
        tokenRepository.deleteByToken("valid-token");
        long countAfter = tokenRepository.count();

        assertEquals(countBefore - 1, countAfter);
        assertFalse(tokenRepository.findByToken("valid-token").isPresent());
    }

    @Test
    void save_ShouldPersistNewToken() {
        Token newToken = Token.builder()
                .token("new-token")
                .tokenType(TokenType.BEARER)
                .user(testUser)
                .expired(false)
                .revoked(false)
                .build();

        Token savedToken = tokenRepository.save(newToken);
        assertNotNull(savedToken.getId());
        assertEquals("new-token", savedToken.getToken());
    }

    @Test
    void findAllValidTokensByUser_ShouldReturnEmptyListForNonExistentUser() {
        List<Token> tokens = tokenRepository.findAllValidTokensByUser(999L);
        assertTrue(tokens.isEmpty());
    }
}
