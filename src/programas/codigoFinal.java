package programas;


import java.util.Arrays;
import visao.FXMLDocumentController;

public class codigoFinal {
    private FXMLDocumentController controller;
    private String[] vetorFila;
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
        for(int i=3;i<vetorFila.length;i=i+4){
            
            if(vetorFila[i].compareTo("var")==0){
                controller.getTxtFcd().appendText(vetorFila[i+8]+"\n");
            }
        }
    }
    
    public void exibe(){
        controller.getTxtFcd().appendText("\n");
    }
    
    public void conversor(){
        
    }
            
    
    
    
    
    
    
    
    
}
