import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Waiting room capacity:");
        int capacity = input.nextInt();
        System.out.print("Number of doctors:");
        int doctors = input.nextInt();
        System.out.print("Number of patients:");
        int patients = input.nextInt();
        Hospital emergencyRoom = new Hospital(capacity, doctors, patients);
        emergencyRoom.simulationEvents();
    }
}