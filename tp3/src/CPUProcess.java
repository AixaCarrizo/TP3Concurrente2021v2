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
                    return; //TODO: Esta mal. En realidad debería remover la transicion y hacer return solo cuando no queda ninguna.
                }

                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*switch (item){
                    case 0:
                    case 3:
                    case 4:
                        try {
                            sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }*/
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
