public class Patient extends Thread {
    private int id;
    private WaitingRoom waitingRoom;
    private boolean treated;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
        this.treated = false;
    }

    public int getPatientId() {
        return id;
    }

    public void arriving() {
        System.out.println("P" + id + " arrived.");
    }

    @Override
    public void run() {
        try {
            arriving();
            waitingRoom.enter(this);
            synchronized (this) {
                while (!treated) {
                    wait();
                }
            }
            System.out.println("P" + id + " left.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void treat() {
        treated = true;
        notifyAll();
    }
}