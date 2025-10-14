package com.moger.crudproject.controller;

import com.moger.crudproject.entity.Student;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class StudentController_mvc {

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);

    }
    @GetMapping("/new")
    public String showForm (Model theModel) {

        theModel.addAttribute("student", new Student());
        return "student-form";
    }

    @PostMapping("/processForm")
    public String processForm (@Valid @ModelAttribute("student") Student student, BindingResult result) {

        if (result.hasErrors()) {
            return  "student-form";
        }else {
            return "student-confirmation";
        }
    }
}
