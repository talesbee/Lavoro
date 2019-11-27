package programas;

import visao.FXMLDocumentController;

public class codigoFinal {
    private FXMLDocumentController controller;
    private String[] vetorLinha;
    private String[] vetorID;
    private Fila fila;
    private final funcoes funk;
    private int escrevendo = 0;
    
    public void setController(FXMLDocumentController controller1) {
        this.controller = controller1;
    }
    
    public codigoFinal(){
        this.funk = new funcoes();
    }
    
    public codigoFinal(String[] filaCodigo){
        this.funk = new funcoes();
    }
    
    public void getCodFinal(){
        vetorLinha = controller.getTxtIcd().getText().split("\n");
        fila = new Fila((vetorLinha.length*3));
        for(String s : vetorLinha){
            vetorID = s.split(" - ");
            for(String sc : vetorID){
                fila.insereFila(sc);
                System.out.println(sc);
            }
            
        }
        processamento();
     }
    
    private void processamento(){
        int flag = 0;
        System.out.println("Tamanho: "+fila.tamanho());
        for(int i=0; i<fila.tamanho();i++){
            String var = fila.removeFila();
            if(flag==1){
                scriv(var);
                flag=0;
            }
            
            
            
            
            
            
            if(var.compareTo("_escr")==0){
                flag = 1;
            }
            
            
            
            
            

        }
        
        
        
    }
    
    
    private void flags(int f){
        switch(f){
            case 1:
                     
                break;
            case 2:
                
                break;
        }
        
        
        
    }
    
    
    private void scriv(String s){
        controller.getTxtFcd().appendText(";Escrita "+escrevendo+"\n");
        controller.getTxtFcd().appendText("msg"+escrevendo+": .db "+s+",0\n");
                
        controller.getTxtFcd().appendText("msgdisp+"+escrevendo+":\n");
        controller.getTxtFcd().appendText("        ldi r31, high(msg"+escrevendo+"<<1)\n");
        controller.getTxtFcd().appendText("        ldi r30, low(msg"+escrevendo+"<<1)\n");
        controller.getTxtFcd().appendText("loop"+escrevendo+":   lpm r16, z+\n");
        controller.getTxtFcd().appendText("        cpi r16, 0\n");
        controller.getTxtFcd().appendText("        breq here"+escrevendo+"\n");
        controller.getTxtFcd().appendText("        mov r0, r16\n");
        controller.getTxtFcd().appendText("        sts UDR0,r0\n");
        controller.getTxtFcd().appendText("        rcall delays \n");
        controller.getTxtFcd().appendText("        rjmp loop"+escrevendo+"\n");
        controller.getTxtFcd().appendText("here"+escrevendo+":  ret\n");
        escrevendo++;
    }
    
    private void fare(){
        
    }
    
    private void serial(){
        
    }
    
    private void ripeti(){
        
    }
    
    private void menzo(){
        
    }
    
    private String operador(String s){
        String op[] = {"<",">","<=",">=","<=>","=="};
        String ap[] = {"BRLO","BRGE","BRSH","BRGE","BRNE","BREQ"};
        for(int i=0;i<op.length;i++){
            if(s.compareTo(op[i])==0){
                return ap[i];
            }
        }
        return "error";
    }

}
