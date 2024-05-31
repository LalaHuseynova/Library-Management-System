package lang;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;



public class Messages {
    public enum Message {
        // Keys from Login class
        USER_NOT_FOUND("User not found", "İstifadəçi tapılmadı", "Пользователь не найден"),
        LOGIN("Login", "Giriş", ""),
        USERNAME_LABEL("Username", "İstifadəçi adı", "Имя пользователя"),
        PASSWORD_LABEL("Password", "Şifrə", "Пароль"),
        GO_TO_LIBRARY_BUTTON("Go to Library", "Kitabxanaya daxil ol", "Перейти в библиотеку"),
        REDIRECTING_TO_ADMIN_PANEL("Redirecting to admin panel...", "", ""),
        SUCCESS("Success", "Uğur", ""),
        REDIRECTING_TO_LIBRARY("Redirecting to library...", "", ""),
        INVALID_USERNAME_PASSWORD("Invalid username or password. Do you want to sign up?", "Yanlış istifadəçi adı və ya şifrə. Qeydiyyatdan keçmək istəyirsiniz?", "Неверное имя пользователя или пароль. Хотите зарегистрироваться?"),
        ERROR("Error", "Səhv", ""),
        DONT_HAVE_AN_ACCOUNT("I don't have an account", "Mən hesabım yoxdur", "У меня нет аккаунта"),
        SIGN_UP("Sign Up", "Qeydiyyat", ""),
        BOOK_STACK("Book Stack", "", ""),

        // Keys from SignUp class
        NEW_USERNAME_LABEL("New Username:", "Yeni istifadəçi adı:", "Новое имя пользователя:"),
        NEW_PASSWORD_LABEL("New Password:", "Yeni şifrə:", "Новый пароль:"),
        REGISTER_BUTTON("Register", "Qeydiyyat", "Зарегистрироваться"),
        BACK_TO_LOGIN_BUTTON("Back to the Login", "Girişə qayıt", "Вернуться к входу"),
        USERNAME_ALREADY_TAKEN("Username already taken. Please choose another one.", "İstifadəçi adı artıq mövcuddur. Xahiş edirik başqa bir ad seçin.", "Имя пользователя уже занято. Пожалуйста, выберите другое."),
        PASSWORD_LENGTH_ERROR("Password must be at least 4 characters long.", "Şifrə ən azı 4 simvol olmalıdır.", "Пароль должен содержать не менее 4 символов."),
        REGISTRATION_SUCCESS("Registration successful! Can go to Login", "Qeydiyyat uğurla başa çatdı! Giriş edə bilərsiniz", "Регистрация успешна! Можете перейти к входу"),
        USERNAME_ALREADY_EXISTS("Username already exists. Please choose another one.", "İstifadəçi adı artıq mövcuddur. Xahiş edirik başqa bir ad seçin.", "Имя пользователя уже существует. Пожалуйста, выберите другое.");





        ;

        private final Map<Language, String> translations = new HashMap<>();

        Message(String eng, String aze, String ru) {
            translations.put(Language.ENGLISH, eng);
            translations.put(Language.AZERBAIJANI, aze);
            translations.put(Language.RUSSIAN, ru);
        }

        public String getTranslation(Language lang) {
            return translations.get(lang);
        }
    }

    public static String getMessage(Message message, Language lang) {
        return message.getTranslation(lang);
    }

    public static void main(String[] args) {

      //  JFrame frame = new JFrame();

        Language lang = Language.AZERBAIJANI;
        Message message = Message.LOGIN;
        System.out.println(getMessage(message, lang));


    }
}