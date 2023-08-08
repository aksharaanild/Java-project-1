package com.example.demo.Repository;

import com.example.demo.Model.Books;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface BookRepositoryCustom{
    List<Books> findByKeywords(List<String> keywords, Sort sort);

}
