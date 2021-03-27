import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;

public class Monitor {
    static Lock lock = new ReentrantLock ();
    //private Condition[] quesOfWait; //si esta vacio el buffer
    List<Condition> quesOfWait = new ArrayList<Condition> ();
    private int packetCounter;
    private final Politica politic;
    private String transitions = new String ("");
    private final int dataNumber;
    private final PN pn = new PN ();
    private final CpuBuffer buffer1;
    private final CpuBuffer buffer2;
    private static final String[] numTransitions = {"T0", "T4", "T11", "T3", "T10", "TA", "T12", "T13", "T14", "T2", "T5", "T6", "T7", "T8", "T9"};
    private static final boolean print = true;

    public Monitor (CpuBuffer buffer1, CpuBuffer buffer2, int dataNumber) {
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
        this.packetCounter = 0;
        this.politic = new Politica (buffer1, buffer2);
        this.dataNumber = dataNumber;
        for (int i = 0; i < 15; i++) {
            quesOfWait.add (lock.newCondition ());
        }
    }


    /**
     * T0: Arrival_rate
     * T1: Power_down_threshold
     * T2: T1
     * T3: T2
     * T4: T3
     * T5: T5
     * T6: T6
     * T7: T7
     */

    public String getTransitions () {
        return transitions;
    }

    /*
    INVARIANTES DE PLAZA
    m1 + m7 = 1
    m4 + m12 + m14 = 1
    m5 + m13 + m15 = 1
    m0 + m6 = 1
    m8 + m9 = 1
    */

    private boolean verifyMInvariants () throws Exception {
        int mark[] = pn.getMarkVector ();

        if (((mark[1] + mark[7]) == 1) && ((mark[4] + mark[12] + mark[14]) == 1) &&
                ((mark[5] + mark[13] + mark[15]) == 1) && ((mark[0] + mark[6]) == 1) && ((mark[8] + mark[9]) == 1))
            return true;
        else {
            throw new Exception ("Fallo en invariantes de plaza");
        }
    }

    private void printSave (int index, int valueToReturn) {
        if (print) {
            if (valueToReturn > 0) {
                System.out.println ("Hice disparo " + numTransitions[index]);
            } else {
                System.out.println ("No se puedo realizar el disparo " + numTransitions[index]);
            }
        }
        if (valueToReturn > 0)
            transitions += numTransitions[index];
    }

    private void signalPolitic () {
        int aux[] = pn.getSensitized ();
        for (int i = 0; i < 15; i++) {
            if (aux[i] == 1) {
                System.out.println ("Despertar a: " + numTransitions[i] + "\n");
                quesOfWait.get (i).signal ();
            }
        }
    }

    public int shoot (int index) {  //Dispara una transicion (index) devuelve 1 si pudo hacerla y 0 si no
        lock.lock ();
        int valueToReturn = 0;

        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;
        while (true) {
            if (!(pn.isPos (shoot))) {
                try {
                    printSave (index, valueToReturn);
                    //signalPolitic ();
                    quesOfWait.get (index).await ();
                } catch (InterruptedException e1) {
                    e1.printStackTrace ();
                }
            } else {
                valueToReturn = 1;
                printSave (index, valueToReturn);
                signalPolitic ();
                if (index == 0) {
                    valueToReturn = politic.bufferPolitic ();
                }
                break;
            }
        }

        if (packetCounter == dataNumber) {
            System.out.println ("Deberia terminar todito. A implementar \n");
        }
        lock.unlock ();
        return valueToReturn;
    }
}