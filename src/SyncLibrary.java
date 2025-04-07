import java.util.*;

public class SyncLibrary {
    List<String> books = new ArrayList<String>();
    int capacity;

    public SyncLibrary(int capacity){
        this.capacity=capacity;
    }

    public synchronized void returnBook(String book) throws InterruptedException {
        while (books.size() == capacity){
            wait();
        }
        books.add(book);
        notifyAll();
    }

    public String borrowBook() throws InterruptedException {
        String book = "";
        while (books.isEmpty()){
            wait();
        }
        book = books.getFirst();
        notifyAll();
        return book;
    }
}
