import java.util.ArrayList;

public class CPUProcess extends Thread{

    private final MonitorV2 monitor;
    private final ArrayList<Integer> processList;

    private int count = 0;

    public CPUProcess( MonitorV2 monitor, ArrayList processList){
        this.monitor = monitor;
        this.processList = processList;
    }

    public CPUProcess(MonitorV2 monitor, int process){
        this.monitor = monitor;
        this.processList = new ArrayList<>();
        this.processList.add(process);
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            for( int item : processList ) {
                if ( monitor.shoot(item) == -1 ){
                    System.out.println("END: " + item);
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "CPUProcess{" +
                "processList=" + processList +
                '}';
    }
}
