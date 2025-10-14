package com.moger.crudproject.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StudentMajorConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StudentMajor {

    //define default major code
    public String value() default "stu_";

    //define default error message
    public String message() default "must start with stu_";

    //define default groups
    public Class<?>[] groups() default {};

    //define default payloads
    public Class<? extends Payload>[] payload() default {};
}
