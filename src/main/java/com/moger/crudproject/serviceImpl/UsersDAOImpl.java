package com.moger.crudproject.serviceImpl;

import com.moger.crudproject.dao.UserDAO;
import com.moger.crudproject.entity.Role;
import com.moger.crudproject.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsersDAOImpl implements UserDAO {

    //define field for entity manager
    private final EntityManager entityManager;

    //Inject entity manager using constructor injection
    public UsersDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(User tempUser) {

        entityManager.persist(tempUser);
    }

    @Override
    @Transactional
    public void save(Role authorities) {

        entityManager.persist(authorities);
    }

    @Override
    @Transactional
    public void saveUsersRoles(User user, Role role) {

        Query query = entityManager.createNativeQuery("Insert into users_roles(user_id, role_id) values(?1, ?2)");
        query.setParameter(1, user.getId());
        query.setParameter(2, role.getId());
        query.executeUpdate();
    }

    @Override
    public User findByUsername(String userName) {

        Query query = entityManager.createNativeQuery("select * from user where user_name=:theUserName", User.class);
        query.setParameter("theUserName", userName);
        return (User) query.getSingleResult();
    }
}
