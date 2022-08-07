import java.util.HashMap;
import java.util.Random;

//модель описывающая сущность - книги пользователя
public class FormulyarBook {
    String date;
    String author;
    String nameBook;
    String id;
    String strKodBook; //"date_id" - код книги (вид например: "2022-01-28_678")

    //двухсвязный список ( КЛЮЧ(код книги), СТРОКА(дата,автор,наименование книги)
    private static HashMap<String, String> hmBooks = null;

    public FormulyarBook() {
    }

    //метод генерации Номера формуляра читателя
    public static int getId() {
        Random rnd = new Random();
        int min = 1, max = 10000;
        int number = min + rnd.nextInt((max - min) + 1);
        return number;
    }

    //(ok) прочитать существующий формуляр читателя
    public static FormulyarBook get(String str) {
        String[] mStr=new String[str.length()];
        mStr=str.split(",");
        FormulyarBook book=new FormulyarBook();
        book.id=mStr[3];
        book.date=mStr[0];
        book.author = mStr[1];
        book.nameBook=mStr[2];
        //ключ в файле книг
        book.strKodBook= book.date + "_" + book.id;
        //System.out.println("book.strKodBook=" + book.strKodBook);
        return book;
    }

    //создаем всегда один список для работы с книгами читателя
    public static HashMap<String, String> getHmBooks() {
        if (hmBooks == null){
            hmBooks= new HashMap<>();
            //System.out.println("\nCreate hmBooks !!!");
        }
        return hmBooks;
    }
}
