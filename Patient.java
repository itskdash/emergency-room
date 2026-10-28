public class Patient {
    private int id;

    public Patient(int id) {
        this.id = id;
    }

    public void arriving() {
        System.out.print("P" + id + " ");
    }
}