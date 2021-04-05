import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

public class PN {
    private int[] M;
    private int[] B;
    private int[][] I;
    private int[][] Ipos;
    private int[][] Ineg;
    private int[][] H;
    private int[] E;
    private LocalTime[] sensitizedTime;
    private int minTimeArrival;
    private int minTimeSrv1;
    private int minTimeSrv2;
    private int estados; //N
    private int transiciones; //M
    private static int dataNumber;
    private static int packetCounter = 0;
    private static boolean print = false;

    /*
    // N = cant de estados , M = cant de transiciones
    public PN(int[] m, int[] b, int[] q, int[][] i, int[][] h, int[] E) {
        this.M = m; // Vector de marcado inicial // (N x 1)
        this.B = b; // Si B[i] = 1, la transicion esta desensibilizada (M x 1)
        this.I = i; // Matriz de incidencia (N x M)
        this.H = h; // Matriz de inhibicion (M x N)
    }
     */

    public PN (int dataNumber) {
        this.dataNumber = dataNumber;
        init ();
    }

    public void init () {
        this.M = new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1}; //Vector de marcado inicial

        this.Ipos = new int[][]{
//               Ar P  P2 S  S2 1  12 13 14 2  5  6  7  8  9
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},    // M0=Active
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},    // M1=Active_2
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M2=CPU_buffer
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},    // M3=CPU_buffer2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0},    // M4=CPU_ON
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1},    // M5=CPU_ON_2
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M6=Idle
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M7=Idle_2
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},    // M8=P0
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M9=P1
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},    // M10=P13
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0},    // M11=P6
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},    // M12=Power_up
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},    // M13=Power_up_2
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M14=Stand_by
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M15=Stand_by_2
        };

        this.Ineg = new int[][]{
//               Ar P  P2 S  S2 1  12 13 14 2  5  6  7  8  9
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M0=Active
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M1=Active_2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},    // M2=CPU_buffer
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},    // M3=CPU_buffer2
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0},    // M4=CPU_ON
                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},    // M5=CPU_ON_2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},    // M6=Idle
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},    // M7=Idle_2
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},    // M8=P0
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},    // M9=P1
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0},    // M10=P13
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0},    // M11=P6
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},    // M12=Power_up
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},    // M13=Power_up_2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},    // M14=Stand_by
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},    // M15=Stand_by_2

        };


        this.I = new int[][]{
//               Ar P  P2 S  S2 1  12 13 14 2  5  6  7  8  9
                {0, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},        // M0=Active
                {0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},        // M1=Active_2
                {0, 0, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, 0, 0},        // M2=CPU_buffer
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1},        // M3=CPU_buffer2
                {0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},        // M4=CPU_ON
                {0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},        // M5=CPU_ON_2
                {0, 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0},        // M6=Idle
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},        // M7=Idle_2
                {-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},        // M8=P0
                {1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, -1, 0},       // M9=P1
                {0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 0, 0, 0, 1, 0},       // M10=P13
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, -1, 0, -1, 0, 0},       // M11=P6
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0},        // M12=Power_up
                {0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0},        // M13=Power_up_2
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0},        // M14=Stand_by
                {0, 0, 1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0},        // M15=Stand_by_2
        };

        this.H = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A0=arrival_rate
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A1=Power_down_threshold
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A2=Power_down_2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A3=Service_rate
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A4=Service_rate_2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A5=T1
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A6=T12
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A7=T13
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A8=T14
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A9=T2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A10=T5
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A11=T6
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A12=T7S
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A13=T8
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // A14=T9
        };


        this.B = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Vector de transiciones desensibilizadas por arco inhibidor
        this.E = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Vector de sensibilizado
        this.estados = 16;
        this.transiciones = 15;
        this.sensitizedTime = new LocalTime []{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        for (int i = 0; i < transiciones; i++){
            this.sensitizedTime[i] = LocalTime.now();
        }//inicializa los contadores de tiempo como si todas estuvisen sensibilizadas al empezar (?

        this.minTimeArrival = 25;
        this.minTimeSrv1 = 50;
        this.minTimeSrv2 = 50;

    }


    public int isPos (int[] index) {

        String M_name[] = new String[]{"Active", "Active_2", "CPU_buffer", "CPU_buffer 2", "CPU_ON", "CPU_ON_2", "Idle", "Idle_2", "P0", "P1", "P13", "P6", "Power_up", "Power_up_2", "Stand_by", "Stand_by_2"};

        // Calculo E
        for (int m = 0; m < transiciones; m++) {
            this.E[m] = 1;

            for (int n = 0; n < estados; n++) {
                if (M[n] - Ineg[n][m] < 0) {
                    E[m] = 0;
                    break;
                }
            }
        }
        // TODO: COMPROBAR SI ESTA BIEN ACA
        // Limitacion de generacion de datos (T0)
        if (packetCounter == dataNumber)
            E[0] = 0;
        if (M[2] >= 10)
            E[5] = 0;
        if (M[3] >= 10)
            E[13] = 0;

        // System.out.println("E: \n");
        //printArray(E);

        int temp;
        int[] aux = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        aux = getSensitized();
        int []oldSens = new int [] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < transiciones; i++ ){
            oldSens[i] = aux[i];
        }
        for (int m = 0; m < transiciones; m++) {
            if (aux[m] * index[m] > 0) aux[m] = 1; // sigma and Ex
            else aux[m] = 0; // Si no pongo el else, quedan los unos de la operacion anterior
        }



        //Calculo B
        /*for (int m = 0; m < transiciones; m++) {
            B[m] = 0;
            for (int n = 0; n < estados; n++) {   // Si algun numero del nuevo vector de marcado es = 1, no puedo dispararla
                //temp = H[m][i] * Q[i];    // Sumo para obtener el nuevo vector de desensibilizado
                temp = H[m][n] * M[n];
                B[m] = B[m] + temp; // B = 0 -> no se puede :(
            }
            if (B[m] == 0) {
                B[m] = 1;
            } else {
                B[m] = 0;
            }
        }

        // System.out.println("H x m: \n");
        // printArray(B);

        for (int m = 0; m < transiciones; m++) {
            if (B[m] * E[m] > 0) aux[m] = 1; // B and E
            if (aux[m] * index[m] > 0) aux[m] = 1; // sigma and Ex
            else aux[m] = 0; // Si no pongo el else, quedan los unos de la operacion anterior
        }*/



        // System.out.println("sigma and Ex: \n");
        //printArray(aux);

        int zeroCounter = 0; // Esto es para ver que lo que quiero y puedo disparar sea diferente de 0
        for (int m = 0; m < transiciones; m++) {
            if (aux[m] != 0) zeroCounter++;
        }
        if (zeroCounter == 0)
            return -1;

        // I * aux  (n x m * m x 1 = n x 1)

        int[] aux2 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int n = 0; n < estados; n++) {
            for (int m = 0; m < transiciones; m++) {
                temp = I[n][m] * aux[m];
                aux2[n] = aux2[n] + temp;
            }
        }

        int[] mPrima = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};




        if (print)
            System.out.println ("Nuevo marcado: \n");
            for (int n = 0; n < estados; n++) {   // Si algun numero del nuevo vector de marcado es negativo, no puedo dispararla
                mPrima[n] = M[n] + aux2[n];    // Sumo para obtener el nuevo vector de marcado
                if (print)
                    System.out.println (mPrima[n] + " " + M_name[n] + "\n");
                if (mPrima[n] < 0) {
                    return -1;
                }
        }


        //inserte aqui funcion magica para actualizar los timestamps de transiciones que se sensibilizaron (tendria que comparar
        // sensiblizado anterior con el actual)


        LocalTime shootTime = LocalTime.now();
        LocalTime transitionTime =  null;
        for(int i = 0; i < transiciones; i++){
            if(index[i] > 0){
                if (i == 0 || i == 3 || i == 4) {
                    switch (i){
                        case 0:
                            transitionTime = sensitizedTime[i].plus(minTimeArrival, MILLIS);
                            break;
                        case 3:
                            transitionTime = sensitizedTime[i].plus(minTimeSrv1, MILLIS);
                            break;
                        case 4:
                            transitionTime = sensitizedTime[i].plus(minTimeSrv2, MILLIS);
                            break;
                    }
                    //transitionTime = sensitizedTime[i].plus(minTime, MILLIS);
                    if ( shootTime.isAfter(transitionTime) || shootTime.equals(transitionTime)) { //si el tiempo actual es mayor que el de sensibilizado + minTime
                        sensitizedTime[i] = LocalTime.now(); //actualizo el tiempo de sensibilizado "para que vuelva a 0" (?
                    }
                    else {
                        System.out.println("Quise disparar T" + i + " y estoy fuera del intervalo de tiempo\n");
                        //System.out.println("shootTime: " + shootTime.toString() + "\n" + "TransitionTime: " + transitionTime.toString() + "\n");
                        //return false; //en realidad aca tendria que mandar la diferencia de tiempo que tiene que dormir
                        return (int)(MILLIS.between(shootTime, transitionTime));
                    }
                }
            }

        }

        this.M = mPrima;
        if (index[0] == 1) {
            packetCounter++;
        }

        updateTimeStamps(oldSens); //le mando el vector de sensiblizado del marcado anterior

        return 0;
    }


    public void updateTimeStamps(int [] oldSens){

        for (int m = 0; m < transiciones; m++) {
            this.E[m] = 1;

            for (int n = 0; n < estados; n++) {
                if (M[n] - Ineg[n][m] < 0) {
                    E[m] = 0;
                    break;
                }
            }
        }
        // TODO: COMPROBAR SI ESTA BIEN ACA
        // Limitacion de generacion de datos (T0)
        if (packetCounter == dataNumber)
            E[0] = 0;
        if (M[2] >= 10)
            E[5] = 0;
        if (M[3] >= 10)
            E[13] = 0;



        int [] newSens = getSensitized();
        System.out.println("Viejo sensiblizado: ");
        printArray(oldSens);
        System.out.println("Nuevo sensiblizado: ");
        printArray(newSens);
        for (int i = 0; i < transiciones; i++){
            if( (newSens[i] > 0) && (newSens[i] != oldSens [i]) ){
                sensitizedTime[i] = LocalTime.now();
            }
        }
    }


    public void printArray (int [] array){
        System.out.print("{ ");
        for(int i : array){
            System.out.print( i + " ");
        }
        System.out.print("}\n");
    }

    public boolean isMarked (int index) {
        return ((this.M[index] != 0)); // Devuelve false si no hay nada en esa plaza y viceversa
    }

    public int[] getMarkVector () {
        return this.M;
    }


    public int[] getSensitized () {
        int temp;
        int[] aux = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Calculo B
        for (int m = 0; m < transiciones; m++) {
            B[m] = 0;
            for (int n = 0; n < estados; n++) {   // Si algun numero del nuevo vector de marcado es = 1, no puedo dispararla
                //temp = H[m][i] * Q[i];    // Sumo para obtener el nuevo vector de desensibilizado
                temp = H[m][n] * M[n];
                B[m] = B[m] + temp; // B = 0 -> no se puede :(
            }
            if (B[m] == 0) {
                B[m] = 1;
            } else {
                B[m] = 0;
            }
        }

        for (int m = 0; m < transiciones; m++) {
            if (B[m] * E[m] > 0) aux[m] = 1; // B and E
            else aux[m] = 0; // Si no pongo el else, quedan los unos de la operacion anterior
        }
        return aux;
    }

    public boolean ifEnd () {
        int[] Minitial = new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1};
        for (int n = 0; n < estados; n++) {
            if (M[n] != Minitial[n])
                return false;
        }
        if (packetCounter == dataNumber) {
            return true;
        }
        return false;
    }


}


