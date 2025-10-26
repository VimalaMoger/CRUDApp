package com.moger.crudproject;

import com.moger.crudproject.config.SecurityConfig;
import com.moger.crudproject.entity.Role;
import com.moger.crudproject.entity.User;
import com.moger.crudproject.serviceImpl.UsersDAOImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private SecurityConfig config;

    @InjectMocks
    private UsersDAOImpl userDaoImpl;

    @Test
    public void saveUserTest() {

       User user = buildUser();
       doNothing().when(entityManager).persist(any(User.class));
       userDaoImpl.save(user);
       verify(entityManager, times(1)).persist(user);
    }

    @Test
    public void saveUserRolesTest() {

        User user = buildUser();
        Role role = new Role("ROLE_STU");

        when(entityManager.createNativeQuery(any())).thenReturn(query);
        when(query.setParameter(eq(1), any())).thenReturn(query);
        when(query.setParameter(eq(2), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);  //simulate 1 row updated
        userDaoImpl.saveUsersRoles(user, role);

        verify(entityManager).createNativeQuery(any());
        verify(query, atMost(2)).setParameter(eq(1), any());
        verify(query, atLeastOnce()).setParameter(eq(1), any());
    }

    @Test
    public void findUserByUsernameTest() {

        String userName = "Pansy";

        when(entityManager.createNativeQuery(any(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        query.setParameter("theUserName", "Pansy");
        when(query.getSingleResult()).thenReturn(buildUser());

        User user = userDaoImpl.findByUsername(userName);
        assertEquals(userName, user.getUserName());
        verify(entityManager).createNativeQuery(any(), eq(User.class));
        verify(query).getSingleResult();
        verify(query, atMost(2)).setParameter(anyString(), any());
        verify(query, atLeastOnce()).setParameter(anyString(), any());
    }

    User buildUser() {

        User user = new User();
        user.setId(1);
        user.setUserName("Pansy");
        user.setPassword("123");
        user.setEnabled(true);
        return user;
    }
}
