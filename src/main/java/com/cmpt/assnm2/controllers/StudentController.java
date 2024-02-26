package com.cmpt.assnm2.controllers;
import com.cmpt.assnm2.models.Student;
import com.cmpt.assnm2.models.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

@Controller
public class StudentController 
{
    @Autowired
    private StudentRepository studentRepo;
    //redirecting to the main fornt website
    @GetMapping("/students/view")
    public String getAllUsers(Model model) 
    {
        // get all students from the database
        List<Student> students = studentRepo.findAll();
        // end of call
        model.addAttribute("stu", students);
        return "students/studentMain";
    }


    // this post mapping should add user to the 
    @PostMapping("/students/add")
    public String addStudent(@RequestParam Map<String, String> newStudent, HttpServletResponse response) {
        System.out.println("ADD Student");

        String newName = newStudent.get("name");
        double newWeight = Double.parseDouble(newStudent.get("weight"));
        double newHeight = Double.parseDouble(newStudent.get("height"));
        String newHairColor = newStudent.get("hairColor");
        double newGpa = Double.parseDouble(newStudent.get("gpa"));

        studentRepo.save(new Student(newName, newWeight, newHeight, newHairColor, newGpa));
        response.setStatus(201);
        return "redirect:/students/view";
    }

    // this is the controller for editing the student entry.
    // @GetMapping("/students/edit/{studentId}")
    // public String editStudentPage(@PathVariable(value = "studentId") int studentId, Model model) {
    //     try {
    //         System.out.println("EDIT student uid:");
    //         System.out.println(studentId);
    //         Student student = studentRepo.findById(studentId).orElseThrow(() -> new Exception("null"));
    
    //         // Add the student object to the model
    //         model.addAttribute("student", student);
    
    //         // Return the Thymeleaf template name (without the ".html" extension)
    //         return "edit";
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         // Handle exception appropriately, e.g., return an error template
    //         return "error";
    //     }
    // }

    
        //this is for removing a student from the database. 

        @PostMapping("/students/remove/{uid}")
        public String removeStudent(@PathVariable(value = "uid") int uid, HttpServletResponse response) {
            System.out.println("REMOVE user uid:");
            System.out.println(uid);
            Optional<Student> userRecord = studentRepo.findById(uid);

            if (userRecord.isPresent()) {
                System.out.println("[REMOVE]Success");
                studentRepo.deleteById(uid);
                response.setStatus(201);
            } else {
                System.out.println("[REMOVE]Record not found");
            }
            return "redirect:/students/view";
        }

    // Get request to redirect to the edit page
    @GetMapping("/students/edit/{uid}")
    public String gotoEditStudent(
            @PathVariable(value = "uid") int uid,
            Model model,
            HttpServletResponse response) {
        try {
            Student student = studentRepo.findById(uid).orElseThrow(() -> (new Exception("null")));
            model.addAttribute("stu", student);
            model.addAttribute("stuId", uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "students/edit";
    }
    //this
    @PostMapping("/students/edit/{uid}")
    public String editStudent(
            @PathVariable int uid,
            @RequestParam Map<String, String> newStudent,
            HttpServletResponse response,
            Model model) {
        try {
            System.out.println("EDIT student uid:");
            System.out.println(uid);
            String newName = newStudent.get("newName");
            double newWeight = Double.parseDouble(newStudent.get("newWeight"));
            double newHeight = Double.parseDouble(newStudent.get("newHeight"));
            String newHairColor = newStudent.get("newHairColor");
            double newGpa = Double.parseDouble(newStudent.get("newGpa"));
            Student student = studentRepo.findById(uid).orElseThrow(() -> new Exception("Student not found"));
            student.setName(newName);
            student.setWeight(newWeight);
            student.setHeight(newHeight);
            student.setHairColor(newHairColor);
            student.setGpa(newGpa);
            studentRepo.save(student);
    
            // Fetch the updated list of students
            List<Student> students = studentRepo.findAll();
            model.addAttribute("students", students);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Redirect to the view page after editing
        return "redirect:/students/view";
    }

}