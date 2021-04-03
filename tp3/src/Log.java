import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Log implements Runnable {

    private String contenido;
    private static ArrayList<CPUProcess> processThread;
    private static MonitorV2 monitor;
    private final static boolean print = false;

    Log (ArrayList<CPUProcess> processThread,MonitorV2 monitor) {
        this.processThread= processThread;
        this.monitor = monitor;
    }


    public void EscribirContenido () {
       String estado;

        estado = LocalDateTime.now () + monitor.getBuffer ();
        contenido = contenido + estado + "\r\n";
        for (CPUProcess item: processThread)
        {
            estado = " - El estado de " + item.toString()+ " es " + item.getState () + " \r\n";
        }

        contenido = contenido + estado;

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
