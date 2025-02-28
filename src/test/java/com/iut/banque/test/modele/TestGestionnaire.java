package com.iut.banque.test.modele;

import static org.junit.jupiter.api.Assertions.*;

import com.iut.banque.modele.Gestionnaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.iut.banque.exceptions.IllegalFormatException;


public class TestGestionnaire {

    private Gestionnaire gestionnaire;

    @BeforeEach
    void setUp() throws IllegalFormatException {
        gestionnaire = new Gestionnaire("Durand", "Paul", "5 avenue du Centre", true, "manager1", "securePass");
    }

    @Test
    void testGetNom() {
        assertEquals("Durand", gestionnaire.getNom());
    }

    @Test
    void testSetNom() {
        gestionnaire.setNom("Martin");
        assertEquals("Martin", gestionnaire.getNom());
    }

    @Test
    void testGetPrenom() {
        assertEquals("Paul", gestionnaire.getPrenom());
    }

    @Test
    void testSetPrenom() {
        gestionnaire.setPrenom("Pierre");
        assertEquals("Pierre", gestionnaire.getPrenom());
    }

    @Test
    void testGetAdresse() {
        assertEquals("5 avenue du Centre", gestionnaire.getAdresse());
    }

    @Test
    void testSetAdresse() {
        gestionnaire.setAdresse("10 rue Principale");
        assertEquals("10 rue Principale", gestionnaire.getAdresse());
    }

    @Test
    void testIsMale() {
        assertTrue(gestionnaire.isMale());
    }

    @Test
    void testSetMale() {
        gestionnaire.setMale(false);
        assertFalse(gestionnaire.isMale());
    }

    @Test
    void testGetUserId() {
        assertEquals("manager1", gestionnaire.getUserId());
    }

    @Test
    void testSetUserIdValid() throws IllegalFormatException {
        gestionnaire.setUserId("newManager");
        assertEquals("newManager", gestionnaire.getUserId());
    }

    @Test
    void testGestionnaireConstructorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Gestionnaire("Durand", "Paul", "5 avenue du Centre", true, "", "securePass"));
    }

    @Test
    void testToString() {
        String expected = "Gestionnaire [nom=Durand, prenom=Paul, adresse=5 avenue du Centre, male=true, userId=manager1, userPwd=securePass]";
        assertEquals(expected, gestionnaire.toString());
    }
}
