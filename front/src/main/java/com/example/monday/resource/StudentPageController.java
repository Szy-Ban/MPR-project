package com.example.monday.resource;

import com.example.monday.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller //Użycie tej adnotacji definiuje Springowego beana, od @RestController, którego używaliśmy do tej pory różni się tym,
//że dedytkowany jest do udostępniania stron w formacie html, a nie w postacji json jak to ma miejsce w przypadku restowego kontrolera
//jest to też element Controller z wzorca MVC (model-view-controller)
@RequestMapping("/students-page")
@RequiredArgsConstructor
public class StudentPageController {

//    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @GetMapping//korzystamy z adnotacji znanych nam z restowych kontrollerów, żeby określić metodę (opcjonalnie też ścieżkę do metody)
    public String returnStudentsPage(Model model) { // jeśli chcemy operować danymi na widoku to w parametrze zawsze musimy pobrać Model - jest to model ze wzorca MVC
        //model.addAttribute("name", name); //dodanie do modelu atrybutu o konkretnej nazwie pozwoli nam następnie na widoku pobrać jego wartość po nadanej tu nazwie
        var students = studentService.getAll();
        model.addAttribute("students", students);
        return "index"; //z metod zawsze zwracamy Stringa i jako wartość wstawiamy nazwę szablonu thymeleafowego (pliku html)
    }

    @GetMapping("/add") // ustawiamy ścieżkę, do zwrócenia stron zawsze używamy metody GET, jest to standardowa metoda
    //jakiej używają przeglądarki do pobrania strony
    public String displayAddStudentPage(Model model) {
        model.addAttribute("student", new CreateStudent()); // dodajemy pusty obiekt do formularza tak, aby użytkownik mógł do niego wprowadzić wartości
        //jeśli byśmy tego nie zrobili otrzymalibyśmy błąd, ponieważ nie możemy korzystać w javie (a co za tym idzie w thymeleaf) z niezainicjalizowanych obiektów
        //jeśli obiekt ten miałby ustawioną jakąś wartość byłaby ona od razu wprowadzona na wyświetlonym formularzu
        return "addStudent";
    }

    @PostMapping// tu pojawia się post, gdyż jest to akcja wywoływana z jednego z szablonów,
    //a nie na podstawie adresu z przeglądarki
    public String saveStudent(@ModelAttribute CreateStudent createStudent) {
        studentService.saveStudent(createStudent);
        return "redirect:/students-page";//jako, że nie wchodzimy to bezpośrednio na stronę, a akcja dzieje się po kliknięciu przycisku
        //do nazwy szablonu musimy dodać 'redirect:{ścieżka do strony}' aby zostać przeniesionym po kliknięciu przycisku na inną stronę
        //robimy to tylko w przypadku gdy jest to przeniesienie na podstawie akcji użytkownika, jeśli udostępniamy stronę na podstawie adresu w przeglądarce podajemy tylko nazwę szablonu
    }


//    @GetMapping("/byKierunek") // ustawiamy ścieżkę, do zwrócenia stron zawsze używamy metody GET, jest to standardowa metoda
//    //jakiej używają przeglądarki do pobrania strony
//    public String getStudentsByKierunek(Model model) {
//        model.addAttribute("kierunek", null);
//        return "studentsByKierunek";
//    }
//    @PostMapping("/getStudentsByKierunek")
//    public String returnStudentsByKierunek(Model model, @ModelAttribute Kierunek kierunek) {
//        var students = studentService.getStudentsByKierunek(kierunek);
//        model.addAttribute("students", students);
//        model.addAttribute("kierunek", kierunek);
//        return "studentsByKierunek";
//    }

    @GetMapping("/byKierunek")
    public String getStudentsByKierunek(Model model){
        model.addAttribute("studenciKierunekData", new StudentDtoKierunek());
        return "byKierunek";
    }

    @PostMapping("/byKierunek")
    public String getStudentsByKierunekSecond(Model model, @ModelAttribute StudentDto studentDto){
        var listaStudentow = studentService.getStudentsByKierunek(studentDto.kierunek());
        model.addAttribute("studenciKierunek", listaStudentow);
        return "byKierunek";
    }

    @GetMapping("/byEcts")
    public String getStudentsByEcts(Model model){
        model.addAttribute("studenciEctsData", new StudentDtoEcts());
        return "byEcts";
    }

    @PostMapping("/byEcts")
    public String getStudentsByEctsSecond(Model model, @ModelAttribute StudentDto studentDto){
        var listaStudentow = studentService.getStudentsByECTS(studentDto.ects());
        model.addAttribute("studenciEcts", listaStudentow);
        return "byEcts";
    }

    //todo edycja poniżej - coś się nie zgrywa w momencie klikania zaktualizowania danych przy zmianie danych
    //todo wyskakuje error ktory nie wiem jak naprawić
//    @PostMapping("/updateStudent")
//    public String updateStudent(@ModelAttribute("studentData") StudentDto studentDto) {
//        studentService.updateStudent(studentDto);
//        return "redirect:/students-page"; // Przekierowanie do listy studentów
//    }

    @GetMapping("/searchStudent")
    public String showEditStudentForm(Model model) {
        model.addAttribute("studentSearchData", new StudentDtoNotEmpty()); // Pusty obiekt DTO do wyszukiwania
        return "searchStudent";
    }

    @GetMapping("/editStudent")
    public String editStudentForm(Model model, @RequestParam Long index) {
        StudentDto student = studentService.getStudentByIndex(index);
        if(student == null) {
            return "student not found";
        }
        model.addAttribute("studentData", student);
        return "editStudent";
    }

    @PostMapping("/editStudent")
    public String editStudentFormSecond(@ModelAttribute("studentData") StudentDto studentDto) {
        studentService.updateStudent(studentDto);
        return "redirect:/students-page";
    }


}
