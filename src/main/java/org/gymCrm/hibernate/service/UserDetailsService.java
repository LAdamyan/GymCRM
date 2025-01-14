package org.gymCrm.hibernate.service;

import org.gymCrm.hibernate.model.User;


public interface UserDetailsService <T extends User>{

    boolean authenticate(String username, String password);
}
