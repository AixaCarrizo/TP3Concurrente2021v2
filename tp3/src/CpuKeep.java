public class CpuKeep extends Thread {

    private final Monitor monitor;
    private final int cpunumber;

    public CpuKeep (Monitor monitor, int cpuNumber) {
        this.monitor = monitor;
        this.cpunumber = cpuNumber - 1;
    }

    @Override
    public void run () {
        super.run ();
        int flag = 0;

        while (flag != -1) {
            flag = monitor.shoot (10 - cpunumber * 4); // T5 y T12 (Index: 10 y 6)
        }
        System.out.println (("CpuKepp" + (cpunumber + 1) + " : Good Bye!"));
    }
}