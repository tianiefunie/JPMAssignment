package com.example.demo.dto;


import javax.persistence.*;



@Entity
public class Country {
    @Id
    @Column
    private Integer id;

    @Column
    private String name;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    //...
}