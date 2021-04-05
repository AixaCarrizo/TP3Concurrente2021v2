import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorV2 {

    private final int numberTransitions = 15;
    private static Lock lock = new ReentrantLock ();
    private List<Condition> quesWait = new ArrayList<Condition> ();
    private boolean[] boolQuesWait = new boolean[numberTransitions];

    private final PN pn;

    private boolean end = false;
    private String transitions = "";
    private static final String[] numTransitions = {"T0", "T4", "T11", "T3", "T10", "TA", "T12", "T13", "T14", "T2", "T5", "T6", "T7", "T8", "T9"};
    private static final boolean print = false;


    public MonitorV2 (int packageNumber) {
        pn = new PN (packageNumber);
        initConditions ();
    }

    private void initConditions () {
        for (int i = 0; i < numberTransitions; i++) {
            quesWait.add (lock.newCondition ());
            boolQuesWait[i] = false;
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

    private void signalPoliticV2 () {
        int aux[] = pn.getSensitized ();
        for (int i = 0; i < 15; i++) {
            if (print)
                System.out.println ("COSO:" + i + "    " + boolQuesWait[i] + "  ---   " + aux[i]);
            if (aux[i] == 1 && boolQuesWait[i]) {
                if (print)
                    System.out.println ("Wakeup: " + i);
                quesWait.get (i).signal ();
                return;
            }
        }
        if (pn.ifEnd ()) {
            end = true;
            System.out.println ("I'm final boss, bro");
            finalSignalPoliticV2 ();
        }
    }

    private void finalSignalPoliticV2 () {
        for (int i = 0; i < 15; i++) {
            quesWait.get (i).signal ();
            boolQuesWait[i] = false; // TODO: Ta al pedo pero queda lindo
        }
    }

    private void showBoolQuesWait () {
        int aux[] = pn.getSensitized ();
        int count = 0;
        for (boolean item : boolQuesWait) {
            System.out.println ("index:" + count + "    " + item + "  ---   " + aux[count]);
            count++;
        }
        System.out.println ("--------------------------------- 0 --------------------------------------------");
    }

    public int shoot (int index) {  //Dispara una transicion (index) devuelve 1 si pudo hacerla y 0 si no
        lock.lock ();

        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;
        int shootResult = -1;
        while (true) {
            shootResult = pn.isPos(shoot);
            //if (!(pn.isPos (shoot))) {
            if (shootResult < 0) {
                if (end) {
                    System.out.println ("I must end my life: " + index);
                    lock.unlock ();
                    return -1;
                }
                System.out.println ("Don't shoot: " + index);
                printSave (index, 0);

                signalPoliticV2 ();
                boolQuesWait[index] = true;
                if (print)
                    showBoolQuesWait ();
                try {
                    quesWait.get (index).await ();
                } catch (InterruptedException e1) {
                    e1.printStackTrace ();
                }

            } else if(shootResult == 0){
                System.out.println ("Shoot: " + index);
                printSave (index, 1);

                boolQuesWait[index] = false;
                if (print)
                    showBoolQuesWait ();
                signalPoliticV2 ();
                break;
            }
            else{
                System.out.println ("Quise disparar T" + index + "y tengo que esperar " + shootResult + "milisegundos\n");
                lock.unlock();
                return shootResult;
            }
        }

        try {
            if (verifyMInvariants ()) {
                //System.out.println ("Mi invariantes: " + index);

                lock.unlock ();
                return 0;
            }
        } catch (Exception e1) {
            e1.printStackTrace ();
            System.exit (1);
        }

        System.out.println ("Unlock shoot: " + index);
        lock.unlock ();
        return -1;
    }
}