import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
        // 1. Иницијализација на баферот и синхронизација
        Buffer buffer = new Buffer();
        Solution.init(buffer);

        // 2. Стартување на Producer и Controller примероци
        Solution.Producer p1 = new Solution.Producer();
        Solution.Controller c1 = new Solution.Controller();
        Solution.Controller c2 = new Solution.Controller();
        Solution.Controller c3 = new Solution.Controller();

        // 3. Извршување
        p1.execute();
        c1.execute();
        c2.execute();
        c3.execute();
    }
}

// ✅ Dummy Buffer класата со методите што треба
class Buffer {
    public void produce() {
        System.out.println("Producing item by " + Thread.currentThread().getName());
    }

    public void check() {
        System.out.println("Checking item by " + Thread.currentThread().getName());
    }
}

// ✅ Решението според условите (access control и ограничувања)
class Solution {

    static Buffer buffer;

    static Semaphore accessBuffer;   // дозволува само 1 пристап (check или produce)
    static Semaphore lock;           // штити numChecks
    static Semaphore canCheck;       // дозволува до 10 проверки истовремено
    static int numChecks = 0;

    public static void init(Buffer buf) {
        buffer = buf;
        accessBuffer = new Semaphore(1);
        lock = new Semaphore(1);
        canCheck = new Semaphore(10);
        numChecks = 0;
    }

    public static class Producer {
        public void execute() {
            try {
                accessBuffer.acquire();         // нема паралелни проверки
                buffer.produce();               // врши додавање
                accessBuffer.release();         // дозволи други
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static class Controller {
        public void execute() {
            try {
                lock.acquire();
                if (numChecks == 0) {
                    accessBuffer.acquire();     // блокира добавување додека траат проверки
                }
                numChecks++;
                lock.release();

                canCheck.acquire();             // max 10 проверки
                buffer.check();
                canCheck.release();

                lock.acquire();
                numChecks--;
                if (numChecks == 0) {
                    accessBuffer.release();     // кога нема повеќе проверки → дозволи додавање
                }
                lock.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
