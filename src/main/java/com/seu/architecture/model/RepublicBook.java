package com.seu.architecture.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.*;
import java.util.List;

/**
 * 民国图书
 */
@Entity
@Table(name = "republic_book")
@SolrDocument(solrCoreName = "republic_book")
@DiscriminatorValue("republic_book")
public class RepublicBook extends Book {

    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.BOOKID_FIELD_NAME)
    private Long bookid;

    // 载体形态
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.ZAITIXINGTAI_FIELD_NAME)
    private String zaitixingtai;

    // 语言
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.YUYAN_FIELD_NAME)
    private String yuyan;

    // 登记单位1
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.DENGJIDANWEI1_FEILD_NAME)
    private String dengjidanwei1;

    // 登记单位2
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.DENGJIDANWEI2_FIELD_NAME)
    private String dengjidanwei2;

    //分类号
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.FENLEIHAO_FIELD_NAME)
    private String fenleihao;

    // 本数
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.BENSHU_FEILD_NAME)
    private int benshu;

    // 图章
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.TUZHANG_FIELD_NAME)
    private String tuzhang;

    // 备注
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.BEIZHU_FIELD_NAME)
    private String beizhu;

    // 新分类号
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.XINFENLEIHAO_FIELD_NAME)
    private String xinfenleihao;

    // zhaiyao
    @Indexed(type = "text_general")
    @Field(SearchableRepublicBook.ZHAIYAO_FIELD_NAME)
    private String zhaiyao;


    @ElementCollection
    private List<String> thumbnailList;

    @ElementCollection
    private List<String> zhaopianbianhao;

    public String getZaitixingtai() {
        return zaitixingtai;
    }

    public void setZaitixingtai(String zaitixingtai) {
        this.zaitixingtai = zaitixingtai;
    }

    public String getYuyan() {
        return yuyan;
    }

    public void setYuyan(String yuyan) {
        this.yuyan = yuyan;
    }

    public String getDengjidanwei1() {
        return dengjidanwei1;
    }

    public void setDengjidanwei1(String dengjidanwei1) {
        this.dengjidanwei1 = dengjidanwei1;
    }

    public String getDengjidanwei2() {
        return dengjidanwei2;
    }

    public void setDengjidanwei2(String dengjidanwei2) {
        this.dengjidanwei2 = dengjidanwei2;
    }

    public String getFenleihao() {
        return fenleihao;
    }

    public void setFenleihao(String fenleihao) {
        this.fenleihao = fenleihao;
    }

    public int getBenshu() {
        return benshu;
    }

    public void setBenshu(int benshu) {
        this.benshu = benshu;
    }

    public String getTuzhang() {
        return tuzhang;
    }

    public void setTuzhang(String tuzhang) {
        this.tuzhang = tuzhang;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getXinfenleihao() {
        return xinfenleihao;
    }

    public void setXinfenleihao(String xinfenleihao) {
        this.xinfenleihao = xinfenleihao;
    }

    public String getZhaiyao() {
        return zhaiyao;
    }

    public void setZhaiyao(String beizhu) {
        this.zhaiyao = zhaiyao;
    }


    public List<String> getThumbnailList() { return thumbnailList; }

    public void setThumbnailList(List<String> thumbnailList) { this.thumbnailList = thumbnailList; }

    public List<String> getZhaopianbianhao() {
        return zhaopianbianhao;
    }

    public void setZhaopianbianhao(List<String> zhaopianbianhao) {
        this.zhaopianbianhao = zhaopianbianhao;
    }
}
