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
        listBuff = pn.getIsBuffer (); // Devuelve los indices de las marcas de los buffers
        addBuffer = pn.getCountBuffer (); // Devuelve las transiciones que alimentan los buffers
    }

    public int signalPolitic (boolean[] boolQuesWait) {
        aux = pn.getSensitized ();
        if (boolQuesWait[addBuffer[0]] && boolQuesWait[addBuffer[1]] && aux[addBuffer[0]] == 1) {
            markVector = pn.getMarkVector ();

            if (markVector[listBuff[0]] < markVector[listBuff[1]]) {
                return addBuffer[0];
            } else if (markVector[listBuff[0]] >= markVector[listBuff[1]]) {
                return addBuffer[1];
            }

        }
        /*
        if(aux[6] == 1 && boolQuesWait[6])
            return 6;
        if(aux[10]== 1 && boolQuesWait[10])
            return 10;
         */
        for (int i = 0; i < 15; i++) {
            if (aux[i] == 1 && boolQuesWait[i] && i != 5 && i != 13) {
                return i;
            }
        }
        return -1;
    }
}
