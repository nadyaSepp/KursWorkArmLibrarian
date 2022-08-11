import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//класс контроллер с сервисными функциями
public class Controller {
    static final int KOD_HM_USERS = 1;
    static final int KOD_HM_FORMULARS = 2;
    static final int KOD_HM_BOOKS = 3;

    //наименования рабочих файлов сохранения данных
    public static String nameFileFormulyars = "formulyars.txt";  //файл "Формуляры" пользователей
    public static String nameFileUsers = "armUsers.txt";       //файл "Формуляры" библиотекарей
    public static String nameBooks = "книги";                  //доп. наименование для файла "КнигиПользователя"

    //метод формирования КЛЮЧ_ЧИТАТЕЛЯ (строка вида: Имя + Фамилия + Номер)
    public String getKodFile(String str, int number) {
        String[] sub;
        sub = str.split(",");
        System.out.println(String.valueOf(number));
        String strFile = sub[0] + sub[1] + String.valueOf(number);
        //System.out.println(str);
        return strFile;
    }

    //(ok) метод идентификации читателя
    public Object kntrRegistration(HashMap<String, String> hm) {
        Scanner Sc = new Scanner(System.in);
        System.out.println("\nВведите Код читателя из списка:\n");
        showKeyFormulars(hm);
        String strIdent = Sc.next();
        //поиск в списке формуляров (HashMap)
        for (Map.Entry m : hm.entrySet()) {
            if (m.getKey().equals(strIdent)) {
                //System.out.println(key);
                return m.getKey();
            }
        }
        return null;
    }

    //(ok) метод идентификации
    public Object kntrDataBooks(HashMap<String, String> hm) {
        Scanner Sc = new Scanner(System.in);
        System.out.println("\nВведите Код книги из списка:\n");
        showFormulars(hm);
        String strIdent = Sc.next();
        //поиск в списке формуляров (HashMap)
        for (Map.Entry m : hm.entrySet()) {
            if (m.getKey().equals(strIdent)) {
                System.out.println(m.getKey());
                return m.getKey();
            }
        }
        return null;
    }
    //(ok) метод генерации Номера формуляра читателя
    public int getNumberFormular() {
        Random rnd = new Random();
        int min = 1, max = 1000;
        int number = min + rnd.nextInt((max - min) + 1);
        return number;
    }

    //(ok) метод регистрации читателя (создаем все нужные файлы под его данные)
    public boolean signUpFormular(HashMap<String, String> hm) throws IOException {
        boolean res = false, boolBooks = false, boolDates = false;

        Scanner Sc = new Scanner(System.in);
        System.out.println("\n--- Регистрация читателя -----\n");
        System.out.println("\nВведите данные читателя через запятую по формату: \n"
                + "Имя,Фамилия,Дата рождения,адрес,место работы,телефон\n");
        String strFormular = Sc.next();

        //создание Кода читателя (строка)
        int number = getNumberFormular();
        String strKodFile = getKodFile(strFormular, number);

        //создание файла для хранения книг читателя
        boolBooks = createFile(nameBooks + strKodFile + ".txt");
        //запись данных пользователя в общий файл "Формуляры"
        if (boolBooks) {
            hm.put(strKodFile, strFormular + "," + String.valueOf(number) );
            res = true;
        }
        return res;
    }

    //метод удаления данных читателя
    public boolean deleteFormularReader(HashMap<String, String> hm) throws IOException {
        //определяемся с наличием файлов пользователя
        Object key = kntrRegistration(hm);
        if (key != null) {
            //deleteFileBooks(hm);
            //удалить данные читателя из файла "Формуляры"
            hm.remove(key);
            return true;
        }
        return false;
    }
    public boolean deleteBooks(HashMap<String, String> hm)throws IOException{
        Object kodReader = kntrRegistration(hm);
        if (kodReader != null) {
            String nameFileBooks = nameBooks + kodReader + ".txt";
            //init hmBooks
            boolean res = setList(getDataFromFile(nameFileBooks), KOD_HM_BOOKS, FormulyarBook.getHmBooks());
            Object key = kntrDataBooks(FormulyarBook.getHmBooks());
            if (key != null) {
                //удалить книгу
                hm.remove(key);
                return true;
            }
        }
        return false;
    }

    //метод запроса книг читателя на запись
    public String getBooksForWrite(){
        Scanner Sc = new Scanner(System.in);
        System.out.println("\n--- Запись книг -----\n");
        System.out.println("\nВведите дату(Формат: ГГГГ-ММ-ДД): ");
        String date = Sc.next();
        Scanner ScA = new Scanner(System.in);
        System.out.println("\nВведите автора книги: ");
        String author = ScA.next();
        Scanner ScB = new Scanner(System.in);
        System.out.println("\nВведите наименование книги: ");
        String book = ScB.next();
        String id=String.valueOf(FormulyarBook.getId());
        return (date + "," + author + "," + book + "," + String.valueOf(id));
    }

