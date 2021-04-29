import java.util.ArrayList;

public class CPUProcess extends Thread {

    private final MonitorV2 monitor;
    private ArrayList<Integer> processList;

    private int count = 0;

    public CPUProcess (MonitorV2 monitor, ArrayList processList) {
        this.monitor = monitor;
        this.processList = processList;
    }


    @Override
    public void run () {
        super.run ();

        int shootResult = 0;
        while (true) {
            for (int item : processList) {
                shootResult = monitor.shoot (item);
                if (shootResult == -1) {
                    System.out.println ("END: " + item);
                    return;
                } else if (shootResult > 0) {

                    //System.out.println ("Quise disparar T" + item + " y me voy a dormir " + shootResult + " milisegundos");
                    try {
                        sleep (shootResult + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                    //System.out.println ("Quise disparar T" + item + " y me desperte");
                    monitor.shoot (item);
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
