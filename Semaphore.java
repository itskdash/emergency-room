public class Semaphore {
    private int availableUnits;

    public Semaphore(int availableUnits) {
        this.availableUnits = availableUnits;
    }

    public synchronized void get() throws InterruptedException {
        while (availableUnits == 0) {
            wait();
        }
        availableUnits--;
    }

    public synchronized void free() {
        availableUnits++;
        notifyAll();
    }

    public synchronized boolean tryGetNow() {
        if (availableUnits > 0) {
            availableUnits--;
            return true;
        }
        return false;
    }

    public synchronized boolean tryget(long timeoutMillis) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        long remaining = timeoutMillis;
        while (availableUnits == 0 && remaining > 0) {
            wait(remaining);
            remaining = deadline - System.currentTimeMillis();
        }
        if (availableUnits > 0) {
            availableUnits--;
            return true;
        }
        return false;
    }
}