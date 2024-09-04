package trabbim.com;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class ProdutorPedra extends Thread {
    private final Armazem armazem;
    private final int id;

    public ProdutorPedra(Armazem armazem, int id) {
        this.armazem = armazem;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Tempo de espera para produção
                Recurso recurso = new Recurso("Pedra");
                armazem.adicionarRecurso(recurso);
                System.out.println("Produtor de Pedra " + id + " adicionou recurso: " + recurso.getTipo());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
