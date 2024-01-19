package com.example.monday.data;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    public Student(String name, StudentUnit unit, Kierunek kierunek, Long ects) {
        this.name = name;
        this.unit = unit;
        this.kierunek = kierunek;
        this.ects = ects;
    }
    public Student(String name, StudentUnit unit, Long index, Kierunek kierunek, Long ects) {
        this.name = name;
        this.unit = unit;
        this.index = index;
        this.kierunek = kierunek;
        this.ects = ects;
    }

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private Long ects;
    @Enumerated(EnumType.STRING)
    private StudentUnit unit;
    @Enumerated(EnumType.STRING)
    private Kierunek kierunek;
    @Setter
    private Long index;

}
