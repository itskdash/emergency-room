import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {
    private Queue<Patient> waitingQueue = new LinkedList<>();
    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutualExclusion;

    public WaitingRoom(int capacity) {
        this.empty = new Semaphore(capacity);
        this.full = new Semaphore(0);
        this.mutualExclusion = new Semaphore(1);
    }

    public void enter(Patient patient) throws InterruptedException {
        addPatient(patient);
    }

    public void returnPatient(Patient patient) throws InterruptedException {
        addPatient(patient);
    }

    private void addPatient(Patient patient) throws InterruptedException {
        if (!empty.tryGetNow()) {
            System.out.println("The waiting room is full.");
            System.out.println("P" + patient.getPatientId() + " is waiting outside for an available seat.");
            empty.get();
        }
        mutualExclusion.get();
        waitingQueue.add(patient);
        System.out.println("P" + patient.getPatientId() + " entered the waiting room.");
        mutualExclusion.free();
        full.free();
    }

    public Patient takePatient() throws InterruptedException {
        full.get();
        mutualExclusion.get();
        Patient patient = waitingQueue.poll();
        mutualExclusion.free();
        empty.free();
        return patient;
    }
}