package com.ptithcm.shared.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ptithcm.entities.MonHoc;

@Component
public class SubjectValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MonHoc.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MonHoc mh = (MonHoc) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maMH", "NotEmpty.monHoc.maMH",
                "Mã môn học không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tenMH", "NotEmpty.monHoc.tenMH",
                "Tên môn học không được để trống!");

        if (mh.getMaMH() != null && mh.getMaMH().trim().length() > 10) {
            errors.rejectValue("maMH", "Length.monHoc.maMH", "Mã môn học không được dài quá 10 ký tự!");
        }

        if (mh.getSoTietLT() < 0) {
            errors.rejectValue("soTietLT", "Min.monHoc.soTietLT", "Số tiết lý thuyết phải lớn hơn hoặc bằng 0!");
        }

        if (mh.getSoTietTH() < 0) {
            errors.rejectValue("soTietTH", "Min.monHoc.soTietTH", "Số tiết thực hành phải lớn hơn hoặc bằng 0!");
        }
    }
}
