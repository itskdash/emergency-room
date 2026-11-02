public class Hospital {
    private int numberOfDoctors;
    private int numberOfPatients;
    private WaitingRoom waitingRoom;
    private Semaphore treatmentRooms;

    public Hospital(int waitingRoomCapacity, int numberOfDoctors, int numberOfPatients) {
        this.numberOfDoctors = numberOfDoctors;
        this.numberOfPatients = numberOfPatients;
        waitingRoom = new WaitingRoom(waitingRoomCapacity);
        treatmentRooms = new Semaphore(2);
    }

    public void simulationEvents() throws InterruptedException {
        Doctor[] doctors = new Doctor[numberOfDoctors];
        Patient[] patients = new Patient[numberOfPatients];

        for (int i = 0; i < numberOfDoctors; i++) {
            doctors[i] = new Doctor(i + 1, waitingRoom, treatmentRooms);
        }
        for (int i = 0; i < numberOfPatients; i++) {
            patients[i] = new Patient(i + 1, waitingRoom);
        }

        for (Doctor doctor : doctors) {
            doctor.start();
        }

        System.out.println("Patients arriving:");
        StringBuilder header = new StringBuilder();
        for (int i = 1; i <= numberOfPatients; i++) {
            header.append("P").append(i).append(" ");
        }
        System.out.println(header.toString().trim());

        for (Patient patient : patients) {
            patient.start();
        }
        for (Patient patient : patients) {
            patient.join();
        }

        System.out.println("All patients have been treated.");

        for (Doctor doctor : doctors) {
            doctor.interrupt();
        }
        for (Doctor doctor : doctors) {
            doctor.join();
        }

        System.out.println("Simulation completed.");
    }
}