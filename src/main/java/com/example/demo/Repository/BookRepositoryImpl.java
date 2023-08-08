package com.example.demo.Repository;

import com.example.demo.Model.Books;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("repositoryB")
public class BookRepositoryImpl implements BookRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;
    private TypedQuery<Books> typedQuery; // Declare the field for the typedQuery

   

    @Override
    public List<Books> findByKeywords(List<String> keywords, Sort sort){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Books> query = cb.createQuery(Books.class);
        Root<Books> root = query.from(Books.class);

        List<Predicate> predicates = new ArrayList<>();

        for (String keyword : keywords){
            predicates.add(cb.like(root.get("title"), "%" + keyword + "%"));
        }

        query.where(cb.or(predicates.toArray(new Predicate[0])));

        if (sort != null && !sort.isUnsorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order sortOrder : sort) {
                String property = sortOrder.getProperty();
                if (sortOrder.isAscending()) {
                    orders.add(cb.asc(root.get(property)));
                } else {
                    orders.add(cb.desc(root.get(property)));
                }
            }
            query.orderBy(orders);
        }

        typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private EntityManager getEntityManager() {
        return entityManager;
    }


    public void setTypedQuery(TypedQuery<Books> typedQuery) {
        this.typedQuery = typedQuery;
    }


}
