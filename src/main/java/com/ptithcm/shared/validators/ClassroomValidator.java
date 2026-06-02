package com.ptithcm.shared.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ptithcm.entities.Lop;

@Component
public class ClassroomValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Lop.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Lop lop = (Lop) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maLop", "NotEmpty.lop.maLop", "Mã lớp không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tenLop", "NotEmpty.lop.tenLop",
                "Tên lớp không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maKhoa", "NotEmpty.lop.maKhoa",
                "Mã khoa không được để trống!");

        if (lop.getMaLop() != null && lop.getMaLop().trim().length() > 10) {
            errors.rejectValue("maLop", "Length.lop.maLop", "Mã lớp không được dài quá 10 ký tự!");
        }
    }
}
