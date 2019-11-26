package programas;

import visao.FXMLDocumentController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class sintatico {

    private Fila sintaticoST;
    private final Pilha p = new Pilha();
    private Fila fila;
    private final funcoes funk;
    private final String[][] comandos = new String[23][37];
    private final String[][] legenda = new String[48][13];
    private String[][] mtokens;
    private String[] vetorFila;
    private boolean tudoOK = false;

    private FXMLDocumentController controller;

    public void setController(FXMLDocumentController controller1) {
        this.controller = controller1;
    }

    public sintatico() {
        this.funk = new funcoes();
    }

    public sintatico(String[] filaST) throws IOException {
        this.funk = new funcoes();
        this.vetorFila = funk.limpeza(filaST);
        this.fila = new Fila((vetorFila.length / 4) + 5);
        this.mtokens = funk.ltoken();
    }

    private void coleta(int passo, int tipo, String file) throws IOException {
        String[] linhas = null;
        String linha;
        int j = 0;
        switch (passo) {
            case 1:
                try (BufferedReader br = new BufferedReader(new InputStreamReader(funk.local(file)))) {
                    while ((linha = br.readLine()) != null) {
                        linhas = linha.split(";");
                        for (int i = 0; i < linhas.length; i++) {
                            if (tipo == 1) {
                                legenda[j][i] = linhas[i];
                            }
                            if (tipo == 2) {
                                comandos[j][i] = linhas[i];
                            }
                        }
                        j++;
                    }
                    br.close();
                }
                break;
            case 2:
                
                for (int i = 0; i < vetorFila.length; i = i + 4) {
                        fila.insereFila(vetorFila[i]);
                }
                fila.insereFila("-1");
                break;
        }

    }

    private void preparacao() throws IOException {
        coleta(1, 1, "legenda.csv");
        coleta(1, 2, "comandos.csv");
        coleta(2, 0, "insereFila");
    }

    private String comparador(String palavra) {
        for (int i = 0; i < 33; i++) {
            if (palavra.compareTo(mtokens[i][1]) == 0) {
                return mtokens[i][0];
            }
        }
        return palavra;
    }

    public void analisador() throws IOException {

        preparacao();
        p.empilhar("$");
        p.empilhar("<codigo>");

        String topFila = fila.consultaFila();
        String topPilha = p.exibeUltimoValor().toString();
        String comandoI, comandoJ, comandoP;
        int posI = 0, posJ = 0, count = 0;

        do {
            if (topFila.compareTo("-1") == 0 && topPilha.compareTo("$") == 0) {
                tudoOK = true;
                controller.getTxtSin().appendText("------------------------------------- \n");
                controller.getTxtSin().appendText("Analise concluida com Sucesso!\n");
                controller.getTxtSin().appendText("-------------------------------------\n");
                break;
            } else if (topFila.compareTo("-1") == 0 && topPilha.compareTo("$") != 0) {
                controller.getTxtSin().appendText("-------------------------------------\n");
                controller.getTxtSin().appendText("Erro!");
                controller.getTxtSin().appendText("Erro na linha " + vetorFila[count - 3]);
                controller.getTxtSin().appendText("Coluna: " + vetorFila[count - 2]);
                controller.getTxtSin().appendText("Codigo terminou sem a devida finalização! \n");
                controller.getTxtSin().appendText("-------------------------------------\n");
                break;
            }
            controller.getTxtSin().appendText("--------------------------\n");
            controller.getTxtSin().appendText("Analisando Tokens\n");
            if (topFila.compareTo(topPilha) == 0) {
                controller.getTxtSin().appendText("Top Fila e top Pilha são iguais, removendo os dois!\n");

                count = count + 4;
                Object tirouPilha = p.desempilhar();
                controller.getTxtSin().appendText("Removido: " + tirouPilha + "\n");
                controller.getTxtSin().appendText("Removido da Pilha: " + tirouPilha + "\n");
                controller.getTxtSin().appendText("Removido da Fila: " + fila.removeFila() + "\n");

            } else {
                controller.getTxtSin().appendText("TopFila e TopPilha são diferentes.\n");

                controller.getTxtSin().appendText("Procurando se " + topPilha + " Tem nos comandos.\n");
                for (int i = 0; i < 23; i++) {
                    comandoP = comandos[i][0];
                    if (comandoP.compareTo(topPilha) == 0) {
                        posI = i;
                    }
                }
                controller.getTxtSin().appendText("Procurando se " + topFila + " é um token relacionado ao comando.\n");
                for (int i = 0; i < 37; i++) {
                    comandoP = comandos[0][i];
                    if (comandoP.compareTo(topFila) == 0) {
                        posJ = i;
                    }
                }

                controller.getTxtSin().appendText("Analisando se comando existe e se tem token relacionado!\n");

                if (posI != 0 && posJ != 0) {
                    controller.getTxtSin().appendText("Token e comando relacionados!\n");
                    controller.getTxtSin().appendText("PosI: " + posI + " PosJ: " + posJ + "\n");
                    comandoI = comandos[posI][posJ];
                    controller.getTxtSin().appendText("Comando encontrado: " + comandoI + "\n");
                    controller.getTxtSin().appendText("Procurando a legenda...\n");
                    for (int i = 0; i < 48; i++) {
                        comandoP = legenda[i][0];
                        if (comandoP.compareTo(comandoI) == 0) {
                            posI = i;
                        }
                    }
                    Object desempilhar = p.desempilhar();
                    controller.getTxtSin().appendText("Legenda na linha: " + posI + "\n");
                    controller.getTxtSin().appendText("Adicionando na pilha os novos valores \n");
                    for (int i = 12; i > 1; i--) {
                        comandoP = legenda[posI][i];
                        if (comandoP.compareTo("-") != 0) {
                            p.empilhar(legenda[posI][i]);
                        }
                    }
                } else {

                    if (topPilha.compareTo("X") == 0) {
                        comandoP = (String) p.desempilhar();
                    } else {
                        controller.getTxtSin().appendText("\n");
                        controller.getTxtSin().appendText("----------------------------\n");
                        controller.getTxtSin().appendText("Erro!\n");
                        controller.getTxtSin().appendText("Erro na linha " + vetorFila[count - 3] + "\n");
                        controller.getTxtSin().appendText("Coluna: " + vetorFila[count - 2] + "\n");
                        controller.getTxtSin().appendText("Era esperado: " + comparador(topPilha) + "\n");
                        controller.getTxtSin().appendText("Valor encontrado: " + comparador(topFila) + "\n");
                        controller.getTxtSin().appendText("----------------------------\n");
                        controller.getTxtSin().appendText("\n");
                        break;
                    }

                }
                posI = 0;
                posJ = 0;
            }

            topFila = fila.consultaFila();

            topPilha = p.exibeUltimoValor().toString();
            controller.getTxtSin().appendText("-------------------------------\n");
            controller.getTxtSin().appendText("Proximos a serem analisados!\n");
            controller.getTxtSin().appendText("Topo da pilha: " + topPilha + "\n");
            controller.getTxtSin().appendText("Topo da fila: " + topFila + "\n");
        } while (true);
    }

    public boolean getFinal() {
        return tudoOK;
    }
}
