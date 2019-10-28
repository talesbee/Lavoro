package programas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import visao.FXMLDocumentController;

public class semantico {

    private Fila tokem;
    private String[][] variavel = new String[50][2];
    private String[][] oldVariavel = new String[50][2];
    private int k = 0;
    private FXMLDocumentController controller;
    private funcoes funk = new funcoes();
    private boolean tudoOk = true;
    private boolean divisao = false;

    public void setController(FXMLDocumentController controller1) {
        this.controller = controller1;
    }

    public boolean getResult() {
        return tudoOk;
    }

    public semantico() {

    }

    private void error(String linha, String coluna) {
        controller.getTxtSem().appendText("Erro!\n");
        controller.getTxtSem().appendText("Erro na linha: '" + linha + "' Coluna: '" + coluna + "\n");
    }

    public semantico(Fila tokens) {
        this.tokem = tokens;
        for (int i = 0; i < variavel.length; i++) {
            variavel[i][0] = "-";
            variavel[i][1] = "-";
            oldVariavel[i][0] = "-";
            oldVariavel[i][1] = "-";
        }
    }

    private int localVariavel(String vari) {
        for (int i = 0; i < variavel.length; i++) {
            if (variavel[i][0].compareTo(vari) == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean variavelExiste(String vari) {
        for (String[] variavel1 : variavel) {
            if (variavel1[0].compareTo(vari) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean operador(String valor) {
        String[] op = {"t_mais", "t_mul", "t_menos", "t_div"};
        for (String op1 : op) {
            if (op1.compareTo(valor) == 0) {
                if (op1.compareTo("t_div") == 0) {
                    divisao = true;
                }

                return true;
            }
        }
        return false;
    }

    private boolean isNumeroRegexp(String texto) {
        Pattern pat = Pattern.compile("[0-9]+");
        Matcher mat = pat.matcher(texto);
        return mat.matches();
    }

    private int declaracao = 0, localVari = -1, localVari2 = -1, declaracao2 = 0;

    public void analisador() {
        String[] vetor = funk.limpeza(tokem.exibeFila());

        for (int i = 0; i < vetor.length / 4; i++) {
            //Declaração de variável:
            if (vetor[4 * i].compareTo("t_vari") == 0) {
                controller.getTxtSem().appendText("Variável declarada: '");
                declaracao = 1;
            } else if (vetor[4 * i].compareTo("t_ter") == 0 && declaracao == 1) {
                declaracao = 2;
            } else if (vetor[4 * i].compareTo("id") == 0 && declaracao == 2) {
                controller.getTxtSem().appendText(vetor[(4 * i) + 3] + "'\n");
                if (!variavelExiste(vetor[(4 * i) + 3])) {
                    variavel[k][0] = vetor[(4 * i) + 3];
                    oldVariavel[k][0] = vetor[(4 * i) + 3];
                    k++;
                    declaracao = 0;
                    controller.getTxtSem().appendText("\n");
                } else {
                    error(vetor[(i * 4) + 1], vetor[(i * 4) + 2]);
                    controller.getTxtSem().appendText("Variável já foi declarada!\n");
                    tudoOk = false;
                    break;
                }
                //Printando variável:
            } else if (vetor[4 * i].compareTo("t_escr") == 0) {
                controller.getTxtSem().appendText("Escrevendo id, ignorando declaração!\n");
                declaracao = 3;
            } else if (vetor[4 * i].compareTo("id") == 0 && declaracao == 3) {
                controller.getTxtSem().appendText("Id escrito com sucesso! id: '" + vetor[(4 * i) + 3] + "'\n");
                controller.getTxtSem().appendText("\n");
                declaracao = 0;
                //Adicionando valor a variavel:
            } else if (vetor[4 * i].compareTo("id") == 0 && declaracao == 0) {
                controller.getTxtSem().appendText("Esperando declaração de valor a uma variável -> id: '" + vetor[(4 * i) + 3] + "'\n");
                localVari = localVariavel(vetor[(4 * i) + 3]);
                if (localVari == -1) {
                    error(vetor[(i * 4) + 1], vetor[(i * 4) + 2]);
                    controller.getTxtSem().appendText("Variavel não declarada!\n");
                    tudoOk = false;
                    break;
                } else {
                    declaracao = 4;
                }
            } else if (vetor[4 * i].compareTo("t_p") == 0 && declaracao == 4) {
                declaracao = 5;
                //Verificando o tipo de atribuição:

            } else if (declaracao == 5) {
                //Simples:
                if (!operador(vetor[4 * (i + 1)])) {
                    //É um int:
                    if (isNumeroRegexp(vetor[(4 * i) + 3])) {
                        variavel[localVari][1] = vetor[(4 * i) + 3];
                        oldVariavel[localVari][1] = vetor[(4 * i) + 3];
                        controller.getTxtSem().appendText("Valor atribuido: '" + variavel[localVari][1] + "'.\n");
                        controller.getTxtSem().appendText("\n");
                        //Não é um int:    
                    } else {
                        localVari2 = localVariavel(vetor[(4 * i) + 3]);
                        if (localVari2 != -1) {
                            variavel[localVari][1] = variavel[localVari2][1];
                            controller.getTxtSem().appendText("Valor da variável '" + variavel[localVari2][0] + "' atribuido a variável '" + variavel[localVari][0] + "'.\n");
                            controller.getTxtSem().appendText("Novo valor da variavel '" + variavel[localVari][0] + "' é: '" + variavel[localVari][1] + "'.\n");
                            controller.getTxtSem().appendText("\n");
                        } else {
                            error(vetor[(i * 4) + 1], vetor[(i * 4) + 2]);
                            controller.getTxtSem().appendText("Lavoro só aceita variavel tipo int!\n");
                            tudoOk = false;
                            break;
                        }

                    }
                    declaracao = 0;

                    //Composta
                } else {
                    controller.getTxtSem().appendText("Atribuição de valor composta!\n");
                    controller.getTxtSem().appendText("Primeiro valor: '" + vetor[(4 * i) + 3] + "'\n");
                    //É um int
                    if (isNumeroRegexp(vetor[(4 * i) + 3])) {
                        controller.getTxtSem().appendText("Valor colocado no registrador temporario: '" + vetor[(4 * i) + 3] + "'.\n");
                        declaracao = 6;
                        //Não é um int:    
                    } else {
                        localVari2 = localVariavel(vetor[(4 * i) + 3]);
                        if (localVari2 != -1) {
                            controller.getTxtSem().appendText("Valor da variavel '" + variavel[localVari2][0] + "' colocado no registrador temporario: '" + variavel[localVari2][1] + "'.\n");
                            controller.getTxtSem().appendText("\n");
                        } else {
                            error(vetor[(i * 4) + 1], vetor[(i * 4) + 2]);
                            controller.getTxtSem().appendText("Lavoro só aceita variavel tipo int!\n");
                            tudoOk = false;
                            break;
                        }

                    }
                    declaracao = 6;
                }
            } else if (declaracao == 6) {
                if (vetor[4 * i].compareTo("t_div") == 0) {
                    divisao = true;
                }

                declaracao = 7;
            } else if (declaracao == 7) {
                controller.getTxtSem().appendText("Segundo valor: '" + vetor[(4 * i) + 3] + "'\n");
                //É um int
                if (isNumeroRegexp(vetor[(4 * i) + 3])) {
                    if (divisao && vetor[(4 * i) + 3].compareTo("0")==0){
                        error(vetor[(i * 4) + 1], vetor[(i * 4) + 2]);
                        controller.getTxtSem().appendText("Divisão por Zero!\n");
                        tudoOk = false;
                        break;
                    }
                    controller.getTxtSem().appendText("Valor colocado no registrador temporario: '" + vetor[(4 * i) + 3] + "'.\n");
                    declaracao = 0;
                    //Não é um int:    
                } else {
                    localVari2 = localVariavel(vetor[(4 * i) + 3]);
                    if (localVari2 != -1) {
                        controller.getTxtSem().appendText("Valor da variavel '" + variavel[localVari2][0] + "' colocado no registrador temporario: '" + variavel[localVari2][1] + "'.\n");
                        if (divisao && variavel[localVari2][1].compareTo("0")==0){
                            error(vetor[(i * 4) + 1], vetor[(i * 4) + 2]);
                            controller.getTxtSem().appendText("Divisão por Zero!\n");
                            tudoOk = false;
                            break;
                        }
                        controller.getTxtSem().appendText("\n");
                    } else {
                        error(vetor[(i * 4) + 1], vetor[(i * 4) + 2]);
                        controller.getTxtSem().appendText("Lavoro só aceita variavel tipo int!\n");
                        tudoOk = false;
                        break;
                    }
                    declaracao = 0;

                }
                divisao = false;
            }
            
            
        }
    }
}
