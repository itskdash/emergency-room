import java.util.Random;

public class Doctor extends Thread {
    private int id;
    private WaitingRoom waitingRoom;
    private Semaphore treatmentRooms;
    private Random random;

    public Doctor(int id, WaitingRoom waitingRoom, Semaphore treatmentRooms) {
        this.id = id;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Patient patient = waitingRoom.takePatient();
                System.out.println("Doctor " + id + " is assigned to P" + patient.getPatientId() + ".");
                System.out.println("Doctor " + id + " is trying to get a treatment room.");

                boolean got = treatmentRooms.tryget(3000);

                if (!got) {
                    System.out.println("Doctor " + id + " could not get a treatment room.");
                    System.out.println("Doctor " + id + " is sending P" + patient.getPatientId()
                            + " back to the waiting room.");
                    waitingRoom.returnPatient(patient);
                    System.out.println("Doctor " + id + " is now available.");
                    continue;
                }

                try {
                    System.out.println("Doctor " + id + " got a treatment room.");
                    System.out.println("Doctor " + id + " begins treating P" + patient.getPatientId() + ".");
                    Thread.sleep(random.nextInt(5000) + 3000);
                    System.out.println("Doctor " + id + " finished treating P" + patient.getPatientId() + ".");
                } finally {
                    treatmentRooms.free();
                    System.out.println("Doctor " + id + " free a treatment room.");
                }

                System.out.println("Doctor " + id + " is now available.");
                patient.treat();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}