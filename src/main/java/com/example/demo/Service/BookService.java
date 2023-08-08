package com.example.demo.Service;

import com.example.demo.Model.Books;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Repository.BookRepositoryCustom;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.List;


@Service
@Component
public class BookService implements ApplicationRunner {

    private BookServiceInterface bookServiceInterface;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception{

        //Load CSV file
        ClassPathResource resource = new ClassPathResource("data.csv");
        CSVReader csvReader = new CSVReader(new FileReader(resource.getFile()));

        //Parse CSV data
        List<String[]> data = csvReader.readAll();

        for (String[] row : data) {
            Books books = new Books();
            books.setTitle(row[0]);
            books.setAuthor(row[1]);
            books.setDate(row[2]);
            books.setViews(row[3]);
            books.setLikes(row[4]);
            books.setLink(row[5]);
            if (row[6] != null && !row[6].isEmpty() && row[6].matches("\\d+")) {
                books.setRating(Integer.parseInt(row[6]));
            }
            bookRepository.save(books);
        }
        csvReader.close();
    }

    public List<Books> getBookByAuthor(String author) {
        return bookServiceInterface.getBookByAuthor(author);
    }

    public List<Books> getBookByTitle(String title) {
        return bookServiceInterface.getBookByExactTitle(title);
    }

    public List<Books> getBookByKeyword(String title) {
        return bookServiceInterface.getBookByKeyword(title);
    }



}
