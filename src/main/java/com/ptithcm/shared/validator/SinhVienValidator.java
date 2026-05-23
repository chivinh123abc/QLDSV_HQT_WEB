package com.ptithcm.shared.validator;

import com.ptithcm.entity.SinhVien;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SinhVienValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SinhVien.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SinhVien sv = (SinhVien) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maSV", "NotEmpty.sinhVien.maSV",
                "Mã sinh viên không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ho", "NotEmpty.sinhVien.ho",
                "Họ sinh viên không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ten", "NotEmpty.sinhVien.ten",
                "Tên sinh viên không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maLop", "NotEmpty.sinhVien.maLop",
                "Mã lớp không được để trống!");

        if (sv.getMaSV() != null && sv.getMaSV().trim().length() > 10) {
            errors.rejectValue("maSV", "Length.sinhVien.maSV", "Mã sinh viên không được dài quá 10 ký tự!");
        }
    }
}
