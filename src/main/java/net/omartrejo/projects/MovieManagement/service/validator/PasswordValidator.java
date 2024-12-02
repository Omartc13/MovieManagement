package net.omartrejo.projects.MovieManagement.service.validator;

import net.omartrejo.projects.MovieManagement.exception.InvalidPasswordException;
import org.springframework.util.StringUtils;

public class PasswordValidator {

    public static void validatePassword(String password, String passwordRepeated){

        if (!StringUtils.hasText(password) || !StringUtils.hasText(passwordRepeated)){
            throw new IllegalArgumentException("Password must contains data");
        }

        if (!password.equals(passwordRepeated)){
            throw new InvalidPasswordException(password,passwordRepeated,"Passwords do not match");
        }

        if (!containsNumber(password)){
            throw new InvalidPasswordException(password,"Passwords must contain at least one number");
        }

        if (!containsUpperCase(password)){
            throw new InvalidPasswordException(password,"Password must contains at least one Uppercase letter");
        }

        if (!containsLowerCase(password)){
            throw new InvalidPasswordException(password,"Password must contains at least one Lowercase letter");
        }

        if (!containsEspecialCharacters(password)){
            throw new InvalidPasswordException(password,"Password must contains at least one Especial character");
        }

    }

    private static boolean containsEspecialCharacters(String password) {
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*");
    }

    private static boolean containsLowerCase(String password) {
        return password.matches(".*[a-z].*");
    }

    private static boolean containsUpperCase(String password) {
        return password.matches(".*[A-Z].*");
    }

    private static boolean containsNumber(String password) {
        return password.matches(".*\\d.*");
    }

}