    //метод записи книг читателя
    public boolean writeBooksReader(HashMap<String, String> hm) throws IOException {
        //контроль регистрации читателя
        Object kodReader = kntrRegistration(hm);
        if (kodReader != null) {
            //собрать имя файла книг читателя
            String nameFileBooks = nameBooks + kodReader + ".txt";
            //получить строку данных для записи
            String strBooks=getBooksForWrite();
            //запись книг в конец файла
            writeFile(nameFileBooks, strBooks, true);
            return true;
        }
        else {System.out.println("Извините,читатель не зарегистрирован!");}
        return false;
    }

    //поиск долга по дате
    public  boolean searchDate(HashMap<String, String> hmF, HashMap<String, String> hmB) throws IOException {
        //контроль регистрации читателя
        Object kodReader = kntrRegistration(hmF);
        if (kodReader != null) {
            String nameFileBooks = nameBooks + kodReader + ".txt";
            //hmBooks
            boolean res=setList( getDataFromFile(nameFileBooks), KOD_HM_BOOKS, hmB);
            Scanner Sc = new Scanner(System.in);
            System.out.println("\n--- Поиск задолжности -----\n");
            System.out.println("\nВведите дату(Формат: ГГГГ-ММ-ДД): ");
            String date = Sc.next();
            LocalDate dGet=LocalDate.parse(date);
            //System.out.println("dateGet=" + dGet);

            //поиск в списке книг читателя
            for (Map.Entry m : hmB.entrySet()) {
                String[] mStr=new String[m.getValue().toString().length()];
                mStr=m.getValue().toString().split(",");
                //System.out.println("m=" + m.getValue().toString());
                LocalDate dSet=LocalDate.parse(mStr[0].toString());
                //System.out.println("dateSet=" + dSet);
                //! можно и так!
                // int res2=dGet.compareTo(dSet);
                //System.out.println("res=" + res2);
                //if (res2 < 0) System.out.println("Долг по: " + m.getValue());
                if (dSet.isAfter(dGet)) System.out.println("Долг: " + m.getValue());
            }
        }
        else {System.out.println("Извините,читатель не зарегистрирован!");}
        return false;
    }

    //метод удаление файла с книгами
    public boolean deleteFileBooks(HashMap<String, String> hm){
        Object kodReader = kntrRegistration(hm);
        if (kodReader != null) {
            String nameFileBooks = nameBooks + kodReader + ".txt";
            //удалить файл
            Path path = Paths.get(nameFileBooks);
            if (Files.exists(path) == true){
                File myFile = new File(nameFileBooks);
                myFile.delete();
            }
        }
        return true;
    }

    //метод отображения всех книг читателя
    public boolean showBooksReader(HashMap<String, String> hmF,HashMap<String, String> hmB) throws IOException {
        //контроль регистрации читателя
        Object kodReader = kntrRegistration(hmF);
        if (kodReader != null) {
            String nameFileBooks = nameBooks + kodReader + ".txt";
            //System.out.println(nameFileBooks);
            //hmBooks
            boolean res=setList( getDataFromFile(nameFileBooks), KOD_HM_BOOKS, hmB);
            showValueFormulars(hmB);
            return true;
        }
        return false;
    }


    //метод отображения списков
    public void showFormulars(HashMap<String, String> hm) {
        System.out.println("|---Код -----------|------------Данные ---------------------------------------------"  );
        for (Map.Entry m : hm.entrySet()) {
            System.out.println(m.getKey() + "\t\t\t" + m.getValue());
        }
        return;
    }
    public void showValueFormulars(HashMap<String, String> hm) {
        System.out.println("|------------Данные ---------------------------------------------"  );
        for (Map.Entry m : hm.entrySet()) {
            System.out.println(m.getValue());
        }
        return;
    }
    public void showKeyFormulars(HashMap<String, String> hm) {
        System.out.println("|------ Код читателя --------|"  );
        for (Map.Entry m : hm.entrySet()) {
            System.out.println(m.getKey());
        }
        return;
    }

    //(ok) метод перезаписи списка hm в строку
    public StringBuilder getStrHm(HashMap<String, String> hm) {
        StringBuilder str = new StringBuilder();
        //цикл по hm
        for (Map.Entry m : hm.entrySet()) {
            str.append(m.getValue().toString() + "\n");
        }
        //System.out.println("getStrHm(): str\n" + str);
        return str;
    }

