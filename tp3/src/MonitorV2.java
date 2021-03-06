import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorV2 {
    private final int numberTransitions = 15;

    private static Lock lock = new ReentrantLock ();
    private List<Condition> quesWait = new ArrayList<Condition> ();
    private boolean[] boolQuesWait = new boolean[numberTransitions]; //false = no esta esperando , true = esta esperando

    private Log log;
    private Politica politica;
    private final PN pn;

    private boolean end = false;
    private String transitions = "";
    private static final boolean print = true;
    private static final boolean printDebug = false;


    public MonitorV2 (int packageNumber) {
        pn = new PN (packageNumber);
        initConditions ();
    }

    private void initConditions () {
        for (int i = 0; i < numberTransitions; i++) {
            quesWait.add (lock.newCondition ());
            boolQuesWait[i] = false;
        }

        log = new Log (pn);
        politica = new Politica (pn);
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

    private void signalPoliticV2 () {
        int t = politica.signalPolitic (boolQuesWait); // Devuelve el indice de la transicion donde esta el hilo a despertar
        if (t != -1) {
            quesWait.get (t).signal ();
            return;
        }

        if (pn.ifEnd ()) { // Si la politica devuelve -1 es porque no pudo despertar a nadie, me fijo si tengo que terminar
            end = true;
            if (printDebug) System.out.println ("I'm final boss, bro");
            finalSignalPoliticV2 ();
        }
    }

    private void finalSignalPoliticV2 () { // Despierta a todos los hilos para terminar la ejecucion
        for (int i = 0; i < 15; i++) {
            quesWait.get (i).signal ();
            boolQuesWait[i] = false;
        }
    }

    private void showBoolQuesWait () { // Imprime si las transiciones estan sensibilizadas o no y dormidas o no
        int aux[] = pn.getSensitized ();
        int count = 0;
        for (boolean item : boolQuesWait) {
            if (printDebug) System.out.println ("index:" + count + "    " + item + "  ---   " + aux[count]);
            count++;
        }
        if (printDebug)
            System.out.println ("--------------------------------- 0 --------------------------------------------");
    }

    public int shoot (int index) {  // Dispara una transicion (index) devuelve 1 si pudo hacerla y 0 si no

        lock.lock ();

        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;
        int shootResult = -1;

        while (true) {
            shootResult = pn.isPos (shoot);
            if (shootResult < 0) {
                if (end) {
                    if (printDebug) System.out.println ("I must end my life: " + index);
                    lock.unlock ();
                    return -1;
                }

                if (print) log.printFail (index);

                boolQuesWait[index] = true; // Se queda esperando en la transicion que quiso disparar
                if (printDebug) showBoolQuesWait ();

                try {
                    quesWait.get (index).await ();
                } catch (InterruptedException e1) {
                    e1.printStackTrace ();
                }
            } else if (shootResult == 0) {
                System.out.println ("Shoot: " + index);
                if (print) log.printSuccess (index);

                boolQuesWait[index] = false;
                if (print)
                    showBoolQuesWait ();
                signalPoliticV2 (); //despierta un hilo
                break;
            } else { // Si no le dio ni 0 ni -1 es porque era alguna transicion temporizada
                System.out.println ("Quise disparar T" + index + " y tengo que esperar " + shootResult + "ms");
                lock.unlock ();
                return shootResult;
            }
        }

        try {
            if (verifyMInvariants ()) {
                lock.unlock ();
                return 0;
            }
        } catch (Exception e1) {
            e1.printStackTrace ();
            System.exit (1);
        }

        if (printDebug) System.out.println ("Unlock shoot: " + index);
        lock.unlock ();
        return -1;
    }
}