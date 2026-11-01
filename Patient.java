public class Patient extends Thread {
    private int id;
    private WaitingRoom waitingRoom;
    private boolean treated = false;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    public long getId(){
        return id;
    }

    public void arriving() {
        System.out.print("P" + id + " arrived.");
    }

    @Override 
    public void run(){
        arriving();
        try{
            waitingRoom.enter(this); //waiting room function

            synchronized(this){
                while(!treated){
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
        notify();
    }
}