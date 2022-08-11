//в классе используем паттерн Singlton - "Одиночка"

import java.io.IOException;
import java.util.HashMap;

//модель описывающая сущность - АРМ библиотекаря
public class Arm {
    //коды работы АРМ
    //global menu
    static final int KOD_GLOB_MENU=0;       // show главное меню АРМ
    static final int KOD_ENTER_MENU=1;      // вход в главное меню АРМ
    static final int KOD_EXIT_MENU=2;       // выход из главного меню АРМ
    static final int KOD_GLOB_MENU_EXIT=55; // выход из главного меню АРМ
    static final int KOD_SUBMENU_EXIT=555;  // выход из подменю
    //podmenu
    static final int KOD_WORK_FORMULYAR=1;   // работа с формуляром читателя
    static final int KOD_CONTROL_DOLG=2;     // контроль должников
    //podmenu: работа с формуляром читателя
    static final int KOD_SIGNUP=1;           // регистрация читателя
    static final int KOD_WRITE_BOOKS=2;      // запись книг в формуляр читателя
    static final int KOD_DELETE_BOOKS=3;     // удаление книг из формуляра
    static final int KOD_DELETE_FORMULAR=4;  // удаление формуляра читателя
    static final int KOD_SHOW_FORMULARS=5;   // просмотр всех формуляров
    static final int KOD_SHOW_BOOKS=6;       // просмотр книг читателя
    //podmenu: контроль должников
    static final int KOD_SEARCH_DATE=1;        // проверка по дате

    //двухсвязный список (login-password) библиотекарей для проверки входа
    private static HashMap<String, String> hmArmUsers=null;

    private Arm() {
    }

    //метод создания списка
    public static HashMap<String, String> getHmUsers(){
        if (hmArmUsers == null){
            hmArmUsers= new HashMap<>();
            //System.out.println("\nCreate hmArmUsers !!!");
        }
        return hmArmUsers;
    }

    //метод контроля регистрации библиотекарей
    public static boolean kntrRegistration(String log, String passwd, HashMap<String,String> hm) throws IOException {
        //System.out.println(log + passwd);
        if (! hm.isEmpty()) {
            //System.out.println(hm.containsKey(log) + " & " + hm.get(log));
            if (hm.containsKey(log)) {
                if (passwd.equals(hm.get(log))) {return true;}
            }
        }
        else {
            System.out.println("\nCписок пуст!");
        }
        return false;
    }

    //метод печати всех меню АРМ
    public static  void showMenu(int kod){
        switch (kod) {
            case KOD_GLOB_MENU: {
                System.out.println("\n"
                        + "********************************************\n"
                        + "*           Главное меню АРМ               *\n"
                        + "********************************************\n"
                        + "I    1-работа с формуляром пользователя    I\n"
                        + "I__________________________________________I\n"
                        + "I    2-контроль должников                  I\n"
                        + "I__________________________________________I\n"
                        + "I    55-выход из АРМ                       I\n"
                        + "I__________________________________________I\n");
                break;
            }
            case KOD_WORK_FORMULYAR: {
                System.out.println("\n"
                        + "\t***********************************************\n"
                        + "\t*     \"Работа с формуляром пользователя\"    *\n"
                        + "\t***********************************************\n"
                        + "\tI  1-регистрация пользователя                 I\n"
                        + "\tI_____________________________________________I\n"
                        + "\tI  2-запись книг в формуляр пользователя      I\n"
                        + "\tI_____________________________________________I\n"
                        + "\tI  3-удаление книг из формуляра               I\n"
                        + "\tI_____________________________________________I\n"
                        + "\tI  4-удаление формуляра пользователя          I\n"
                        + "\tI_____________________________________________I\n"
                        + "\tI  5- просмотр всех формуляров                I\n"
                        + "\tI_____________________________________________I\n"
                        + "\tI  6- просмотр книг читателя                  I\n"
                        + "\tI_____________________________________________I\n"
                        + "\tI  555-выход в главное меню                   I\n"
                        + "\tI_____________________________________________I\n");
                break;
            }
            case KOD_CONTROL_DOLG: {
                System.out.println("\n"
                        + "\t***********************************************\n"
                        + "\t*           \"Контроль должников\"            *\n"
                        + "\t***********************************************\n"
                        + "\tI  1-выборка по запрашиваемой дате            I\n"
                        + "\tI_____________________________________________I\n"
                        + "\tI  2-выборка по запрашиваемой книге           I\n"
                        + "\tI_____________________________________________I\n"
                        + "\tI  555-выход в главное меню                    I\n"
                        + "\tI_____________________________________________I\n");
                break;
            }

            default:break;
        }//sw
    }

}
