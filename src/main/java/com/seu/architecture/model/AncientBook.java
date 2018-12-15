package com.seu.architecture.model;


import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.*;
import java.util.List;

/**
 * 馆藏古籍
 */
@Entity
@Table(name = "ancient_book")
@SolrDocument(solrCoreName = "ancient_book")
@DiscriminatorValue("ancient_book")
public class AncientBook extends Book {

    // 卷/册数
    @Indexed
    @Field(SearchableAncientBook.JUANSHU_FIELD_NAME)
    private int juanshu;

    // 类别
    @Indexed
    @Field(SearchableAncientBook.LEIBIE_FIELD_NAME)
    private String leibie;

    // 所属丛书
    @Indexed
    @Field(SearchableAncientBook.SUOSHUCONGSHU_FIELD_NAME)
    private String suoshucongshu;

    // 藏书版本
    @Indexed
    @Field(SearchableAncientBook.CANGSHUBANBEN_FIELD_NAME)
    private String cangshubanben;

    // 类型
    @Indexed
    @Field(SearchableAncientBook.LEIXING_FIELD_NAME)
    private String leixing;

    // 成书时间/初版时间
    @Indexed
    @Field(SearchableAncientBook.CHENGSHUSHIJIAN_FIELD_NAME)
    private String chengshushijian;

    // 出版地点
    @Indexed
    @Field(SearchableAncientBook.CHUBANDIDIAN_FIELD_NAME)
    private String chubandidian;

    // 订、译者
    @Indexed
    @Field(SearchableAncientBook.DINGZHE_FIELD_NAME)
    private String dingzhe;


    // 分类号
    @Indexed
    @Field(SearchableAncientBook.FENLEIHAO_FIELD_NAME)
    private String fenleihao;

    // 登录号
    @Indexed
    @Field(SearchableAncientBook.DENGLUHAO_FIELD_NAME)
    private String dengluhao;

    // 录入时间
    @Indexed
    @Field(SearchableAncientBook.LURUSHIJIAN_FIELD_NAME)
    private String lurushiijan;

    // 提要
    @Indexed
    @Field(SearchableAncientBook.TIYAO_FIELD_NAME)
    private String tiyao;

    // 备注
    @Indexed
    @Field(SearchableAncientBook.BEIZHU_FIELD_NAME)
    private String beizhu;

    @ElementCollection
    private List<String> cangshuxinxi;

    @ElementCollection
    private List<String> thumbnailList;

    @ElementCollection
    private List<String> zhaopianbianhao;

    public int getJuanshu() {
        return juanshu;
    }

    public void setJuanshu(int juanshu) {
        this.juanshu = juanshu;
    }

    public String getChengshushijian() {
        return chengshushijian;
    }

    public void setChengshushijian(String chengshushijian) {
        this.chengshushijian = chengshushijian;
    }

    public String getLeibie() {
        return leibie;
    }

    public void setLeibie(String leibie) {
        this.leibie = leibie;
    }

    public String getCangshubanben() {
        return cangshubanben;
    }

    public void setCangshubanben(String cangshubanben) {
        this.cangshubanben = cangshubanben;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }

    public String getSuoshucongshu() {
        return suoshucongshu;
    }

    public void setSuoshucongshu(String suoshucongshu) {
        this.suoshucongshu = suoshucongshu;
    }

    public String getChubandidian() {
        return chubandidian;
    }

    public void setChubandidian(String chubandidian) {
        this.chubandidian = chubandidian;
    }

    public String getDingzhe() {
        return dingzhe;
    }

    public void setDingzhe(String dingzhe) {
        this.dingzhe = dingzhe;
    }

    public String getFenleihao() {
        return fenleihao;
    }

    public void setFenleihao(String fenleihao) {
        this.fenleihao = fenleihao;
    }

    public String getDengluhao() {
        return dengluhao;
    }

    public void setDengluhao(String dengluhao) {
        this.dengluhao = dengluhao;
    }

    public String getLurushiijan() {
        return lurushiijan;
    }

    public void setLurushiijan(String lurushiijan) {
        this.lurushiijan = lurushiijan;
    }

    public String getTiyao() {
        return tiyao;
    }

    public void setTiyao(String tiyao) {
        this.tiyao = tiyao;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public List<String> getCangshuxinxi() {
        return cangshuxinxi;
    }

    public void setCangshuxinxi(List<String> cangshuxinxi) {
        this.cangshuxinxi = cangshuxinxi;
    }


    public List<String> getThumbnailList() {
        return thumbnailList;
    }

    public void setThumbnailList(List<String> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }

    public List<String> getZhaopianbianhao() {
        return zhaopianbianhao;
    }

    public void setZhaopianbianhao(List<String> zhaopianbianhao) {
        this.zhaopianbianhao = zhaopianbianhao;
    }
}
