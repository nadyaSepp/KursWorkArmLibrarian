//Курсовая работа №1
//Группа JAVA_112
//Разработчик: Сеппар Надежда
//Тема: АРМ сотрудников библиотеки
//09.07.22

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class KursWork1 {
    public  static void main(String[] args) throws IOException {

        int     kodMenu=0, kodSubMenu=0;
        boolean flagMenu=false, flagUser=false, flagSubMenu=false;
        boolean res=false, resCreateFileUsers=false,resCreateFileForms=false;
        String  str_error="Cистемная ошибка! Обратитесь к Администратору!";

        //создаем контроллер с сервисными функциями
        Controller contr=new Controller();

        //создаем файлы хранения инф-ии (armUsers.txt, formulyars.txt)
        contr.initFile(contr.nameFileUsers,contr.KOD_HM_USERS);
        contr.initFile(contr.nameFileFormulyars,contr.KOD_HM_FORMULARS);

        //создаем рабочий двухсвязный список 'hmArmUsers' (login-password) пользователей АРМ (библиотекарей) для проверки входа
        HashMap<String, String> hmArmUsers;
        hmArmUsers=Arm.getHmUsers();
        //наполняем его из файла (armUsers.txt) данными
        resCreateFileUsers=contr.setList(contr.getDataFromFile(contr.nameFileUsers),contr.KOD_HM_USERS, hmArmUsers);

        //создаем рабочий двухсвязный список 'hmFormulars'(id,string) пользователей (каждая строка-данные одного пользователя через запятую)
        HashMap<String, String> hmFormReaders;
        hmFormReaders = FormulyarReader.getFormulars();
        //наполняем его из файла (formReaders.txt) данными
        resCreateFileForms=contr.setList(contr.getDataFromFile(contr.nameFileFormulyars),contr.KOD_HM_FORMULARS,hmFormReaders);

        //создаем рабочий двухсвязный список читателя 'hmBooks'(id-строка вида:дата,автор,книга)
        HashMap<String, String> hmBooks;
        hmBooks= FormulyarBook.getHmBooks();

        Scanner Sc = new Scanner(System.in);
        //System.out.println("Users=" + resCreateFileUsers + " " + "F=" + resCreateFileForms);
        while(!flagUser){

            if (! resCreateFileUsers || ! resCreateFileForms) {
                System.out.println("\n" + str_error);
                break;
            }
            //проверка входа сотрудника библиотеки
            System.out.println("\n\n ****    АРМ сотрудника библиотеки   ****");
            kodMenu=0;
            System.out.println("\n1 - вход в АРМ\n"
                    + "2 - выход из АРМ");
            kodMenu = Sc.nextInt();
            if (kodMenu == Arm.KOD_EXIT_MENU) {flagMenu=false;break;}
            if (kodMenu == Arm.KOD_ENTER_MENU) {
                System.out.println("Введите логин:");
                String login = Sc.next();
                System.out.println("Введите пароль:");
                String passwd = Sc.next();
                if (Arm.kntrRegistration(login, passwd, hmArmUsers)) {
                    System.out.println("Регистрация успешна!");
                    flagUser = true;
                    flagMenu = true;
                } else {
                    System.out.println("Извините,Вы не зарегистрированы!\n"
                            + "Для внесения Вас в Базу данных вызовите Администратора!");
                }
            }
            else {System.out.println("Неверный код!");}
        }

        //собственно циклическая часть  программы для основной работы сотрудника библиотеки
        while(flagMenu) {
            Arm.showMenu(Arm.KOD_GLOB_MENU);
            flagSubMenu=true;
            System.out.println("\nВведите код меню: \n");
            kodMenu = Sc.nextInt();
            //System.out.printf("kodMenu=%d\n",kodMenu);

            switch (kodMenu){
                case Arm.KOD_WORK_FORMULYAR:{
                    while(flagSubMenu) {
                        res=false;
                        Arm.showMenu(Arm.KOD_WORK_FORMULYAR);
                        System.out.println("\nВведите код подменю: \n");
                        kodSubMenu = Sc.nextInt();
                        switch (kodSubMenu){
                            case Arm.KOD_SHOW_FORMULARS:  {
                                contr.showFormulars(hmFormReaders);
                                res=true; break;
                            }
                            case Arm.KOD_SIGNUP:  {
                                res=contr.signUpFormular(hmFormReaders);
                                if (res){System.out.println("Регистрация пользователя успешна!");}
                                break;
                            }
                            case Arm.KOD_WRITE_BOOKS: {
                                res=contr.writeBooksReader(hmFormReaders);
                                if (res) System.out.println("\nКниги успешно записаны!");
                                break;
                            }
                            case Arm.KOD_SHOW_BOOKS: {
                                res=contr.showBooksReader(hmFormReaders,hmBooks);
                                if (!res) System.out.println("Нет данных пользователя!");
                                break;
                            }
                            case Arm.KOD_DELETE_BOOKS:  {
                                res=contr.deleteBooks(hmFormReaders);
                                if (res){System.out.println("Данные удалены!");}
                                else{res=true;System.out.println("Нет данных !"); }
                                break;
                            }
                            case Arm.KOD_DELETE_FORMULAR: {
                                res=contr.deleteFormularReader(hmFormReaders);
                                if (res){System.out.println("Данные пользователя удалены!");}
                                else{res=true;System.out.println("Нет данных пользователя!"); }
                                break;
                            }
                            case Arm.KOD_SUBMENU_EXIT:    {flagSubMenu=false; res=true; break;}
                            default: {res=true; System.out.println("\nНе верный код: " + kodSubMenu);}
                        }//sw
                    }//
                    if (! res) System.out.println("\n" + str_error);
                    break;
                }
                case Arm.KOD_GLOB_MENU_EXIT: {flagMenu=false; break;}
                default: { System.out.printf("\nНе верный код меню: %d \n",kodMenu); break;}
            }
            if (flagMenu == false) {
                //сохранить список hm в файл "Формуляры" (параметр - false)
                StringBuilder strR = contr.getStrHm(hmFormReaders);
                contr.writeFile(contr.nameFileFormulyars, strR.toString(), false);
                System.out.println("Выход из АРМ!");
            }
        }//wh
    }//m

}
