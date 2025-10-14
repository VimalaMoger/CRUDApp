package com.moger.crudproject;

import com.moger.crudproject.config.SecurityConfig;
import com.moger.crudproject.entity.User;
import com.moger.crudproject.serviceImpl.UsersDAOImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@MockitoSettings(strictness = Strictness.LENIENT)
//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

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
       entityManager.persist(any());
       userDaoImpl.save(user);
       verify(entityManager, times(1)).persist(user);
    }

    @Test
    public void findUserByUsernameTest() {
        //Class<User> entityClass = User.class;
        String userName = "Pansy";
        //String jpqlQuery = "select * from user where user_name=userName";
        when(entityManager.createNativeQuery(any(), eq(User.class))).thenReturn(query);
        when(query.setParameter(eq("name"), any())).thenReturn(query);
        //query.setParameter("paramName", userName);
        when(query.getSingleResult()).thenReturn(buildUser());

//"theUserName", "Pansy"
        User user = userDaoImpl.findByUsername(userName);
        assertEquals(userName, user.getUserName());
        verify(entityManager).createQuery("jpqlQuery");
        verify(query).getResultList();
        verify(query).setParameter("paramName", userName);
    }
/*  when(userRepository.findById(2L)).thenReturn(Optional.empty());

Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.findUserById(2L);
        });
        assertEquals("User not found", exception.getMessage());
 */
    User buildUser() {

        User user = new User();
        user.setId(1);
        user.setUserName("Pansy");
        user.setPassword("123");
        user.setEnabled(true);
        return user;
    }
}
