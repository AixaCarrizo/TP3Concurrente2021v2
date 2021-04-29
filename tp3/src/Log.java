import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Log {

    private static String contenido;

    private PN pn;
    private final static boolean print = false;

    private static final String[] numTransitions = {"T0", "T4", "T11", "T3", "T10", "TA", "T12", "T13", "T14", "T2", "T5", "T6", "T7", "T8", "T9"};
    private ArrayList<Integer> useBufferList;
    private ArrayList<Integer> isBufferList;
    private static ArrayList<Integer> countBufferList;
    private ArrayList<Integer> lastCountBufferList;

    Log () {

    }

    Log (PN pn) {
        this.pn = pn;
        initBuffers ();
    }

    private void initBuffers () {
        useBufferList = new ArrayList<> (Arrays.asList (pn.getUseBuffer ()));
        isBufferList = new ArrayList<> (Arrays.asList (pn.getIsBuffer ()));
        countBufferList = new ArrayList<> (Collections.nCopies (isBufferList.size (), 0));
        lastCountBufferList = new ArrayList<> (Collections.nCopies (isBufferList.size (), 0));
    }

    public void printSuccess (int index) {
        StringBuilder aux = new StringBuilder ();
        aux.append ("Hice el disparo ").append (numTransitions[index]).append ("\n");
        System.out.print (aux);

        contenido += aux;
        print (index);
    }

    public void printFail (int index) {
        StringBuilder aux = new StringBuilder ();
        aux.append ("No se pudo realizar el disparo ").append (numTransitions[index]).append ("\n");
        System.out.print (aux);

        contenido += aux;
    }

    private void print (int index) {

        if (useBufferList.contains (index)) {
            StringBuilder aux = new StringBuilder ();
            int count = 0;

            for (int item : isBufferList) {
                int markVector = pn.getMarkVector ()[item];
                aux.append ("El buffer ").append (count + 1).append (" tiene ").append (markVector).append (" elementos").append ("\n");
                countBufferList (count, markVector);
                count++;
            }

            System.out.print (aux);
            contenido += aux;
        }
    }

    private void countBufferList (int countIndex, int countBufferSize) {

        if (lastCountBufferList.get (countIndex) < countBufferSize) {
            int aux = countBufferList.get (countIndex) + 1;
            countBufferList.set (countIndex, aux);
        }

        lastCountBufferList.set (countIndex, countBufferSize);
    }

    public void guardarArchivo () {
        try {
            int count = 0;
            for (Integer item : countBufferList) {
                StringBuilder aux = new StringBuilder ();
                aux.append ("Pasaron ").append (item).append (" elementos por el buffer ").append (count + 1).append ("\n");
                count++;
                contenido += aux;
            }

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
}

