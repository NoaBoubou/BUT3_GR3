package com.iut.banque.test.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.iut.banque.constants.LoginConstants;
import com.iut.banque.interfaces.IDao;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Gestionnaire;
import com.iut.banque.facade.LoginManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TestLoginManager {
    private LoginManager loginManager;
    private IDao daoMock;

    @BeforeEach
    void setUp() {
        loginManager = new LoginManager();
        daoMock = Mockito.mock(IDao.class);
        loginManager.setDao(daoMock);
    }

    @Test
    void testTryLogin_Success_Client() {
        Client client = new Client();

        when(daoMock.isUserAllowed("client123", "password")).thenReturn(true);
        when(daoMock.getUserById("client123")).thenReturn(client);

        int result = loginManager.tryLogin("client123", "password");

        assertEquals(LoginConstants.USER_IS_CONNECTED, result);
        assertEquals(client, loginManager.getConnectedUser());
    }

    @Test
    void testTryLogin_Success_Gestionnaire() {
        Gestionnaire manager = new Gestionnaire();

        when(daoMock.isUserAllowed("admin123", "adminpass")).thenReturn(true);
        when(daoMock.getUserById("admin123")).thenReturn(manager);

        int result = loginManager.tryLogin("admin123", "adminpass");

        assertEquals(LoginConstants.MANAGER_IS_CONNECTED, result);
        assertEquals(manager, loginManager.getConnectedUser());
    }

    @Test
    void testTryLogin_Failed() {
        when(daoMock.isUserAllowed("wrongUser", "wrongPass")).thenReturn(false);

        int result = loginManager.tryLogin("wrongUser", "wrongPass");

        assertEquals(LoginConstants.LOGIN_FAILED, result);
        assertNull(loginManager.getConnectedUser());
    }

    @Test
    void testGetConnectedUser() {
        Client client = new Client();
        loginManager.setCurrentUser(client);

        assertEquals(client, loginManager.getConnectedUser());
    }

    @Test
    void testLogout() {
        Client client = new Client();
        loginManager.setCurrentUser(client);

        loginManager.logout();

        assertNull(loginManager.getConnectedUser());
        verify(daoMock, times(1)).disconnect();
    }
}
