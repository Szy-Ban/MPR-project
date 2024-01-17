package com.example.monday.data;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    public Student(String name, StudentUnit unit, Kierunek kierunek) {
        this.name = name;
        this.unit = unit;
        this.kierunek = kierunek;
    }
    public Student(String name, StudentUnit unit, Long index, Kierunek kierunek ) {
        this.name = name;
        this.unit = unit;
        this.index = index;
        this.kierunek = kierunek;
    }

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private StudentUnit unit;
    @Enumerated(EnumType.STRING)
    private Kierunek kierunek;
    @Setter
    private Long index;

}
