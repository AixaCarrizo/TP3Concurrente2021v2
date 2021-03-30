import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
/*
public class Log implements Runnable {

    private String contenido;
    private final CpuBuffer buff1;
    private final CpuBuffer buff2;
    private final CpuPower controller1;
    private final CpuPower controller2;
    private final CpuWork cpu1;
    private final CpuWork cpu2;

    private final static boolean print = false;

    Log (CpuBuffer buffer1, CpuBuffer buffer2, CpuPower controller1, CpuPower controller2, CpuWork cpu1, CpuWork cpu2) {
        this.buff1 = buffer1;
        this.buff2 = buffer2;
        this.controller1 = controller1;
        this.controller2 = controller2;
        //this.gd = gd;
        this.cpu1 = cpu1;
        this.cpu2 = cpu2;
    }


    public void EscribirContenido () {
        String estadoBuff;
        String estadoCpu;
        String estadoController;
        estadoBuff = LocalDateTime.now () + " - El buffer 1 tiene " + buff1.size () + " elementos y el buffer 2 tiene " + buff2.size () + " elementos.";
        contenido = contenido + estadoBuff + "\r\n";
        estadoCpu = " - El estado del CPU 1" + " es " + cpu1.getState () + " y el estado del CPU 2 es " + cpu2.getState () + " \r\n";
        contenido = contenido + estadoCpu;
        estadoController = " - El estado del controlador 1" + " es " + controller1.getState () + " y el estado del controlador 2 es " + controller2.getState () + "\r\n";
        contenido = contenido + estadoController;
        //contenido = contenido + buff1.size() + " " + buff2.size() + "\n";
    }

    public void GuardarArchivo () {
        try {
            String ruta = "./log.txt";
            File file = new File (ruta);
            if (!file.exists ()) {
                file.createNewFile ();
            }
            FileWriter fw = new FileWriter (file);
            BufferedWriter bw = new BufferedWriter (fw);

            bw.write (contenido);
            bw.close ();
            if (print) {
                System.out.println ("Contenido Guardado: ");
                System.out.println (contenido);
                System.out.println ("Se ha guardado el txt con exito. Enhorabuena!");
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void run () {
        for (int j = 1; j <= 2000; j++) {
            this.EscribirContenido ();
            try {
                TimeUnit.MILLISECONDS.sleep (25);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
                System.out.print ("Process Failed");
            }
        }
        this.GuardarArchivo ();

    }

}
*/