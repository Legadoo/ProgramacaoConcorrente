package trabbim.com;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;


import java.util.concurrent.LinkedBlockingQueue;


public class Main {
    public static void main(String[] args) {
        int capacidadeArmazem = 10;
        BlockingQueue<Recurso> fila = new LinkedBlockingQueue<>(capacidadeArmazem);
        Semaphore armazemCheio = new Semaphore(capacidadeArmazem);

        Armazem armazem = new Armazem(capacidadeArmazem); // Instância do Armazem

        ProdutorMadeira produtorMadeira = new ProdutorMadeira(armazem, 1);
        ProdutorPedra produtorPedra = new ProdutorPedra(armazem, 2);


        // Não irei usar por enquanto
        // Consumidor consumidorConstrutor = new ConsumidorConstrutor(armazem);
        // Consumidor consumidorFerreiro = new ConsumidorFerreiro(armazem);

        // Iniciar os threads dos produtores e consumidores
        Thread threadProdutorMadeira = new Thread(produtorMadeira);
        Thread threadProdutorPedra = new Thread(produtorPedra);
        // Thread threadConsumidorConstrutor = new Thread(consumidorConstrutor);
        // Thread threadConsumidorFerreiro = new Thread(consumidorFerreiro);

        threadProdutorMadeira.start();
        threadProdutorPedra.start();
        // threadConsumidorConstrutor.start();
        // threadConsumidorFerreiro.start();

        // Iniciar produtores
        new Produtor(armazem, 1).start();
        new Produtor(armazem, 2).start();

        // Iniciar consumidores com tipos de recurso diferentes
        new Consumidor(armazem, 1, "Tipo1").start();
        new Consumidor(armazem, 2, "Tipo2").start();

    }
}
