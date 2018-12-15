package com.seu.architecture.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(multicoreSupport = true)
public class SolrConfig {

    @Bean
    public SolrClient solrClient(@Value("${solr.host}") String solrHost) {
        return new HttpSolrClient(solrHost);
    }

    @Bean
    public SolrOperations solrOperations(SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }
}
