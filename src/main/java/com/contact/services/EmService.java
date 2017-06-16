/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.contact.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author emmanuel
 * A singleton class that ensures that only one instance of Entity Manager is
 * created through out the entire application.
 */
@Service
public class EmService {

    @PersistenceContext
    private EntityManager entityManager;

    private static EntityManager em;

    @PostConstruct
    public void init() {
        em = this.entityManager;
    }

    public static EntityManager em() {
        return em;
    }
}
