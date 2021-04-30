import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    private final static int dataNumber = 1000;
    private static ArrayList<CPUProcess> processThread = new ArrayList<CPUProcess> ();
    private static ArrayList<ArrayList<Integer>> arrayTransitions = new ArrayList<ArrayList<Integer>> ();

    private final static MonitorV2 monitor = new MonitorV2 (dataNumber);
    //private final static int[][] arrayTransitions = {{10},{6},{9,3},{14,4},{0},{5},{13},{11,12,1},{7,8,2}};


    private static void initProcess () {

        for (int i = 0; i < 9; i++) {
            arrayTransitions.add (new ArrayList<> ());
        }

        arrayTransitions.get (0).add (10);//t5
        arrayTransitions.get (1).add (6);//t12
        arrayTransitions.get (2).add (0);//t0
        arrayTransitions.get (3).add (5);//t1
        arrayTransitions.get (4).add (13);//t8

        arrayTransitions.get (5).add (9);//t2
        arrayTransitions.get (5).add (3);//srv_rate1

        arrayTransitions.get (6).add (14);//t9
        arrayTransitions.get (6).add (4);//srv_rate2

        arrayTransitions.get (7).add (11);//t6
        arrayTransitions.get (7).add (12);//t7
        arrayTransitions.get (7).add (1);//power_down1

        arrayTransitions.get (8).add (7);//t13
        arrayTransitions.get (8).add (8);//t14
        arrayTransitions.get (8).add (2);//power_down2

        try {
            for (ArrayList<Integer> items : arrayTransitions) {
                processThread.add (new CPUProcess (monitor, items));
            }

            for (CPUProcess item : processThread) {
                item.start ();
            }

            for (CPUProcess item : processThread) {
                item.join ();
            }

        } catch (Exception e) {
            System.out.println (e);
        }
    }


    public static void main (String[] args) {

        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        System.out.println(timeStamp);

        initProcess ();
        new Log ().guardarArchivo ();

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
        timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        System.out.println(timeStamp);
    }


}