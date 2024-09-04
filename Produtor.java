package trabbim.com;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Produtor extends Thread {
    private final BlockingQueue<Integer> buffer;
    private final Armazem armazem;
    private final int id;
    Consumidor(BlockingQueue<Integer> buffer) {
        this.buffer = buffer;
    }

    public Produtor(Armazem armazem, int id) {
        this.armazem = armazem;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Tempo de espera para produção
                Recurso recurso = new Recurso("Tipo" + id);
                armazem.adicionarRecurso(recurso);
                System.out.println("Produtor " + id + " adicionou recurso: " + recurso.getTipo());
                for (int i = 0; i < 10; i++) {
                    Integer item = buffer.take();
                    System.out.println("Consumido: " + item);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
