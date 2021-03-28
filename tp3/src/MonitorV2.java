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

    private final PN pn = new PN ();
    private int packageNumber, packageCounter, initialCounter;
    private ArrayList<Integer> finalTransitions, initialTransitions;

    private boolean end, initialEnd = false;
    private String transitions = "";
    private static final String[] numTransitions = {"T0", "T4", "T11", "T3", "T10", "TA", "T12", "T13", "T14", "T2", "T5", "T6", "T7", "T8", "T9"};
    private static final boolean print = true;


    public MonitorV2(int packageNumber) {
        initConditions();
        this.packageNumber = packageNumber;
        this.finalTransitions = new ArrayList<>();
        this.initialTransitions = new ArrayList<>();
    }

    private void initConditions() {
        for (int i = 0; i < numberTransitions; i++) {
            quesWait.add (lock.newCondition ());
            boolQuesWait[i] = false;
        }
    }

    public void addFinalTransitions(int finalTransitions) {
        this.finalTransitions.add(finalTransitions);
    }

    public void addInitialTransitions(int initialTransitions) {
        this.initialTransitions.add(initialTransitions);
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

    private void iMSpecial(Integer index){
        if ( initialTransitions.contains(index)){
            System.out.println("I'm initial boss, bro");

            initialCounter++;
            if( initialCounter == packageNumber ) this.initialEnd = true;
        }

        if ( finalTransitions.contains(index)){
            System.out.println("I'm final boss, bro");

            packageCounter++;
            if ( packageCounter == packageNumber ) this.end = true;
        }
    }

    private void signalPoliticV2() {
        int aux[] = pn.getSensitized ();
        for (int i = 0; i < 15; i++) {
            if (aux[i] == 1 && boolQuesWait[i]) {
                System.out.println("Wakeup: " + i);

                quesWait.get (i).signal ();
                return;
            }
        }

        System.out.println("Anyone is here");
    }

    private void showBoolQuesWait(){
        int aux = 0;
        for ( boolean item : boolQuesWait ){
            System.out.println("aux:" + aux++ + "    " + item);
        }
    }

    public int shoot (int index) {  //Dispara una transicion (index) devuelve 1 si pudo hacerla y 0 si no
        lock.lock ();

        int[] shoot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        shoot[index] = 1;

        while (true) {
            if (!(pn.isPos (shoot))) {
                System.out.println("Don't shoot: " + index);
                printSave (index, 0);

                if (end) return -1;

                boolQuesWait[index] = true;
                signalPoliticV2 ();

                try {
                    showBoolQuesWait();
                    quesWait.get (index).await ();
                } catch (InterruptedException e1) {
                    e1.printStackTrace ();
                }

            } else {
                System.out.println("Shoot: " + index);
                printSave (index, 1);

                iMSpecial(index);
                boolQuesWait[index] = false;
                showBoolQuesWait();
                signalPoliticV2 ();

                break;
            }
        }
        if (end || initialEnd) {
            System.out.println("I must end my life");

            initialEnd = false;
            return -1;
        }

        try {
            if (verifyMInvariants ()) {
                System.out.println("Mi invariantes");

                lock.unlock ();
                return 0;
            }
        } catch (Exception e1) {
            e1.printStackTrace ();
            System.exit (1);
        }

        System.out.println("Unlock shoot: " + index);
        lock.unlock ();
        return -1;
    }
}