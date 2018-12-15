package com.seu.architecture.model;

import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.*;

/**
 * Created by 17858 on 2017-10-24.
 */
@Entity
@Table(name = "all_book")
@SolrDocument(solrCoreName = "all_book")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type",
        discriminatorType = DiscriminatorType.STRING)
public class Book {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @Indexed(type = "text_general")
    @Field(SearchableCharacteristicBook.BOOKID_FIELD_NAME)
    private Long clientId;

    @Indexed(type = "text_general")
    @Field(SearchableAllBook.TITLE_FIELD_NAME)
    private String title;

    @Indexed(type = "text_general")
    @Field(SearchableAllBook.AUTHOR_FIELD_NAME)
    private String author;

    @Indexed(type = "text_general")
    @Field(SearchableAllBook.CHUBAN_FIELD_NAME)
    private String chuban;

    @Indexed(type = "text_general")
    @Field(SearchableAllBook.CHUBANSHIJIAN_FIELD_NAME)
    private String chubanshijian;

    @Indexed(type = "text_general")
    @Field(SearchableAllBook.THUMBNAIL_FIELD_NAME)
    private String thumbnail;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChuban() {
        return chuban;
    }

    public void setChuban(String chuban) {
        this.chuban = chuban;
    }

    public String getChubanshijian() {
        return chubanshijian;
    }

    public void setChubanshijian(String chubanshijian) {
        this.chubanshijian = chubanshijian;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
