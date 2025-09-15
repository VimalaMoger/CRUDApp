package com.moger.crudproject.dao;

import com.moger.crudproject.entity.Role;
import com.moger.crudproject.entity.User;

public interface UserDAO {

    //users data
    void save(User tempUser);

    void save(Role authorities);

    void saveUsersRoles(User user, Role role);

    User findByUsername(String userName);
}
