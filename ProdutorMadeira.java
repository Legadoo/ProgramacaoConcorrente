package trabbim.com;

public class ProdutorMadeira extends Thread {
    private final Armazem armazem;
    private final int id;

    public ProdutorMadeira(Armazem armazem, int id) {
        this.armazem = armazem;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Tempo de espera para produção
                Recurso recurso = new Recurso("Madeira");
                armazem.adicionarRecurso(recurso);
                System.out.println("Produtor de Madeira " + id + " adicionou recurso: " + recurso.getTipo());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
