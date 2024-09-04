package trabbim.com;

public class Consumidor extends Thread {
    private final Armazem armazem;
    private final int id;
    private final String tipoRecurso; // Tipo de recurso que o consumidor consome

    public Consumidor(Armazem armazem, int id, String tipoRecurso) {
        this.armazem = armazem;
        this.id = id;
        this.tipoRecurso = tipoRecurso;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1500); // Tempo de espera para consumo

                // Tentar retirar um recurso do armazém
                Recurso recurso = armazem.retirarRecurso();

                // Verificar se o recurso é do tipo correto
                if (recurso != null && recurso.getTipo().equals(tipoRecurso)) {
                    System.out.println("Consumidor " + id + " retirou recurso: " + tipoRecurso);
                } else {
                    // Caso o recurso não corresponda ao tipo desejado ou o armazém esteja vazio
                    if (recurso == null) {
                        System.out.println("Consumidor " + id + " tentou retirar recurso, mas o armazém está vazio.");
                    } else {
                        System.out.println("Consumidor " + id + " tentou retirar recurso, mas o tipo não corresponde. Tipo esperado: " + tipoRecurso + ", Tipo recebido: " + recurso.getTipo());
                        // Recolocar o recurso de volta no armazém
                        armazem.adicionarRecurso(recurso);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
