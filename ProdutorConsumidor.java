package trabbim.com;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProdutorConsumidor {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(5);

        long tempoInicial = System.nanoTime();

        Thread produtor = new Thread(new Produtor(buffer));
        Thread consumidor = new Thread(new Consumidor(buffer));

        produtor.start();
        consumidor.start();
        produtor.join();
        consumidor.join();

        long tempoFinal = System.nanoTime();
        long tempoExecucao = tempoFinal - tempoInicial;

        System.out.println("Tempo de execução: " + (tempoExecucao / 1_000_000) + " ms");
    }

    static class Produtor implements Runnable {
        private final BlockingQueue<Integer> buffer;

        Produtor(BlockingQueue<Integer> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.put(i);
                    System.out.println("Produzido: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumidor implements Runnable {
        private final BlockingQueue<Integer> buffer;

        Consumidor(BlockingQueue<Integer> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    Integer item = buffer.take();
                    System.out.println("Consumido: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
