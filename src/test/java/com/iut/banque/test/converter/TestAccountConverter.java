package com.iut.banque.test.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.iut.banque.converter.AccountConverter;
import com.iut.banque.interfaces.IDao;
import com.iut.banque.modele.Compte;
import com.opensymphony.xwork2.conversion.TypeConversionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class TestAccountConverter {

    private AccountConverter converter;
    private IDao daoMock;

    @BeforeEach
    void setUp() {
        daoMock = Mockito.mock(IDao.class);
        converter = new AccountConverter(daoMock);
    }

    @Test
    void testConvertFromString_Success() {
        Map<String, Object> context = new HashMap<>();
        String[] values = { "12345" };
        Compte expectedCompte = mock(Compte.class);

        when(daoMock.getAccountById("12345")).thenReturn(expectedCompte);

        Object result = converter.convertFromString(context, values, Compte.class);

        assertNotNull(result);
        assertEquals(expectedCompte, result);
    }

    @Test
    void testConvertFromString_Failure() {
        Map<String, Object> context = new HashMap<>();
        String[] values = { "invalid" };

        when(daoMock.getAccountById("invalid")).thenReturn(null);

        assertThrows(TypeConversionException.class, () -> {
            converter.convertFromString(context, values, Compte.class);
        });
    }

    @Test
    void testConvertToString_Success() {
        Map<String, Object> context = new HashMap<>();
        Compte compte = mock(Compte.class);

        when(compte.getNumeroCompte()).thenReturn("67890");

        String result = converter.convertToString(context, compte);

        assertEquals("67890", result);
    }

    @Test
    void testConvertToString_NullAccount() {
        Map<String, Object> context = new HashMap<>();

        String result = converter.convertToString(context, null);

        assertNull(result);
    }

    @Test
    void testConvertFromString_NullOrEmptyValues() {
        Map<String, Object> context = new HashMap<>();

        assertThrows(NullPointerException.class, () -> {
            converter.convertFromString(context, null, Compte.class);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            converter.convertFromString(context, new String[]{}, Compte.class);
        });
    }

    @Test
    void testConvertFromString_DaoThrowsException() {
        Map<String, Object> context = new HashMap<>();
        String[] values = { "12345" };

        when(daoMock.getAccountById("12345")).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            converter.convertFromString(context, values, Compte.class);
        });
    }

}
