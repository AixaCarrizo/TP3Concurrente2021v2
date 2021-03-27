
public class CpuPower extends Thread {

    private final Monitor monitor;
    private final int cpunumber;

    public CpuPower (Monitor monitor, int cpuNumber) {
        this.monitor = monitor;
        this.cpunumber = cpuNumber - 1;
    }

    @Override
    public void run () {
        super.run ();
        int flag;

        while (true) {
            monitor.shoot (11 - cpunumber * 4);  // T6 y T13 (Index: 11 y 7)
            monitor.shoot (12 - cpunumber * 4); // T7 y T14 (Index: 12 y 8)
            monitor.shoot (1 + cpunumber); // Apago CPU
        }
    }
}