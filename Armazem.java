package trabbim.com;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Armazem {
    private final BlockingQueue<Recurso> fila;

    public Armazem(int capacidade) {
        fila = new LinkedBlockingQueue<>(capacidade);
    }

    public void adicionarRecurso(Recurso recurso) throws InterruptedException {
        fila.put(recurso);
    }

    public Recurso retirarRecurso() throws InterruptedException {
        return fila.take();
    }

    public int getTamanho() {
        return fila.size();
    }
}
