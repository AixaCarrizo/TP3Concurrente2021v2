public class GenData extends Thread {

    private final Monitor monitor;
    private final CpuBuffer buffer1;
    private final CpuBuffer buffer2;
    private final int arrivalRate;
    private final int dataNumber;

    public GenData (Monitor monitor, CpuBuffer buffer1, CpuBuffer buffer2, int arrivalRate, int dataNumber) {
        this.monitor = monitor;
        this.buffer1 = buffer1;
        this.arrivalRate = arrivalRate;
        this.dataNumber = dataNumber;
        this.buffer2 = buffer2;
    }

    @Override
    public void run () {
        super.run ();

        try {
            int nroData = 1;
            while (dataNumber >= nroData) {
                int cpuId;
                Thread.sleep (arrivalRate);

                cpuId = monitor.shoot (0) - 1; // Disparo Arrival_rate
                monitor.shoot (5 + cpuId * 8); // Disparo T1 / T8

                if (cpuId == 0)
                    buffer1.add ("Dato numero: " + nroData); // Agrego un elemento al buffer
                else
                    buffer2.add ("Dato numero: " + nroData); // Agrego un elemento al buffer (Cambiar por buffer2)

                System.out.println ("GenData  : Genero dato numero " + nroData);
                nroData++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        System.out.println (("GenData  : Good Bye!"));
    }
}