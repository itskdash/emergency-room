import java.util.Random;

public class Doctor extends Thread {
    private int id;
    private WaitingRoom waitingRoom;
    private Semaphore treatmentRooms;
    private Random random = new Random();

    public Doctor(int id, WaitingRoom waitingRoom, Semaphore treatmentRooms) {
        this.id = id;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
    }

    @Override 
    public void run() {
        try{
            while (!Thread.currentThread().isInterrupted()) {
                Patient patient = waitingRoom.takePatient(); //waiting room function
                if (patient != null) {
                    break;
                }
                System.out.println("Doctor " + id + " is treating P" + patient.getId() + ".");
                System.out.println("Doctor " + id + "is trying to acquire a treatment room.");
        
                boolean acquired = treatmentRooms.tryget();

                if(!acquired){
                    System.out.println("Doctor " + id + " could not acquire a treatment room. Waiting...");
                    System.out.println("Doctor "+ id + "is sending P" + patient.getId() + " back to the waiting room.");
                    waitingRoom.returnPatient(patient); //waiting room function
                    Thread.sleep(100);

                    continue;
                }
                try{
                    System.out.println("Doctor " + id + " acquired a treatment room.");
                    System.out.println("Doctor " + id + " is treating P" + patient.getId() + ".");
                    int treatmentTime = random.nextInt(3000) +2000; 
                    Thread.sleep(treatmentTime);
                    System.out.println("Doctor " + id + " finished treating P" + patient.getId() + ".");
                    patient.treat();
                } finally {
                    treatmentRooms.free();
                    System.out.println("Doctor " + id + " released a treatment room.");
                }
                System.out.println("Doctor " + id + " is going back to the waiting room.");
            }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
        }
    }
}