package com.repository;

import com.models.Book;
import com.models.Role;
import com.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<User> getAllStudents() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> rootEntry = cq.from(User.class);
            Predicate predicateRole = cb.equal(rootEntry.get("role"), 2);
            CriteriaQuery<User> all = cq.select(rootEntry);
            TypedQuery<User> allQuery = session.createQuery(all.where(predicateRole));
            return allQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public User findByUserName(String username) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user;
        try {
            CriteriaBuilder builder1 = session.getCriteriaBuilder();
            CriteriaQuery<User> q1 = builder1.createQuery(User.class);
            Root<User> root1 = q1.from(User.class);
            Predicate predicateUsername = builder1.equal(root1.get("username"), username);
            user = session.createQuery(q1.where(predicateUsername)).getSingleResult();
        } catch (NoResultException noResultException) {
            user = null;
        } finally {
            session.close();
        }
        return user;
    }

    public void addUser(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            CriteriaBuilder builder1 = session.getCriteriaBuilder();
            CriteriaQuery<Role> q1 = builder1.createQuery(Role.class);
            Root<Role> root1 = q1.from(Role.class);

            Predicate predicateRole = builder1.equal(root1.get("name"), "ROLE_STUDENT");
            Role role = session.createQuery(q1.where(predicateRole)).getSingleResult();
            user.setRole(role);
            user.setEvent("restored");
            session.persist(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void borrowBook(User user, Book book) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            if (user.check(book)) {
                user.getBorrowedBooks().add(book);
                book.setCount(book.getCount()-1);
            }
            session.merge(user);
            session.merge(book);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void returnBook(User user, Book book) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            user.getBorrowedBooks().remove(book);
            book.setCount(book.getCount()+1);
            session.merge(user);
            session.merge(book);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void update(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            session.merge(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void deleteStudent(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            user.setEvent("deleted");
            session.merge(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void restoreStudent(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            user.setEvent("restored");
            session.merge(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }
}
