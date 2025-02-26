package com.iut.banque.test.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;

import com.iut.banque.facade.BanqueFacade;
import com.iut.banque.facade.BanqueManager;
import com.iut.banque.facade.LoginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.iut.banque.constants.LoginConstants;
import com.iut.banque.exceptions.*;
        import com.iut.banque.modele.*;

public class TestBanqueFacade {

    private BanqueManager banqueManager;
    private LoginManager loginManager;
    private BanqueFacade banqueFacade;

    @BeforeEach
    void setUp() {
        banqueManager = mock(BanqueManager.class);
        loginManager = mock(LoginManager.class);
        banqueFacade = new BanqueFacade(loginManager, banqueManager);
    }

    // 🔹 Test de connexion utilisateur
    @Test
    void testTryLogin_ManagerConnected_LoadsClients() {
        when(loginManager.tryLogin("admin", "password")).thenReturn(LoginConstants.MANAGER_IS_CONNECTED);

        int result = banqueFacade.tryLogin("admin", "password");

        assertEquals(LoginConstants.MANAGER_IS_CONNECTED, result);
        verify(banqueManager, times(1)).loadAllClients();
    }

    @Test
    void testTryLogin_Failed_NoClientsLoaded() {
        when(loginManager.tryLogin("user", "wrongpassword")).thenReturn(LoginConstants.LOGIN_FAILED); // Remplace FAILED par la bonne constante

        int result = banqueFacade.tryLogin("user", "wrongpassword");

        assertEquals(LoginConstants.LOGIN_FAILED, result);
        verify(banqueManager, never()).loadAllClients();
    }

    @Test
    void testLogout_CallsLoginManagerLogout() {
        banqueFacade.logout();
        verify(loginManager, times(1)).logout();
    }

    @Test
    void testCrediter_Success() throws IllegalFormatException {
        Compte compte = mock(Compte.class);
        banqueFacade.crediter(compte, 100.0);
        verify(banqueManager, times(1)).crediter(compte, 100.0);
    }

    @Test
    void testCrediter_NegativeAmount_ThrowsException() throws IllegalFormatException {
        Compte compte = mock(Compte.class);
        doThrow(new IllegalFormatException("Montant invalide")).when(banqueManager).crediter(compte, -50.0);

        assertThrows(IllegalFormatException.class, () -> banqueFacade.crediter(compte, -50.0));
    }

    @Test
    void testDebiter_Success() throws InsufficientFundsException, IllegalFormatException {
        Compte compte = mock(Compte.class);
        banqueFacade.debiter(compte, 50.0);
        verify(banqueManager, times(1)).debiter(compte, 50.0);
    }

    @Test
    void testDebiter_InsufficientFunds_ThrowsException() throws InsufficientFundsException, IllegalFormatException {
        Compte compte = mock(Compte.class);
        doThrow(new InsufficientFundsException("Fonds insuffisants")).when(banqueManager).debiter(compte, 500.0);

        assertThrows(InsufficientFundsException.class, () -> banqueFacade.debiter(compte, 500.0));
    }

    @Test
    void testGetAllClients_ReturnsMap() {
        Map<String, Client> clients = new HashMap<>();
        clients.put("C001", mock(Client.class));

        when(banqueManager.getAllClients()).thenReturn(clients);

        Map<String, Client> result = banqueFacade.getAllClients();

        assertEquals(1, result.size());
        assertTrue(result.containsKey("C001"));
    }

    @Test
    void testCreateAccount_Success() throws TechnicalException, IllegalFormatException {
        Client client = mock(Client.class);
        when(loginManager.getConnectedUser()).thenReturn(mock(Gestionnaire.class));

        banqueFacade.createAccount("12345", client);

        verify(banqueManager, times(1)).createAccount("12345", client);
    }

    @Test
    void testCreateAccount_UserNotManager_DoesNothing() throws TechnicalException, IllegalFormatException {
        Client client = mock(Client.class);
        when(loginManager.getConnectedUser()).thenReturn(mock(Utilisateur.class)); // Pas un gestionnaire

        banqueFacade.createAccount("12345", client);

        verify(banqueManager, never()).createAccount(anyString(), any(Client.class));
    }

    @Test
    void testDeleteAccount_Success() throws IllegalOperationException, TechnicalException {
        Compte compte = mock(Compte.class);
        when(loginManager.getConnectedUser()).thenReturn(mock(Gestionnaire.class));

        banqueFacade.deleteAccount(compte);

        verify(banqueManager, times(1)).deleteAccount(compte);
    }

    @Test
    void testDeleteAccount_UserNotManager_DoesNothing() throws IllegalOperationException, TechnicalException {
        Compte compte = mock(Compte.class);
        when(loginManager.getConnectedUser()).thenReturn(mock(Utilisateur.class));

        banqueFacade.deleteAccount(compte);

        verify(banqueManager, never()).deleteAccount(any(Compte.class));
    }

    @Test
    void testCreateManager_Success() throws TechnicalException, IllegalArgumentException, IllegalFormatException {
        when(loginManager.getConnectedUser()).thenReturn(mock(Gestionnaire.class));

        banqueFacade.createManager("admin", "pass", "Nom", "Prenom", "Adresse", true);

        verify(banqueManager, times(1)).createManager("admin", "pass", "Nom", "Prenom", "Adresse", true);
    }

    @Test
    void testCreateManager_UserNotManager_DoesNothing() throws TechnicalException, IllegalArgumentException, IllegalFormatException {
        when(loginManager.getConnectedUser()).thenReturn(mock(Utilisateur.class));

        banqueFacade.createManager("admin", "pass", "Nom", "Prenom", "Adresse", true);

        verify(banqueManager, never()).createManager(anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean());
    }

    @Test
    void testDeleteUser_Success() throws IllegalOperationException, TechnicalException {
        Utilisateur user = mock(Utilisateur.class);
        when(loginManager.getConnectedUser()).thenReturn(mock(Gestionnaire.class));

        banqueFacade.deleteUser(user);

        verify(banqueManager, times(1)).deleteUser(user);
    }

    @Test
    void testDeleteUser_UserNotManager_DoesNothing() throws IllegalOperationException, TechnicalException {
        Utilisateur user = mock(Utilisateur.class);
        when(loginManager.getConnectedUser()).thenReturn(mock(Utilisateur.class));

        banqueFacade.deleteUser(user);

        verify(banqueManager, never()).deleteUser(any(Utilisateur.class));
    }
}
