package com.example.monday.service;

import com.example.monday.data.*;
import com.example.monday.resource.CreateStudent;
import com.example.monday.resource.StudentDto;
import com.example.monday.resource.StudentMapper;
import com.example.monday.resource.StudentResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//Rozszerzamy junit rozszerzeniem z mockito, aby móc w junitowych testach korzystać z funkcji biblioteki mockito
@ExtendWith(MockitoExtension.class)
class MockStudentServiceTest {

    //Mock tworzy nam proxy naszej klasy - to sprawia, że wywołania tej klasy nie wykonają rzeczywistej metody
    //i każdorazowe jej wywołanie musimy skonfigurować, możemy też tak jak w przypadku Spy śledzić jej wywołania

    @Mock
    private StudentRepository studentRepository = mock(StudentRepository.class);

    @Mock
    private StudentMapper studentMapper = new StudentMapper();



    //InjectMocks pozwala nam stworzyć klasę testowaną z wykorzystaniem obiektów, które zdefiniowaliśmy
    //jako elementy mockito używając adnotacji Mock/Spy
    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, studentMapper);
    }

    @AfterEach
    void cleanup() {
        studentRepository.deleteAll();
    }

    @Test
    void givenKierunekWhenGetStudentsThenReturnListOfStudents() {

        Kierunek kierunek = Kierunek.Informatyka;
        Student student = new Student("Szymon", StudentUnit.GDANSK, 5L, kierunek, 0L);
        when(studentRepository.getStudentsByKierunek(kierunek)).thenReturn(Arrays.asList(student));

        List<StudentDto> result = studentService.getStudentsByKierunek(kierunek);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void givenEctsWhenGetStudentsThenReturnListOfStudents() {
        Long ects = 5L;
        Student student = new Student("Szymon", StudentUnit.GDANSK, ects, Kierunek.Informatyka, 0L);
        when(studentRepository.getStudentsByEcts(ects)).thenReturn(Arrays.asList(student));

        List<StudentDto> result = studentService.getStudentsByEcts(ects);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void givenStudentDtoWithInvalidIndexWhenUpdateStudentThenThrowException() {

        Long index = 5L;
        StudentDto studentDto = new StudentDto(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"),"Szymon", StudentUnit.GDANSK, Kierunek.Informatyka, 0L, index);

        //symulowanie sytuacji gdyby user nie istnial
        when(studentRepository.getStudentByIndex(index)).thenReturn(null);

        // sprawdzanie czy rzuci wyjątek
        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.updateStudent(studentDto);
        });

        String expectedMessage = "Student not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIndexWhenGetStudentThenReturnStudentDto() {
        Long index = 5L;
        Student student = new Student("Szymon", StudentUnit.GDANSK, index, Kierunek.Informatyka, 0L);
        StudentDto studentDto = new StudentDto(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"),"Szymon", StudentUnit.GDANSK, Kierunek.Informatyka, 0L, index);
        when(studentRepository.getStudentByIndex(index)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.getStudentByIndex(index);

        assertNotNull(result);
        assertEquals(studentDto, result);
    }

    @Test
    void whenGetAllThenReturnListOfAllStudents() {
        Student student1 = new Student("Szymon", StudentUnit.GDANSK, 5L, Kierunek.Informatyka, 0L);
        Student student2 = new Student("Zygfryd", StudentUnit.WARSZAWA, 6L, Kierunek.Grafika, 0L);
        StudentDto studentDto1 = new StudentDto(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"),"Szymon", StudentUnit.GDANSK, Kierunek.Informatyka, 0L, 5L);
        StudentDto studentDto2 = new StudentDto(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a2567"),"Zygfryd", StudentUnit.WARSZAWA, Kierunek.Grafika, 0L, 6L);

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));
        when(studentMapper.toDto(student1)).thenReturn(studentDto1);
        when(studentMapper.toDto(student2)).thenReturn(studentDto2);

        List<StudentDto> result = studentService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(studentDto1, studentDto2)));
    }

}


