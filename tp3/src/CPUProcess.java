import java.util.ArrayList;

public class CPUProcess extends Thread {

    private final MonitorV2 monitor;
    private ArrayList<Integer> processList;

    private int count = 0;

    public CPUProcess (MonitorV2 monitor, ArrayList processList) {
        this.monitor = monitor;
        this.processList = processList;
    }

    /*
    public CPUProcess(MonitorV2 monitor, int process){
        this.monitor = monitor;
        this.processList = new ArrayList<>();
        this.processList.add(process);
    }
    */

    @Override
    public void run () {
        super.run ();
        System.out.println ("Holaaaa :): " + processList.get (0));

        while (true) {
            for (int item : processList) {
                if (monitor.shoot (item) == -1) {
                    System.out.println ("END: " + item);
                    return; //TODO: Esta mal. En realidad debería remover la transicion y hacer return solo cuando no queda ninguna.
                }

                try {
                    sleep (20);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }

            }
        }
    }

    @Override
    public String toString () {
        return "CPUProcess{" +
                "processList=" + processList +
                '}';
    }
}
