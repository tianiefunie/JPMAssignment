package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
public class ShowEntity {
        @Id
        @Column
        private Integer showNumber;
        @Column
        private Integer numRows;
        @Column
        private Integer numSeatsPerRow;
        @Column
        private Integer cancellationWindowInMinutes;

}
