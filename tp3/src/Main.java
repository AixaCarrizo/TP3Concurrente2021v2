import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;
import java.util.ArrayList;

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

        arrayTransitions.get (0).add (10);
        arrayTransitions.get (1).add (6);
        arrayTransitions.get (2).add (0);
        arrayTransitions.get (3).add (5);
        arrayTransitions.get (4).add (13);

        arrayTransitions.get (5).add (9);
        arrayTransitions.get (5).add (3);

        arrayTransitions.get (6).add (14);
        arrayTransitions.get (6).add (4);

        arrayTransitions.get (7).add (11);
        arrayTransitions.get (7).add (12);
        arrayTransitions.get (7).add (1);

        arrayTransitions.get (8).add (7);
        arrayTransitions.get (8).add (8);
        arrayTransitions.get (8).add (2);

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
    }


}