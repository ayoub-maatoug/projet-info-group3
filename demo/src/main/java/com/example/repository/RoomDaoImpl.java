
package com.example.repository;

import com.example.model.Light;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class RoomDaoImpl implements RoomDaoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Light> findOnLightsInRoom(Integer ID) {
        String jpql = "select lt from Light lt join lt.room r where  r.id = :ID";
        return em.createQuery(jpql, Light.class)
                .setParameter("ID", ID)
                .getResultList();
    }
}
