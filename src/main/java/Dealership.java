import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Dealership {
    private List<String> carList = new ArrayList<>();
    private ReentrantLock reentrantLock;
    private Condition condition;

    public Dealership() {
        reentrantLock = new ReentrantLock();
        condition = reentrantLock.newCondition();
    }

    public void produceOneCar(String name) {
        reentrantLock.lock();
        try {
            carList.add(name);
            condition.signal();
        }
        finally {
            reentrantLock.unlock();
        }
    }

    public void getOneCar() {
        reentrantLock.lock();
        try {
            if (carList.size() == 0) {
                System.out.println("Покупатель " + Thread.currentThread().getName() + ": у диллера нет машин, ожидаю поставки.");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            carList.remove(0);
            System.out.println("Покупатель " + Thread.currentThread().getName() + ": купил новую машину.");
        }
        finally {
            reentrantLock.unlock();
        }

    }
}
