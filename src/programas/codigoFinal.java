package programas;


import java.util.Arrays;
import visao.FXMLDocumentController;

public class codigoFinal {
    private FXMLDocumentController controller;
    private String[] vetorFila;
    private String[] vetorID;
    private final funcoes funk;
    private Fila fila;
    
    public void setController(FXMLDocumentController controller1) {
        this.controller = controller1;
    }
    
    public codigoFinal(){
        this.funk = new funcoes();
    }
    public codigoFinal(String[] filaCodigo){
        this.funk = new funcoes();
        this.vetorFila = funk.limpeza(filaCodigo);
        this.fila = new Fila(50);
    }
    
    public void organizador(){
        int tam = vetorFila.length/4;
        int count = 0;
        vetorID = new String[tam];
        for(int i=3;i<vetorFila.length;i=i+4){
            if(vetorFila[i].compareTo("scriv")==0){
                vetorID[count]="_escreva";
                count++;
                vetorID[count]=vetorFila[i+12];
                count++;
            }
        }
        exibe();
    }
    
    public void exibe(){
        for(String var : vetorID)
            controller.getTxtFcd().appendText(var+"\n");
    }
    
    public void conversor(){
        
    }
            
    
    
    
    
    
    
    
    
}
