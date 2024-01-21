package com.example.monday.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class JpaRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
        // uzupełniamy dane w bazie
    void setUp() {
        var student1 = new Student("Szymon", StudentUnit.GDANSK, 5L, Kierunek.Informatyka, 0L);
        var student2 = new Student("Zygfryd", StudentUnit.WARSZAWA, 15L, Kierunek.Grafika, 4L);
        studentRepository.save(student1);
        studentRepository.save(student2);
    }

    @Test
    void givenStudents_whenGetMaxIndex_ThenReturnValidResult() {
        var maxIndex = studentRepository.getMaxIndex();
        assertTrue(maxIndex.isPresent());
        assertEquals(15L, maxIndex.get());
    }

    @Test
    void givenEctsNumber_areStudentsInList(){
        var ectsStudentList = studentRepository.getStudentsByEcts(4L);
        assertTrue(ectsStudentList.contains(studentRepository.getStudentByIndex(15L)));
    }

}

