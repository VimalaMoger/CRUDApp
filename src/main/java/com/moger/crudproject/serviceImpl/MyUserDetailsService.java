package com.moger.crudproject.serviceImpl;

import com.moger.crudproject.dao.UserDAO;
import com.moger.crudproject.entity.User;
import com.moger.crudproject.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    //user details service to load user info from database
    @Override
    public UserDetails loadUserByUsername(String username) throws DataNotFoundException {

        User user = userDAO.findByUsername(username);
        if (user == null)
            throw new DataNotFoundException("User not found");

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }
}


