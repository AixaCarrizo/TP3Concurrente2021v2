import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class CpuBuffer {

    private Queue<String> cola;

    public CpuBuffer () {

        cola = new ConcurrentLinkedQueue<> ();
    }

    public void add (String dato) {
        cola.add (dato);
    }

    public String remove () {
        return cola.remove ();
    }

    public int size () {
        return cola.size ();
    }
}