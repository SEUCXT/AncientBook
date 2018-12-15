package com.seu.architecture.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 17858 on 2018-12-05.
 */

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    Long id;

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "gender", nullable = false)
    String gender;

    @Column(name = "age", nullable = false)
    int age;

    @Column(name = "career", nullable = false)
    String career;

    @Column(name = "phone", unique = true, nullable = false)
    String phone;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
