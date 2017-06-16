package com.contact.dao;

import com.contact.models.Model;
import com.contact.services.EmService;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


public class Accessor {
    public static EntityManager em = EmService.em();

    public static void saveOne(Model model) {
        if (model.getId() == null) {
            em.persist(model);
        } else {
            em.merge(model);
        }
    }


    public static void deleteOne(Model model){
        em.remove(model);
    }

    public static <T> void deleteOne(T t){
        em.remove(t);
    }

    public static <T> T findOne(Class<T> clazz, Long id) {
        try {
            return em.find(clazz, id);
        } catch(Exception e) {
            return null;
        }
    }

    public static <T> T findOne(Class<T> clazz, String key, Object value) {
        try{
            return em.createQuery(
                "SELECT t FROM " + clazz.getSimpleName() + " t " + "WHERE t." + key + "=:value", clazz)
                .setParameter("value", value)
                .getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }

    public static <T> T findOne(Class<T> clazz, Filter filter) {
        try{
            String sql = "SELECT t FROM " + clazz.getSimpleName() + " t ";
            sql += filter.getSql();
            TypedQuery<T> q = em.createQuery(sql, clazz);
            addParam(q, filter);
            return q.getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }

    public static <T> List<T> findList(Class<T> clazz, Filter filter) {
        String sql = "SELECT t FROM " + clazz.getSimpleName() + " t ";
        sql += filter.getSql();
        TypedQuery<T> q = em.createQuery(sql, clazz);
        addParam(q, filter);
        return q.getResultList();
    }

    public static <T> List<T> findList(Class<T> clazz, Filter filter, Param param) {
        String sql = "SELECT t FROM " + clazz.getSimpleName() + " t ";
        sql += filter.getSql() + getOrder(param);
        TypedQuery<T> q = em.createQuery(sql, clazz);
        addParam(q, filter);
        q.setFirstResult(param.getOffset()).setMaxResults(param.getSize());
        return q.getResultList();
    }

    public static <T> List<T> findList(Class<T> clazz, Filter filter, String sort) {
        String sql = "SELECT t FROM " + clazz.getSimpleName() + " t ";
        sql += filter.getSql() + " ORDER BY " + sort;
        TypedQuery<T> q = em.createQuery(sql, clazz);
        addParam(q, filter);
        return q.getResultList();
    }

    public static <T> Long count(Class<T> clazz, Filter filter) {
        String sql = "SELECT COUNT(t) FROM " + clazz.getSimpleName() + " t ";
        sql += filter.getSql();
        TypedQuery<Long> q = em.createQuery(sql, Long.class);
        addParam(q, filter);
        return q.getSingleResult();
    }

    private static <T> void addParam(TypedQuery<T> q, Filter filter) {
        filter.getParams().forEach((key, value) -> {
            if(value instanceof Date) {
                q.setParameter(key, (Date)value);
            } else {
                q.setParameter(key, value);
            }
        });
    }

    private static String getOrder(Param param) {
        return StringUtils.hasText(param.getSort()) ? " ORDER BY " + param.getSort(): "";
    }
}
