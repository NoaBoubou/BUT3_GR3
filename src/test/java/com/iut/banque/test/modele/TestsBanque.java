package com.iut.banque.test.modele;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.iut.banque.modele.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.InsufficientFundsException;

public class TestsBanque {
    private Banque banque;
    private Compte compteMock;
    private CompteAvecDecouvert compteDecouvertMock;
    private Client clientMock;

    @BeforeEach
    void setUp() {
        banque = new Banque();
        compteMock = mock(Compte.class);
        compteDecouvertMock = mock(CompteAvecDecouvert.class);
        clientMock = mock(Client.class);
    }

    @Test
    void testSetAndGetClients() {
        Map<String, Client> clients = new HashMap<>();
        clients.put("123", clientMock);
        banque.setClients(clients);
        assertEquals(clients, banque.getClients());

        banque.setClients(null);
        assertNull(banque.getClients());
    }

    @Test
    void testSetAndGetGestionnaires() {
        Map<String, Gestionnaire> gestionnaires = new HashMap<>();
        Gestionnaire gestionnaireMock = mock(Gestionnaire.class);
        gestionnaires.put("456", gestionnaireMock);
        banque.setGestionnaires(gestionnaires);
        assertEquals(gestionnaires, banque.getGestionnaires());
    }

    @Test
    void testSetAndGetAccounts() {
        Map<String, Compte> accounts = new HashMap<>();
        accounts.put("789", compteMock);
        banque.setAccounts(accounts);
        assertEquals(accounts, banque.getAccounts());
    }

    @Test
    void testDebiter() throws InsufficientFundsException, IllegalFormatException {
        doNothing().when(compteMock).debiter(100.0);
        banque.debiter(compteMock, 100.0);
        verify(compteMock, times(1)).debiter(100.0);

        doThrow(new InsufficientFundsException("Fonds insuffisants")).when(compteMock).debiter(5000.0);
        assertThrows(InsufficientFundsException.class, () -> banque.debiter(compteMock, 5000.0));
    }

    @Test
    void testCrediter() throws IllegalFormatException {
        doNothing().when(compteMock).crediter(200.0);
        banque.crediter(compteMock, 200.0);
        verify(compteMock, times(1)).crediter(200.0);

        doThrow(new IllegalFormatException("Montant invalide")).when(compteMock).crediter(-50.0);
        assertThrows(IllegalFormatException.class, () -> banque.crediter(compteMock, -50.0));
    }

    @Test
    void testDeleteUser() {
        Map<String, Client> clients = new HashMap<>();
        clients.put("123", clientMock);
        banque.setClients(clients);
        banque.deleteUser("123");
        assertFalse(banque.getClients().containsKey("123"));

        banque.deleteUser("999");
        assertFalse(banque.getClients().containsKey("999"));
    }

    @Test
    void testChangeDecouvert() throws IllegalFormatException, IllegalOperationException {
        doNothing().when(compteDecouvertMock).setDecouvertAutorise(500.0);
        banque.changeDecouvert(compteDecouvertMock, 500.0);
        verify(compteDecouvertMock, times(1)).setDecouvertAutorise(500.0);

        doThrow(new IllegalFormatException("Format invalide")).when(compteDecouvertMock).setDecouvertAutorise(-100.0);
        assertThrows(IllegalFormatException.class, () -> banque.changeDecouvert(compteDecouvertMock, -100.0));
    }

    @Test
    void testRandomOperations() throws InsufficientFundsException, IllegalFormatException, IllegalOperationException {
        double montant = 2045;
        doNothing().when(compteMock).crediter(montant);
        banque.crediter(compteMock, montant);
        verify(compteMock, times(1)).crediter(montant);
    }

    @Test
    void testDebiterSansFonds() throws InsufficientFundsException, IllegalFormatException {
        doThrow(new InsufficientFundsException("Fonds insuffisants")).when(compteMock).debiter(1000.0);

        assertThrows(InsufficientFundsException.class, () -> banque.debiter(compteMock, 1000.0));
    }
}
