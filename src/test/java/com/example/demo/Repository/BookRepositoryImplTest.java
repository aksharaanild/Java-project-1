package com.example.demo.Repository;

import com.example.demo.Model.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookRepositoryImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookRepositoryImpl bookRepositoryImpl;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Books> criteriaQuery;

    @Mock
    private Root<Books> root;

    @Mock
    private TypedQuery<Books> typedQuery;

    @Mock
    private Predicate predicate;



    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        // Set up the mock typedQuery and inject it into the bookRepository
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Books.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Books.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        // Inject the mock typedQuery into the bookRepository
        bookRepositoryImpl.setTypedQuery(typedQuery);

        // Manually inject the entityManager into the bookRepositoryImpl
        ReflectionTestUtils.setField(bookRepositoryImpl, "entityManager", entityManager);

    }


    @Test
    @DisplayName("Should return an empty list when no books match the provided keywords")
    void findByKeywordsWhenNoKeywordsMatch() {
        List<String> keywords = Arrays.asList("fiction", "novel");
        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList());

        List<Books> result = bookRepositoryImpl.findByKeywords(keywords, sort);

        verify(criteriaBuilder, times(2)).like(any(), (String) any());
        verify(criteriaQuery).where((Predicate) any());
        verify(criteriaQuery).orderBy((List<Order>) any());
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).getResultList();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return the books in their natural order when sort parameter is not provided or is unsorted")
    void findByKeywordsWhenSortIsUnsortedOrNotProvided() {
        List<String> keywords = Arrays.asList("Java", "Programming");
        Sort sort = Sort.unsorted();

        List<Books> expectedBooks = Arrays.asList(
                new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "https://example.com/book1", 4),
                new Books(2L, "Advanced Java Programming", "Jane Smith", "2021-02-01", "200", "100", "https://example.com/book2", 5)
        );

        when(typedQuery.getResultList()).thenReturn(expectedBooks);

        // Create a list to hold the predicates
        List<Predicate> predicates = new ArrayList<>();

        // Loop through each keyword and create a predicate for each one
        for (String keyword : keywords) {
            // Call the method and verify the call
            Predicate predicate = criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
            verify(criteriaBuilder, times(1)).like(root.get("title"), "%" + keyword + "%");
            predicates.add(predicate);
        }

        // Use the or method to combine the predicates with OR condition
        Predicate finalPredicate = criteriaBuilder.or(predicates.toArray(new Predicate[0]));

        // Set the final predicate in the query
        when(criteriaQuery.where(finalPredicate)).thenReturn(criteriaQuery);

        List<Books> actualBooks = bookRepositoryImpl.findByKeywords(keywords, sort);

        assertEquals(expectedBooks, actualBooks);
        verify(criteriaQuery, times(1)).where(finalPredicate);

        verify(criteriaQuery, never()).orderBy((List<Order>) any());
    }

    @Test
    @DisplayName("Should sort the returned books in ascending order when sort parameter is provided and is ascending")
    void findByKeywordsWhenSortIsAscending() {
        List<String> keywords = Arrays.asList("java", "programming");
        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        Books book1 = new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "https://example.com/book1", 4);
        Books book2 = new Books(2L, "Advanced Java Programming", "Jane Smith", "2021-02-01", "200", "100", "https://example.com/book2", 5);
        List<Books> expectedBooks = Arrays.asList(book1, book2);

        when(typedQuery.getResultList()).thenReturn(expectedBooks);

        List<Books> actualBooks = bookRepositoryImpl.findByKeywords(keywords, sort);

        assertEquals(expectedBooks, actualBooks);
        verify(criteriaBuilder, times(2)).like(any(), (String) any());
        verify(criteriaQuery).where((Expression<Boolean>) any());
        verify(criteriaQuery).orderBy((List<Order>) any());
        verify(typedQuery).getResultList();
    }

    @Test
    public void testFindByKeywordsWhenSortOrderIsDescending() {
        // Arrange
        List<String> keywords = Arrays.asList("Java");
        Sort sort = Sort.by(Sort.Order.desc("title"));

        // Mocking TypedQuery behavior
        List<Books> expectedBooks = Arrays.asList(new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "https://example.com/book1", 4),
                new Books(2L, "Advanced Java", "Jane Smith", "2021-02-01", "200", "100", "https://example.com/book2", 5));
        // Mocking EntityManager and TypedQuery
        CriteriaBuilder cb = org.mockito.Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Books> query = org.mockito.Mockito.mock(CriteriaQuery.class);
        Root<Books> root = org.mockito.Mockito.mock(Root.class);
        TypedQuery<Books> typedQuery = org.mockito.Mockito.mock(TypedQuery.class);

        // Mock the behavior of the EntityManager and TypedQuery
        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Books.class)).thenReturn(query);
        when(query.from(Books.class)).thenReturn(root);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedBooks);

        // Perform the test
        List<Books> result = bookRepositoryImpl.findByKeywords(keywords, sort);

        // Verify the results
        assertEquals(expectedBooks, result);
    }
}
