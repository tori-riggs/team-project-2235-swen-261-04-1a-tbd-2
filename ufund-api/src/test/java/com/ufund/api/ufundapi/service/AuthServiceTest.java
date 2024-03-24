package com.ufund.api.ufundapi.service;

import com.ufund.api.ufundapi.enums.AuthLevel;
import com.ufund.api.ufundapi.model.AuthCredentials;
import com.ufund.api.ufundapi.persistence.AuthDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@Tag("Service-tier")
public class AuthServiceTest {
    private AuthService authService;
    private AuthDAO authDao;

    @BeforeEach
    public void setupNeedController() {
        authDao = mock(AuthDAO.class);
        authService = new AuthService(authDao);
    }

    @Test
    public void whenCallHasPermissionLevel_WithNoCredentials_ThenReturnFalse() {
        //given
        when(authDao.getAuthCredentials(any())).thenReturn(null);

        //when
        boolean result = authService.hasPermissionLevel("username", "password", AuthLevel.USER);

        //then
        assertFalse(result);
    }

    @ParameterizedTest
    @CsvSource({
            "ADMIN, ADMIN, true",
            "USER, ADMIN, false",
            "ADMIN, USER, false",
            "USER, USER, true"
    })
    public void whenCallHasPermissionLevel_WithAuthLevel_ThenReturnGiven(AuthLevel given, AuthLevel expected, boolean shouldPass) {
        //given
        AuthCredentials userAuth = new AuthCredentials("username", "password", given);
        when(authDao.getAuthCredentials(any())).thenReturn(userAuth);

        //when
        boolean result = authService.hasPermissionLevel("username", "password", expected);

        //then
        assertEquals(result, shouldPass);
    }

}
