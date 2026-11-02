public class Semaphore {
    private int availableUnits;
    private int maxUnits;

    public Semaphore(int availableUnits) {
        this.availableUnits = availableUnits;
        this.maxUnits = availableUnits;
    }

    public synchronized void get() throws InterruptedException {
        while (availableUnits == 0) {
            wait();
        }
        availableUnits--;
    }

    public synchronized void free() {
        if (availableUnits < maxUnits) {
            availableUnits++;
            notifyAll();
        }
    }

    public synchronized boolean tryget() throws InterruptedException {

        long start = System.nanoTime();
        long timeout = 3_000_000_000L;
        long passed = 0;
        long remaining = timeout;
        while (availableUnits == 0 && remaining > 0) {
            wait(remaining / 1_000_000, (int) (remaining % 1_000_000));
            passed = System.nanoTime() - start;
            remaining = timeout - passed;
        }
        if (availableUnits > 0) {
            availableUnits--;
            return true;
        }

        return false;
    }
}
