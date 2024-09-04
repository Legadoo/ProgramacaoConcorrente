package trabbim.com;


public class Recurso {
    private final String tipo;

    public Recurso(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}



// Código original comentado
// Main para testar a simulação
// public class Main {
//     public static void main(String[] args) {
//         int capacidadeArmazem = 10;
//         BlockingQueue<Recurso> fila = new PriorityBlockingQueue<>();
//         Semaphore armazemCheio = new Semaphore(capacidadeArmazem);

//         Produtor produtorMadeira = new ProdutorMadeira(fila, armazemCheio);
//         Consumidor consumidorConstrutor = new ConsumidorConstrutor(fila);

//         Thread threadProdutor = new Thread(produtorMadeira);
//         Thread threadConsumidor = new Thread(consumidorConstrutor);

//         threadProdutor.start();
//         threadConsumidor.start();
//     }
// }
