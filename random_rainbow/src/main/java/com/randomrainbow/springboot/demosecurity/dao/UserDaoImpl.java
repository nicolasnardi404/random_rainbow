package com.randomrainbow.springboot.demosecurity.dao;

import com.randomrainbow.springboot.demosecurity.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDaoImpl implements UserDao {

	private EntityManager entityManager;

	@Autowired
	public UserDaoImpl(EntityManager theEntityManager) {
		this.entityManager = theEntityManager;
	}

	public Optional<User> findByUserName(String theUserName) {
        TypedQuery<User> theQuery = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.userName = :theUserName AND u.enabled = true", User.class);
        theQuery.setParameter("theUserName", theUserName);
        try {
            return Optional.ofNullable(theQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

	@Override
	@Transactional
	public void save(User theUser) {

		// create the user ... finally LOL
		entityManager.merge(theUser);
	}

}
