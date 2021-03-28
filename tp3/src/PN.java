public class PN {
    private int[] M;
    private int[] B;
    private int[][] I;
    private int[][] Ipos;
    private int[][] Ineg;
    private int[][] H;
    private int[] E;
    private int estados; //N
    private int transiciones; //M
    private static boolean print = true;

    /*
    // N = cant de estados , M = cant de transiciones
    public PN(int[] m, int[] b, int[] q, int[][] i, int[][] h, int[] E) {
        this.M = m; // Vector de marcado inicial // (N x 1)
        this.B = b; // Si B[i] = 1, la transicion esta desensibilizada (M x 1)
        this.I = i; // Matriz de incidencia (N x M)
        this.H = h; // Matriz de inhibicion (M x N)
    }
     */

    public PN () {
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
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // arrival_rate
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // Power_down_threshold
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // Power_down_2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // Service_rate
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // Service_rate_2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T1
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T12
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T13
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T14
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T2
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T5
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T6
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T7
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T8
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},        // T9
        };


        this.B = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Vector de transiciones desensibilizadas por arco inhibidor
        this.E = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Vector de sensibilizado
        this.estados = 16;
        this.transiciones = 15;

    }


    public boolean isPos (int[] index) {

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
        if (M[2] >= 10)
            E[5] = 0;
        if (M[3] >= 10)
            E[13] = 0;

        // System.out.println("E: \n");
        //printArray(E);

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

        // System.out.println("H x m: \n");
        // printArray(B);

        for (int m = 0; m < transiciones; m++) {
            if (B[m] * E[m] > 0) aux[m] = 1; // B and E
            if (aux[m] * index[m] > 0) aux[m] = 1; // sigma and Ex
            else aux[m] = 0; // Si no pongo el else, quedan los unos de la operacion anterior
        }

        // System.out.println("sigma and Ex: \n");
        //printArray(aux);

        int zeroCounter = 0; // Esto es para ver que lo que quiero y puedo disparar sea diferente de 0
        for (int m = 0; m < transiciones; m++) {
            if (aux[m] != 0) zeroCounter++;
        }
        if (zeroCounter == 0)
            return false;

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
                return false;
            }
        }

        this.M = mPrima;
        return true;
    }

    public boolean isMarked (int index) {
        return ((this.M[index] != 0)); // Devuelve false si no hay nada en esa plaza y viceversa
    }

    public int[] getMarkVector () {
        return this.M;
    }


    public int[] getSensitized () {
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
        if (M[2] >= 10)
            E[5] = 0;
        if (M[3] >= 10)
            E[13] = 0;
        //return  E;


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

        // System.out.println("H x m: \n");
        // printArray(B);

        for (int m = 0; m < transiciones; m++) {
            if (B[m] * E[m] > 0) aux[m] = 1; // B and E
            else aux[m] = 0; // Si no pongo el else, quedan los unos de la operacion anterior
        }
        return aux;
    }


}