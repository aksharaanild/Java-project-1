//package com.example.demo.Component;
//
//import com.example.demo.Model.Books;
//import com.example.demo.Repository.BookRepository;
//import com.opencsv.CSVReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.io.FileReader;
//import java.util.List;
//
//
//public class MyStartup{
//
////    @Autowired
////    private BookRepository bookRepository;
////
////    @Override
////    public void run(ApplicationArguments args) throws Exception{
////        //Load CSV file
////        ClassPathResource resource = new ClassPathResource("data.csv");
////        CSVReader csvReader = new CSVReader(new FileReader(resource.getFile()));
////
////        //Parse CSV data
////        List<String[]> data = csvReader.readAll();
////
////        for (String[] row : data) {
////            Books books = new Books();
////            books.setTitle(row[0]);
////            books.setAuthor(row[1]);
////            books.setDate(row[2]);
////            books.setViews(row[3]);
////            books.setLikes(row[4]);
////            books.setLink(row[5]);
////            bookRepository.save(books);
////        }
////        csvReader.close();
////    }
//
//}
