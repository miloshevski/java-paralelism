import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreLibrary {
    List<String> books = new ArrayList<String>();
    int capacity;
    Semaphore coordinator = new Semaphore(1);
    Semaphore borrowBookSemaphore = new Semaphore(10);
    Semaphore returnBookSemaphore = new Semaphore(10);

    public SemaphoreLibrary(int capacity){
        this.capacity=capacity;
    }

    public  void returnBook(String book) throws InterruptedException {
        returnBookSemaphore.acquire();
        coordinator.acquire();
        while (books.size() == capacity){
            Thread.sleep(1000);
        }
        books.add(book);
        coordinator.release();
    }

    public String borrowBook() throws InterruptedException {
        borrowBookSemaphore.acquire();
        String book = "";
        coordinator.acquire();
        while (books.size() == 0){
            Thread.sleep(1000);
        }
        book=books.remove(0);
        return book;
    }
}
