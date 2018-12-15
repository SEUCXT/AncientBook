package com.seu.architecture.model;



import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;

import javax.persistence.*;
import java.util.List;

/**
 * 特色文献
 */
@Entity
@Table(name = "characteristic_book")
@DiscriminatorValue("characteristic_book")
public class CharacteristicBook extends Book {


    @Indexed(type = "text_general")
    @Field(SearchableCharacteristicBook.LEIXING_FEILD_NAME)
    private String leixing;

    @Indexed(type = "text_general")
    @Field(SearchableCharacteristicBook.TUZAHNG_FIELD_NAME)
    private String tuzhang;

    @Indexed(type = "text_general")
    @Field(SearchableCharacteristicBook.BEIZHU_FIELD_NAME)
    private String beizhu;

    @Indexed(type = "text_general")
    @Field(SearchableCharacteristicBook.ZHAIYAO_FIELD_NAME)
    private String zhaiyao;


    @ElementCollection
    private List<String> thumbnailList;

    @ElementCollection
    private List<String> zhaopianbianhao;

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) { this.leixing = leixing; }

    public String getTuzhang() {
        return tuzhang;
    }

    public void setTuzhang(String tuzhang) {
        this.tuzhang = tuzhang;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) { this.beizhu = beizhu; }

    public String getZhaiyao() {
        return zhaiyao;
    }

    public void setZhaiyao(String zhaiyao) { this.zhaiyao = zhaiyao; }


    public List<String> getThumbnailList() { return thumbnailList; }

    public void setThumbnailList(List<String> thumbnailList) { this.thumbnailList = thumbnailList; }

    public List<String> getZhaopianbianhao() {
        return zhaopianbianhao;
    }

    public void setZhaopianbianhao(List<String> zhaopianbianhao) {
        this.zhaopianbianhao = zhaopianbianhao;
    }

}
