public class CpuWork extends Thread {

    private final Monitor monitor;
    private final CpuBuffer buffer1;
    private final CpuBuffer buffer2;
    private final int serviceRate;
    private final int cpunumber;


    public CpuWork (Monitor monitor, CpuBuffer cpuBuffer1, CpuBuffer cpuBuffer2, int serviceRate, int cpuNumber) {
        this.monitor = monitor;
        this.buffer1 = cpuBuffer1;
        this.serviceRate = serviceRate;
        this.cpunumber = cpuNumber - 1;
        this.buffer2 = cpuBuffer2;
    }

    @Override
    public void run () {
        super.run ();
        int tasks = 1;

        while (true) {
            monitor.shoot (9 + cpunumber * 5); // T2 y T9 (Index: 9 y 14)

            if (cpunumber == 0)
                buffer1.remove ();   // Saco un elemento del buffer 1
            else
                buffer2.remove ();  // Saco un elemento del buffer 2

            try {
                Thread.sleep (serviceRate);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            monitor.shoot (3 + cpunumber); // Disparo service_rate1-2 (Index:  3 y 4);
            System.out.println ("CPU" + (cpunumber + 1) + "          : Realizo su tarea numero " + tasks);
            tasks++;
        }
    }
    //System.out.println (("CPU" + (cpunumber + 1) + "          : Good Bye!"));
}