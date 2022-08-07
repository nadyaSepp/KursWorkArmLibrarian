import java.util.*;

//Класс "Формуляр"
//Формуляр пользователя должен содержать данные:
//        -Имя;
//        -Фамилия;
//        -год рождения;
//        -домашний адрес;
//        -место работы;
//        -телефон;
//        -учетный номер пользователя;
//        -строка-Код пользователя.

public class FormulyarReader {
    String name;
    String fio;
    String birthDate;
    String adress;
    String work;
    String telepfone;
    String id;           //номер (случайное число)
    String strKodReader; //строка "ИмяФамилияНомер" - код читателя"

    //двухсвязный список ( КЛЮЧ(код читателя), СТРОКА(дата,автор,наименование книги)
    private static HashMap<String, String> hmFormReaders = null;

    FormulyarReader() {
    }

    @Override
    public String toString() {
        return  id+" "+
                name + " " +
                fio;

    }
    //метод генерации cписка формуляров читателя
    public static HashMap<String, String> getFormulars() {
        if (hmFormReaders == null){
            hmFormReaders= new HashMap<>();
            //System.out.println("\nCreate hmFormulars !!!");
        }
        return hmFormReaders;
    }

    //метод генерации Номера формуляра читателя
    public static int getId() {
        Random rnd = new Random();
        int min = 1, max = 10000;
        int number = min + rnd.nextInt((max - min) + 1);
        return number;
    }

    //(ok) прочитать существующий формуляр читателя
    public static FormulyarReader get(String str) {
        //System.out.println("\ngetFormReader str=" + str);
        String[] mStr=new String[str.length()];
        mStr=str.split(",");

        //System.out.println("\ngetFormReader 222" + mStr.toString());
        FormulyarReader form=new FormulyarReader();
        form.id=mStr[6];
        form.name = mStr[0];
        form.fio=mStr[1];
        form.birthDate=mStr[2];
        form.adress=mStr[3];
        form.work=mStr[4];
        form.telepfone=mStr[5];
        form.strKodReader=form.name + form.fio + form.id;
        //System.out.println("getFormReader():" + form.strKodReader);
        return form;
    }


    //(ok) создать формуляр читателя
    public static FormulyarReader set(String str) {
        //System.out.println("\nsetFormReader str=" + str);
        String[] mStr=new String[str.length()];
        mStr=str.split(",");

        FormulyarReader form=new FormulyarReader();
        form.id=String.valueOf(getId());
        form.name = mStr[0];
        form.fio=mStr[1];
        form.birthDate=mStr[2];
        form.adress=mStr[3];
        form.work=mStr[4];
        form.telepfone=mStr[5];
        form.strKodReader=form.name + form.fio + form.id;
        //System.out.println("setFormReader(): kod=" + form.strKodReader);
        return form;
    }
}
