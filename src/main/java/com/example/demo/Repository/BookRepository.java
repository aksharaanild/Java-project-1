package com.example.demo.Repository;

import com.example.demo.Model.Books;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
@Qualifier("repositoryA")
public interface BookRepository extends JpaRepository<Books, Long>, BookRepositoryCustom{

    List<Books> findByAuthor(String author, Sort sort);
    @Query("SELECT b FROM Books b WHERE b.title = :title")
    List<Books> findByExactTitleMatch(@Param("title") String title, Sort sort);

}
