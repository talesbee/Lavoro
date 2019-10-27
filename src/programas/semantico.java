package programas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import visao.FXMLDocumentController;

public class semantico {

    private Fila tokem;
    private String[][] variavel = new String[50][2];
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
    private void error(String linha, String coluna){
        controller.getTxtSem().appendText("Erro!\n");
        controller.getTxtSem().appendText("Erro na linha: '" + linha + "' Coluna: '" + coluna + "\n");
    }

    public semantico(Fila tokens) {
        this.tokem = tokens;
        for (int i = 0; i < variavel.length; i++) {
            variavel[i][0] = "-";
            variavel[i][1] = "-";
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
    private boolean operador(String valor){
        String[] op = {"t_mais","t_mul","t_menos","t_div"};
        for(String op1 : op){
            if(op1.compareTo(valor)==0){
                if(op1.compareTo("t_div")==0)
                    divisao = true;
                
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

    private int declaracao = 0, localVari = -1, localVari2 = -1,declaracao2 = 0;

    public void analisador() {
        String[] vetor = funk.limpeza(tokem.exibeFila());

        //Verificando erros por declaração de variáveis
        for (int i = 0; i < vetor.length / 4; i++) {
            if (vetor[4 * i].compareTo("t_vari") == 0) {
                controller.getTxtSem().appendText("Variável declarada: '");
                declaracao++;
            } else if (vetor[4 * i].compareTo("t_ter") == 0 && declaracao == 1) {
                declaracao++;
            } else if (vetor[4 * i].compareTo("id") == 0 && declaracao == 2) {
                controller.getTxtSem().appendText(vetor[(4 * i) + 3] + "'\n");
                
                if(!variavelExiste(vetor[(4 * i) + 3])){
                    variavel[k][0] = vetor[(4 * i) + 3];
                    k++;
                    declaracao = 0;
                    controller.getTxtSem().appendText("\n");
                }else{
                    error(vetor[(i * 4) + 1],vetor[(i * 4) + 2]);
                    controller.getTxtSem().appendText("Variável já foi declarada!\n");
                    tudoOk = false;
                    break;
                }
                

            } else if (vetor[4 * i].compareTo("t_escr") == 0) {

                controller.getTxtSem().appendText("\n");
                controller.getTxtSem().appendText("Estão querendo escrever um id!\n");
                declaracao = 3;
            } else if (vetor[4 * i].compareTo("id") == 0 && declaracao == 3) {
                controller.getTxtSem().appendText("Id escrito com sucesso! id: '" + vetor[(4 * i) + 3] + "'\n");
                declaracao = 0;

            } else if (vetor[4 * i].compareTo("id") == 0 && declaracao == 0) {
                controller.getTxtSem().appendText("Esperando declaração de valor a uma variável -> id: '" + vetor[(4 * i) + 3] + "'\n");
                localVari = localVariavel(vetor[(4 * i) + 3]);
                if (localVari == -1) {
                    error(vetor[(i * 4) + 1],vetor[(i * 4) + 2]);
                    controller.getTxtSem().appendText("Variavel não declarada!\n");
                    tudoOk = false;
                    break;
                } else {
                    declaracao = 4;
                }
            } else if (vetor[4 * i].compareTo("t_p") == 0 && declaracao == 4) {
                declaracao = 5;
            } else if (vetor[4 * i].compareTo("id") == 0 && declaracao == 5) {
                controller.getTxtSem().appendText("Valor declarado foi: '" + vetor[(4 * i) + 3] + "'\n");
                if (isNumeroRegexp(vetor[(4 * i) + 3])) {
                    if(divisao){
                        if(Integer.parseInt(vetor[(4 * i) + 3])==0){
                            error(vetor[(i * 4) + 1],vetor[(i * 4) + 2]);
                            controller.getTxtSem().appendText("Divisão por Zero!\n");
                            tudoOk = false;
                            break;
                        }
                    }
                    variavel[localVari][1] = vetor[(4 * i) + 3];
                    declaracao = 0;
                    declaracao2 = 1;
                    divisao = false;
                    
                } else {
                    localVari2 = localVariavel(vetor[(4 * i) + 3]);
                    if(localVari2 != -1){
                        if(divisao){
                        if(Integer.parseInt(variavel[localVari2][1])==0){
                            error(vetor[(i * 4) + 1],vetor[(i * 4) + 2]);
                            controller.getTxtSem().appendText("Divisão por Zero!\n");
                            tudoOk = false;
                            break;
                        }
                    }
                        variavel[localVari][1] = variavel[localVari2][1];
                        controller.getTxtSem().appendText("Passando o valor da variavel '"+variavel[localVari2][0]+"' para a variavel '"+variavel[localVari][0]+"'! \n");
                        declaracao = 0;
                        declaracao2 = 1;
                    }else{
                        error(vetor[(i * 4) + 1],vetor[(i * 4) + 2]);
                        controller.getTxtSem().appendText("Erro de declaração, variáveis de Lavoro só aceitam valores tipo int! \n");
                        tudoOk = false;
                        break;
                    }    
                }

            }else if (declaracao2 == 1 && operador(vetor[4 * i])){
                controller.getTxtSem().appendText(" Operador: '"+vetor[(4 * i)+3]+"' \n");
                declaracao = 5;
            }else{
                declaracao2 = -1;
                localVari = -1;
            }
        }

        //Verificando erros por divisão por zero!
        
        
        
        
        
        
        if(tudoOk){
            controller.getTxtSem().appendText("\n");
            controller.getTxtSem().appendText("Variáveis declaradas ao longo do código: \n");
            int total = 1;
            for (String[] variavel1 : variavel) {
                if (variavel1[0].compareTo("-") != 0) {
                    controller.getTxtSem().appendText("Variavel " + total + " -> id: '" + variavel1[0] + "';\n");
                }
                total++;
            }
            
        }

    }

    /*
    
    Identificar a declaração de variável - só int ok
    
    detectar se ela foi declarada +1 vez (n pode); ok 
    
    detectar divisão por zero (n pode);
    
    separar o valor declarado ok

    
    
     */
}
