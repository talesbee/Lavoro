package visao;




import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.scene.control.Tab;
import programas.lexico;
import programas.Fila;
import programas.codigoFinal;
import programas.codigoIntermediario;
import programas.sintatico;
import programas.funcoes;
import programas.semantico;




public class FXMLDocumentController implements Initializable {
   
    public static FileWriter arq;
    public static PrintWriter gravarArq;
    public static String saida = "codigoLavoro.txt";
    public Fila resultLT;
    public Fila resultST;
    public funcoes funk = new funcoes();
    
    
    public FXMLDocumentController(){
        
    }
    
    public TextArea getTxtSin(){
        return txt_sint;
    }
    public TextArea getTxtSem(){
        return txt_sem;
    }
    public TextArea getTxtFcd(){
        return txt_fcd;
    }
    public TextArea getTxtIcd(){
        return txt_icd;
    }

    @FXML
    private TextArea txt_codigo;

    @FXML
    private TextArea txt_log;

    @FXML
    private Tab tab_lt;

    @FXML
    private TextArea txt_lt;

    @FXML
    private Tab tab_sint;

    @FXML
    private TextArea txt_sint;

    @FXML
    private Tab tab_sem;

    @FXML
    private TextArea txt_sem;
    
    @FXML
    private Tab tab_fcd;

    @FXML
    private TextArea txt_fcd;
    
    @FXML
    private Tab tab_icd;

    @FXML
    private TextArea txt_icd;

    @FXML
    private void compilarAction() throws IOException {
        this.tab_lt.setDisable(true);
        this.tab_sint.setDisable(true);
        this.tab_sem.setDisable(true);
        this.tab_fcd.setDisable(true);
        this.tab_icd.setDisable(true);
        this.txt_log.setText("");
        this.txt_lt.setText("");
        this.txt_sint.setText("");
        this.txt_sem.setText("");
        this.txt_fcd.setText("");
        this.txt_icd.setText("");
        lexico();
        
    }

    public void lexico() throws IOException {
        String[] linha = this.txt_codigo.getText().split("\n");
        lexico lex = new lexico(linha);
        int tokens = 0;
        resultLT = lex.analisador();
        this.tab_lt.setDisable(false);
        String[] listaTokens = resultLT.exibeFila();
        for (int i = 0; i < resultLT.tamanho(); i++) {
            tokens++;
            this.txt_lt.appendText("Token: '" + listaTokens[i]);
            i++;
            this.txt_lt.appendText("' Linha: '" + listaTokens[i]);
            i++;
            this.txt_lt.appendText("' Coluna: '" + listaTokens[i]);
            i++;
            this.txt_lt.appendText("' Lexema: '" + listaTokens[i] + "' \n");
        }

        this.txt_log.appendText("Analisador lexico executado com sucesso! \n");
        if(resultLT.tamanho()>1){
            this.txt_log.appendText("Foram encontrados " + tokens + " tokens. \n");
            sintatico();
        }else{
            this.txt_log.appendText("Não foram encontrados Tokens! \n");
        }
    }

    public void sintatico() throws IOException {

        int tamanho = funk.tamanho(resultLT.exibeFila());
        String[] vetorST = new String[tamanho];
        String[] vetorLT = resultLT.exibeFila().clone();
        System.arraycopy(vetorLT, 0, vetorST, 0, tamanho);
        sintatico sin = new sintatico(vetorST);
        sin.setController(aplicacao.getController());
        sin.analisador();
        this.tab_sint.setDisable(false);
        if (sin.getFinal()) {
            this.txt_log.appendText("Sintatico executado com sucesso! \n");
            this.txt_log.appendText("Sem erro Sintático! \n");
            semantico();
        } else {
            this.txt_log.appendText("Sintatico encontrou um erro! \n");
        }
    }

    public void semantico() {
        this.tab_sem.setDisable(false);
        semantico sem = new semantico(resultLT);
        sem.setController(aplicacao.getController());
        sem.analisador();
        this.txt_log.appendText("Semantico executado com sucesso! \n");
        if (sem.getResult()){
            this.txt_log.appendText("Sem erro semantico! \n");
            this.txt_sem.appendText("Nenhum erro semantico encontrado!");
            codigoIntermediario();
        }else{
            this.txt_log.appendText("Erro Semantico! \n");
        }
    }
    
    public void codigoIntermediario(){
        this.tab_icd.setDisable(false);
        String[] vetorLT = resultLT.exibeFila().clone();
        codigoIntermediario ci = new codigoIntermediario(vetorLT);
        ci.setController(aplicacao.getController());
        ci.getIntermediario();
    }
    
    public void codigoFinal(){
        this.tab_fcd.setDisable(false);
        String[] vetorLT = resultLT.exibeFila().clone();
        codigoFinal cf = new codigoFinal(vetorLT);
        cf.setController(aplicacao.getController());
        cf.organizador();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

}
