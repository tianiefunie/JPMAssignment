package com.example.demo.dto;

import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
@RequiredArgsConstructor
public class Show {
        @Id
        @Column
        private Long id;
        @Column
        private String name;

        // standard constructors

        // standard getters and setters
    }

}
