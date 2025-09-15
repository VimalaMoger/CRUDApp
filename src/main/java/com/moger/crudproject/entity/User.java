package com.moger.crudproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
public class User {

    //define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userName;

    private String password;
    private boolean enabled;

    public User(String userName, String password, boolean enabled) {

        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
    }

    //many-to-many relationship between users and roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

}

