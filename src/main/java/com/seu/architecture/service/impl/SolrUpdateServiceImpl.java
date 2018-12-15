package com.seu.architecture.service.impl;

import com.seu.architecture.service.SolrUpdateService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by 17858 on 2017-11-16.
 */
@Service
public class SolrUpdateServiceImpl implements SolrUpdateService {


    @Override
    public boolean solrUpdate() {

        try {
            HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8983/solr/#/all_book");
            solrServer.setConnectionTimeout(5000);
            SolrQuery query = new SolrQuery();
            // 指定RequestHandler，默认使用/select
            query.setRequestHandler("/dataimport");

            String command = "full-import";
            String clean = "false";
            String optimize = "false";

            query.setParam("command", command)
                    .setParam("clean", clean)
                    .setParam("commit", "true")
                    .setParam("entity", "article")
                    .setParam("optimize", optimize);
            solrServer.query(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
