package com.ptithcm.shared.validator;

import com.ptithcm.entity.Users;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UsersValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Users.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Users user = (Users) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.users.username",
                "Tên đăng nhập không được để trống!");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.users.password",
                "Mật khẩu không được để trống!");

        if (user.getRoleId() <= 0) {
            errors.rejectValue("roleId", "Min.users.roleId", "Quyền tài khoản không hợp lệ!");
        }
    }
}
