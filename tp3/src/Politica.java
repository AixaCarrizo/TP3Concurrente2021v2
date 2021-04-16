import java.util.ArrayList;

public class Politica {

    private static String contenido;

    private PN pn;
    private int aux[];
    private Integer listBuff[], addBuffer[];
    private int markVector[];
    private int buffer1, buffer2;
    private final static boolean print = false;

    Politica (PN pn) {
        this.pn = pn;
        listBuff = pn.getIsBuffer ();
        addBuffer = pn.getCountBuffer ();
    }

    public int signalPolitic (boolean[] boolQuesWait) {
        aux = pn.getSensitized ();
        if (boolQuesWait[addBuffer[0]] && boolQuesWait[addBuffer[1]] && aux[addBuffer[0]] == 1) {
            markVector = pn.getMarkVector ();
            if (markVector[listBuff[0]] < markVector[listBuff[1]])
                return addBuffer[0];
            else
                return addBuffer[1];
        }
        for (int i = 0; i < 15; i++) {
            if (aux[i] == 1 && boolQuesWait[i] && i!=5 && i!=13) {
                return i;
            }
        }
        return -1;
    }
/*
    private final CpuBuffer buffer1;
    private final CpuBuffer buffer2;

    static int prevBuff = 2;

    public Politica (CpuBuffer buffer1, CpuBuffer buffer2) {
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
    }

    int bufferPolitic () {
        if (prevBuff == 1) {
            if (buffer1.size () >= buffer2.size ()) {
                prevBuff = 2;
                return 2;
            } else
                return 1;
        } else if (prevBuff == 2) {
            if (buffer2.size () >= buffer1.size ()) {
                prevBuff = 1;
                return 1;
            } else
                return 2;
        }
        return -1;
    }
 */
}
