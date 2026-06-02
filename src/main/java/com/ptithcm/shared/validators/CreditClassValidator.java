package com.ptithcm.shared.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ptithcm.entities.LopTinChi;

@Component
public class CreditClassValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LopTinChi.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LopTinChi ltc = (LopTinChi) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nienKhoa", "NotEmpty.lopTinChi.nienKhoa",
                "Niên khóa không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maMH", "NotEmpty.lopTinChi.maMH",
                "Mã môn học không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maKhoa", "NotEmpty.lopTinChi.maKhoa",
                "Mã khoa không được để trống!");

        if (ltc.getHocKy() <= 0) {
            errors.rejectValue("hocKy", "Min.lopTinChi.hocKy", "Học kỳ phải lớn hơn 0!");
        }

        if (ltc.getNhom() <= 0) {
            errors.rejectValue("nhom", "Min.lopTinChi.nhom", "Nhóm phải lớn hơn 0!");
        }

        if (ltc.getSoSVToiThieu() <= 0) {
            errors.rejectValue("soSVToiThieu", "Min.lopTinChi.soSVToiThieu", "Số sinh viên tối thiểu phải lớn hơn 0!");
        }
    }
}
