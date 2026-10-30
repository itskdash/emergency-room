public class Semaphore {
    private int treatmentRooms;
    private int maxRooms;

    public Semaphore(int treatmentRooms) {
        this.treatmentRooms = treatmentRooms;
        this.maxRooms = treatmentRooms;
    }

    public synchronized void get() throws InterruptedException {
        while (treatmentRooms == 0) {
            wait();
        }
        treatmentRooms--;
    }

    public synchronized void free() {
        if (treatmentRooms < maxRooms){
        treatmentRooms++;
        notifyAll();
        }
    }

    public synchronized boolean tryget() throws InterruptedException {
        
        long start = System.nanoTime();
        long timeout = 5_000_000_000L;
        long passed = 0;
        long remaining = timeout;
        while (treatmentRooms == 0 && remaining > 0){
            wait(remaining / 1_000_000, (int)(remaining % 1_000_000));
            passed = System.nanoTime() - start;
            remaining = timeout - passed;
        }
        if (treatmentRooms > 0) {
            treatmentRooms--;
            return true;
        }
            
        return false;
    }
}
