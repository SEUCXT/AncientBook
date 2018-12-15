package com.seu.architecture;

import com.seu.architecture.repository.BookRepository;
import com.seu.architecture.service.IndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class ArchitectureApplication {

    @Autowired
    IndexingService indexingService;

    @Autowired
    BookRepository ancientBookRepository;

    public static void main(String[] args) {
        SpringApplication.run(ArchitectureApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                // indexingService.submitIndexingJob(ImmutableList.copyOf(ancientBookRepository.findAll()));
            }
        };
    }
}
