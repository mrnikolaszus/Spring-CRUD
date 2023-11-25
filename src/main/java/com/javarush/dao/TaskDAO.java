package com.javarush.dao;

import com.javarush.domain.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TaskDAO {

    private final SessionFactory sessionFactory;

    public TaskDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Task> allTasks(int offset, int limit){
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Task", Task.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
    }
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int allCount(){
        return Math.toIntExact(sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Task", Long.class)
                .getSingleResult());
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public Task findById(int id){
        return sessionFactory.getCurrentSession()
                .get(Task.class, id);


    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(Task task){
        getSession().persist(task);

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Task task){
        getSession().remove(task);

    }

    private Session getSession(){
        return sessionFactory.getCurrentSession();
    }

}
