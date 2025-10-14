package com.moger.crudproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StudentMajorConstraintValidator implements ConstraintValidator<StudentMajor, String> {

    private String majorPrefix;

    @Override
    public void initialize(StudentMajor theStudentMajor) {
        majorPrefix = theStudentMajor.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        boolean result;
        if (s != null) {
            result = s.startsWith(majorPrefix);
        } else {
            result = true;
        }
        return result;
    }
}
