import java.util.Scanner;

public class Hospital {
    private int waitingRoomCapacity;
    private int numberOfDoctors;
    private int numberOfPatients;

    public Hospital(
            int waitingRoomCapacity,
            int numberOfDoctors,
            int numberOfPatients) {
        this.waitingRoomCapacity = waitingRoomCapacity;
        this.numberOfDoctors = numberOfDoctors;
        this.numberOfPatients = numberOfPatients;
    }

    public void simulationEvents() {
        System.out.println("Patients arriving:");
        for (int i = 1; i <= numberOfPatients; i++) {
            Patient p = new Patient(i);
            p.arriving();
        }
        System.out.println();
    }

}