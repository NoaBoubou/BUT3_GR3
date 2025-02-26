package com.iut.banque.test.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.iut.banque.converter.ClientConverter;
import com.iut.banque.interfaces.IDao;
import com.iut.banque.modele.Client;
import com.opensymphony.xwork2.conversion.TypeConversionException;

class TestClientConverter {

    private ClientConverter converter;

    @Mock
    private IDao daoMock;

    @Mock
    private Client mockClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converter = new ClientConverter(daoMock);
    }

    @Test
    void testConvertFromString_Success() {
        Map<String, Object> context = new HashMap<>();
        String[] values = { "12345" };

        when(daoMock.getUserById("12345")).thenReturn(mockClient);

        Client result = (Client) converter.convertFromString(context, values, Client.class);

        assertNotNull(result);
        assertEquals(mockClient, result);
        verify(daoMock, times(1)).getUserById("12345");
    }

    @Test
    void testConvertFromString_NullOrEmptyValues() {
        Map<String, Object> context = new HashMap<>();

        assertThrows(NullPointerException.class, () -> {
            converter.convertFromString(context, null, Client.class);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            converter.convertFromString(context, new String[]{}, Client.class);
        });
    }

    @Test
    void testConvertFromString_ClientNotFound() {
        Map<String, Object> context = new HashMap<>();
        String[] values = { "unknown" };

        when(daoMock.getUserById("unknown")).thenReturn(null);

        assertThrows(TypeConversionException.class, () -> {
            converter.convertFromString(context, values, Client.class);
        });
    }

    @Test
    void testConvertFromString_DaoThrowsException() {
        Map<String, Object> context = new HashMap<>();
        String[] values = { "12345" };

        when(daoMock.getUserById("12345")).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            converter.convertFromString(context, values, Client.class);
        });
    }

    @Test
    void testConvertToString_Success() {
        Map<String, Object> context = new HashMap<>();
        when(mockClient.getIdentity()).thenReturn("12345");

        String result = converter.convertToString(context, mockClient);

        assertEquals("12345", result);
    }

    @Test
    void testConvertToString_NullClient() {
        Map<String, Object> context = new HashMap<>();

        String result = converter.convertToString(context, null);

        assertNull(result);
    }
}
