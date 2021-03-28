import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;

public class Main {
    public static CpuBuffer buffer1 = new CpuBuffer ();
    public static CpuBuffer buffer2 = new CpuBuffer ();
    private final static int dataNumber = 300;

    private final static Monitor monitor = new Monitor (buffer1, buffer2, dataNumber);


    public static void main (String[] args) {
        GenData gd = new GenData (monitor, buffer1, buffer2, 50, dataNumber);
        CpuWork cpu1 = new CpuWork (monitor, buffer1, buffer2, 50, 1);
        CpuWork cpu2 = new CpuWork (monitor, buffer1, buffer2, 50, 2);
        CpuPower cpu1_power = new CpuPower (monitor, 1);
        CpuPower cpu2_power = new CpuPower (monitor, 2);
        CpuKeep cpu1_keep = new CpuKeep (monitor, 1);
        CpuKeep cpu2_keep = new CpuKeep (monitor, 2);
        Thread log = new Thread (new Log (buffer1, buffer2, cpu1_power, cpu1_power, cpu1, cpu2));
        cpu1_power.start ();
        cpu2_power.start ();
        cpu1_keep.start ();
        cpu2_keep.start ();
        gd.start ();
        cpu1.start ();
        cpu2.start ();
        log.start ();

        try {
            cpu1_power.join ();
            cpu2_power.join ();
            cpu1_keep.join ();
            cpu2_keep.join ();
            gd.join ();
            cpu1.join ();
            cpu2.join ();
            log.join ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        try {
            File file = new File ("./prueba.txt");
            // Si el archivo no existe es creado
            if (!file.exists ()) {
                file.createNewFile ();
            }
            FileWriter fw = new FileWriter (file);
            BufferedWriter bw = new BufferedWriter (fw);
            bw.write (monitor.getTransitions ());
            bw.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}