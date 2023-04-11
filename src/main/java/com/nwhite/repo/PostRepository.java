package com.nwhite.repo;

import com.nwhite.model.Post;
import com.nwhite.util.HibernateUtil;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.HashMap;
import java.util.Map;

public class PostRepository {
    static SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();

    public PostRepository() {

    }

    public Post find(Long id) {
        Post post;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            post = session.find(Post.class, id);

            session.getTransaction().commit();
        }

        return post;
    }

    public Post findWithEntityGraph(Long id) {
        Post post;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            EntityGraph entityGraph = session.getEntityGraph("post-entity-graph");
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.fetchgraph", entityGraph);
            post = session.find(Post.class, id, properties);


            session.getTransaction().commit();
        }

        return post;
    }

    public Post findWithEntityGraph2(Long id) {
        Post post;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            EntityGraph entityGraph = session.createEntityGraph(Post.class);
            entityGraph.addAttributeNodes("subject");
            entityGraph.addAttributeNodes("user");
            entityGraph.addSubgraph("comments")
                    .addAttributeNodes("user");
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.fetchgraph", entityGraph);
            post = session.find(Post.class, id, properties);

            session.getTransaction().commit();
        }

        return post;
    }

    public Post findUsingJpql(Long id) {
        Post post;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            EntityGraph entityGraph = session.getEntityGraph("post-entity-graph-with-comment-users");
            post = session.createQuery("Select p from Post p where p.id=:id", Post.class)
                    .setParameter("id", id)
                    .setHint("javax.persistence.fetchgraph", entityGraph)
                    .getSingleResult();

            session.getTransaction().commit();
        }

        return post;
    }

    public Post findUsingCriteria(Long id) {
        Post post;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            EntityGraph entityGraph = session.getEntityGraph("post-entity-graph-with-comment-users");
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
            Root<Post> root = criteriaQuery.from(Post.class);
            criteriaQuery.where(criteriaBuilder.equal(root.<Long>get("id"), id));
            TypedQuery<Post> typedQuery = session.createQuery(criteriaQuery);
            typedQuery.setHint("javax.persistence.loadgraph", entityGraph);
            post = typedQuery.getSingleResult();

            session.getTransaction().commit();
        }

        return post;
    }
}
