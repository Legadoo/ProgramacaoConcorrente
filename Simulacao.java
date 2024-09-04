package trabbim.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

// Classe de simulação principal
public class Simulacao extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Texture lenhadorIdle, lenhadorAtivo, mineiroIdle, mineiroAtivo;
    private Texture consumidor1Sprite, consumidor2Sprite;
    private Texture backgroundTexture; // Textura do plano de fundo
    private TextureRegion[] lenhadorAnimacao, mineiroAnimacao;
    private Animation<TextureRegion> animacaoLenhador, animacaoMineiro;
    private float tempoAnimacao = 0;
    private float progressoLenhador = 0;
    private float progressoMineiro = 0;
    private boolean isLenhadorAtivo = false;
    private boolean isMineiroAtivo = false;
    private boolean armazemBloqueado = false;
    private Random random;
    private float aceleracaoLenhador = 1.0f;
    private float aceleracaoMineiro = 1.0f;
    private Color lenhadorBarColor = Color.GREEN;
    private Color mineiroBarColor = Color.BLUE;
    private float recursoExpiracao = 0;
    private boolean lenhadorFalha = false;
    private boolean mineiroFalha = false;

    // Variáveis para o armazém e consumidores
    private final Armazem armazem = new Armazem(10);
    private int quantidadeProduzida = 0;
    private float taxaProducao = 0;
    private int producaoRestanteLenhador = 10; // Número de recursos restantes a serem produzidos pelo lenhador
    private int producaoRestanteMineiro = 10;  // Número de recursos restantes a serem produzidos pelo mineiro

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(1080, 720);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        lenhadorIdle = new Texture("lenhador_idle.png");
        lenhadorAtivo = new Texture("lenhador_ativo.png");
        mineiroIdle = new Texture("mineiro_idle.png");
        mineiroAtivo = new Texture("mineiro_ativo.png");
        consumidor1Sprite = new Texture("Consumidor1.png");
        consumidor2Sprite = new Texture("Consumidor2.png");
        backgroundTexture = new Texture("background.png"); // Carregar a textura do plano de fundo

        // Criar animações para lenhador
        lenhadorAnimacao = new TextureRegion[2];
        lenhadorAnimacao[0] = new TextureRegion(lenhadorIdle); // Estado idle
        lenhadorAnimacao[1] = new TextureRegion(lenhadorAtivo); // Estado ativo
        animacaoLenhador = new Animation<>(0.1f, lenhadorAnimacao);

        // Criar animações para mineiro
        mineiroAnimacao = new TextureRegion[2];
        mineiroAnimacao[0] = new TextureRegion(mineiroIdle); // Estado idle
        mineiroAnimacao[1] = new TextureRegion(mineiroAtivo); // Estado ativo
        animacaoMineiro = new Animation<>(0.1f, mineiroAnimacao);

        random = new Random();

        // Iniciar produtores e consumidores
        new ProdutorMadeira(armazem, 1).start();
        new ProdutorPedra(armazem, 2).start();
        new Consumidor(armazem, 1, "Madeira").start(); // Passar o tipo de recurso para o consumidor 1
        new Consumidor(armazem, 2, "Pedra").start();   // Passar o tipo de recurso para o consumidor 2
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // Desenhar o plano de fundo
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Atualizar e renderizar animações
        tempoAnimacao += Gdx.graphics.getDeltaTime();
        TextureRegion frameLenhador = animacaoLenhador.getKeyFrame(tempoAnimacao, isLenhadorAtivo);
        TextureRegion frameMineiro = animacaoMineiro.getKeyFrame(tempoAnimacao, isMineiroAtivo);

        // Renderização dos produtores (lenhador e mineiro)
        batch.draw(frameLenhador, 100, 350); // Posição ajustada para o lenhador
        batch.draw(frameMineiro, 700, 350); // Posição ajustada para o mineiro

        // Renderização dos consumidores (consumidor1 e consumidor2)
        batch.draw(consumidor1Sprite, 300, 200); // Posição ajustada para o consumidor 1
        batch.draw(consumidor2Sprite, 800, 200); // Posição ajustada para o consumidor 2

        batch.end();

        // Renderização das barras de progresso
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Barra de progresso do lenhador
        shapeRenderer.setColor(lenhadorBarColor);
        shapeRenderer.rect(30, 190, progressoLenhador, 10);

        // Barra de progresso do mineiro
        shapeRenderer.setColor(mineiroBarColor);
        shapeRenderer.rect(720, 190, progressoMineiro, 10);

        shapeRenderer.end();

        // Desenhar labels com informações
        batch.begin();
        font.draw(batch, "Quantidade Produzida: " + quantidadeProduzida, 10, Gdx.graphics.getHeight() - 30);
        font.draw(batch, "Recursos no Armazém: " + armazem.getTamanho(), 10, Gdx.graphics.getHeight() - 60);
        font.draw(batch, "Taxa de Produção: " + taxaProducao, 10, Gdx.graphics.getHeight() - 90);
        font.draw(batch, "Produção Restante Lenhador: " + producaoRestanteLenhador, 10, Gdx.graphics.getHeight() - 120);
        font.draw(batch, "Produção Restante Mineiro: " + producaoRestanteMineiro, 10, Gdx.graphics.getHeight() - 150);
        font.draw(batch, "Produzindo: " + (isLenhadorAtivo ? "Lenhador" : (isMineiroAtivo ? "Mineiro" : "Nenhum")), 10, Gdx.graphics.getHeight() - 180);
        batch.end();

        // Atualização do progresso com aceleração, falhas e efeitos visuais
        if (isLenhadorAtivo) {
            progressoLenhador += 0.5 * aceleracaoLenhador;
            if (progressoLenhador >= 100) {
                progressoLenhador = 0;
                isLenhadorAtivo = false;
                aceleracaoLenhador = 1.0f;
                lenhadorBarColor = Color.GREEN;

                // Atualizar quantidade de recursos no armazém e produção
                if (!armazemBloqueado && producaoRestanteLenhador > 0) {
                    try {
                        armazem.adicionarRecurso(new Recurso("Madeira"));
                        quantidadeProduzida += 1;
                        taxaProducao = 1.0f; // Atualizar taxa de produção
                        producaoRestanteLenhador--; // Decrementar recursos restantes
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Lenhador produziu um recurso. Recursos restantes: " + producaoRestanteLenhador);
            }
        } else if (producaoRestanteLenhador > 0) {
            // Ativar o lenhador para continuar a produção
            isLenhadorAtivo = true;
            System.out.println("Lenhador está ativo.");
        }

        if (isMineiroAtivo) {
            progressoMineiro += 0.5 * aceleracaoMineiro;
            if (progressoMineiro >= 100) {
                progressoMineiro = 0;
                isMineiroAtivo = false;
                aceleracaoMineiro = 1.0f;
                mineiroBarColor = Color.BLUE;

                // Atualizar quantidade de recursos no armazém e produção
                if (!armazemBloqueado && producaoRestanteMineiro > 0) {
                    try {
                        armazem.adicionarRecurso(new Recurso("Pedra"));
                        quantidadeProduzida += 1;
                        taxaProducao = 1.0f; // Atualizar taxa de produção
                        producaoRestanteMineiro--; // Decrementar recursos restantes
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Mineiro produziu um recurso. Recursos restantes: " + producaoRestanteMineiro);
            }
        } else if (producaoRestanteMineiro > 0) {
            // Ativar o mineiro para continuar a produção
            isMineiroAtivo = true;
            System.out.println("Mineiro está ativo.");
        }

        // Evento aleatório: Aceleração temporária da produção
        if (random.nextFloat() < 0.01) { // 1% de chance a cada frame
            aceleracaoLenhador = 2.0f;
            aceleracaoMineiro = 2.0f;
            lenhadorBarColor = Color.YELLOW;
            mineiroBarColor = Color.YELLOW;
            System.out.println("Evento aleatório: Aceleração temporária ativada.");
        }

        // Simulação de falhas e efeitos visuais
        if (lenhadorFalha) {
            // Mostrar efeito de falha para o lenhador
            lenhadorBarColor = Color.RED;
            System.out.println("Lenhador falhou na produção.");
        }
        if (mineiroFalha) {
            // Mostrar efeito de falha para o mineiro
            mineiroBarColor = Color.RED;
            System.out.println("Mineiro falhou na produção.");
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        lenhadorIdle.dispose();
        lenhadorAtivo.dispose();
        mineiroIdle.dispose();
        mineiroAtivo.dispose();
        consumidor1Sprite.dispose();
        consumidor2Sprite.dispose();
        backgroundTexture.dispose(); // Liberar recursos da textura do plano de fundo
    }
}