    //(ok) метод инициализации рабочих файлов хранения данных
    public void initFile(String fileName, int kodHm) throws IOException {
        //путь к файлу
        Path path = Paths.get(fileName);
        //если файла нет, то создать
        if (Files.exists(path) == false) {
            Files.createFile(path);
            //запись новой информации в файлы
            if (kodHm == KOD_HM_USERS) {
                boolean res = writeFile(fileName,
                        "petrova,12345678\n" + "sidorova,987654321\n" + "i,1", true);
            }
            if (kodHm == KOD_HM_FORMULARS) {
                boolean res = writeFile(fileName,
                        "Анна,Сидорова,09.08.98,Москва пер.Жадов 10-30,НПО,56-44-33б,567\n" +
                                "Петр,Титов,29.10.98,Москва пер.Жадов 10-30,д/сад,55-44-33,5567\n" +
                                "Иван,Титов,12-10-2000,Ульяновск пр.Лен.Кома 12-10,школа 17,23-11-22,34502", true);
            }
        }
        return;
    }

    //(ok) метод создания файла
    public boolean createFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    System.out.println("\nFile " + fileName + " created!");
                else {
                    System.out.println("Something Wrong!");
                    return false;
                }
            } catch (IOException ex) {
                Logger.getLogger(KursWork1.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            System.out.println("\nFile " + fileName + " already exists!");
        }
        return true;
    }

    //(ok) метод записи данных нового читателя в файл "Формуляры"
    //true-дописать файл, false-запись с предварительным стиранием
    public static boolean writeFile(String fileName, String str, boolean kodWrite) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, kodWrite), "KOI8_R"));
        for (int i = 0; i < str.length(); i++) {
            bw.write(str.charAt(i)); //Записали в файл введенную строку по-символьно
        }
        bw.write('\n');
        bw.close();
        return true;
    }

    //(ok) метод распарсивания массива строк в списки (HashMap) согласно коду (kodParser)
    public static boolean setList(String[] allStrFile, int kodParser, HashMap<String, String> hm) {
        if (allStrFile == null) return false;
        String key=null;

        //создать массив строк
        String[][] sub = new String[allStrFile.length][];

        for (int i = 0; i < allStrFile.length; i++) {
            if (kodParser == KOD_HM_USERS) {
                sub[i] = allStrFile[i].split(",");
            }
            if ((kodParser == KOD_HM_FORMULARS) || (kodParser == KOD_HM_BOOKS)){
                sub[i] = allStrFile[i].split("\n");
            }
            //System.out.printf("\nsetList(): sub[%d]= %s", i, Arrays.toString(sub[i]));
        }
        StringBuilder strbArr[] = new StringBuilder[sub.length];
        for (int j = 0; j < sub.length; j++) {
            strbArr[j] = new StringBuilder();
        }
        //итоговый массив строк
        for (int j = 0; j < sub.length; j++) {
            for (int i = 0, k = 1; i < sub[j].length - 1; i++, k++) {
                strbArr[j].append(sub[j][k]);
            }
            ///hm.put(sub[j][0], strbArr[j].toString().trim());
            if (kodParser == KOD_HM_USERS) {
                hm.put(sub[j][0], strbArr[j].toString());
            }
            if (kodParser == KOD_HM_FORMULARS) {
                FormulyarReader form=new FormulyarReader();
                form= FormulyarReader.get(sub[j][0]);
                hm.put(form.strKodReader, sub[j][0]);
            }
            if (kodParser == KOD_HM_BOOKS) {
                FormulyarBook book=new FormulyarBook();
                book=FormulyarBook.get(sub[j][0]);
                hm.put(book.strKodBook, sub[j][0]);
            }
        }
        //System.out.println("\nsetList(): hm");
        //for (Map.Entry m : hm.entrySet()) {
        //    System.out.println(m.getKey() + "\t\t" + m.getValue());
        //}
        return true;
    }

    //(ok) чтение из файла данных в массив строк
    public static String[] getDataFromFile(String fileName) throws IOException {
        //путь к файлу
        Path path = Paths.get(fileName);

        if (Files.exists(path) == true) {
            //создаем объект для чтения
            File myFile = new File(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "KOI8_R"));
            StringBuilder strb = new StringBuilder();
            //System.out.printf("\ngetUsers(): %d \n",myFile.length());
            //читаем по-байтно
            for (int i = 0; i < myFile.length(); i++) {
                strb.append((char) br.read());
            }
            br.close();
            //получаем строку байт
            //System.out.printf("getDannFromFile() strb=%s ",strb);

            //перезапись из строки байт в массив строк (разделитель строк - "\n)
            String[] mStr = strb.toString().split("\n");

            //контрольная печать массива строк
            //for (int i=0;i<mStr.length;i++) {
            // System.out.printf("\ngetDannFromFile() %s ", mStr[i].toString());
            //}
            return mStr;
        }
        return null;
    }


}
