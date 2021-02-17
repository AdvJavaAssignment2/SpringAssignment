package com.repository;

import com.models.Book;
import com.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository {

    private final SessionFactory sessionFactory;
    Session session;

    @Autowired
    public BookRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Book> getAllBooks() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);
            Root<Book> rootEntry = cq.from(Book.class);
            CriteriaQuery<Book> all = cq.select(rootEntry);
            TypedQuery<Book> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public Book getBook(long id) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        Book book;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Book> q1 = builder.createQuery(Book.class);
            Root<Book> root = q1.from(Book.class);

            Predicate predicateBook = builder.equal(root.get("id"), id);
            book = session.createQuery(q1.where(predicateBook)).getSingleResult();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return book;
    }

    public void addBook(Book book) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            book.setEvent("restored");
            session.persist(book);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void updateBook(Book book) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.merge(book);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void deleteBook(Book book) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            book.setEvent("deleted");
            session.merge(book);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }
}
