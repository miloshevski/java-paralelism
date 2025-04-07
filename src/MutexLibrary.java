import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutexLibrary {
    List<String> books = new ArrayList<String>();
    int capacity;
    public static Lock lock = new ReentrantLock();

    public MutexLibrary(int capacity){
        this.capacity=capacity;
    }

    public  void returnBook(String book) throws InterruptedException {
        while (true){
            lock.lock();
            if (books.size() < capacity){
                books.add(book);
                lock.unlock();
                break;
            }
            lock.unlock();
        }
    }

    public String borrowBook() throws InterruptedException {
        String book = "";
        lock.lock();
        while (true){
            if (!books.isEmpty()){
                book = books.getFirst();
                lock.unlock();
                break;
            }
            lock.unlock();
        }
        return book;
    }
}
