package programas;

import java.math.BigInteger;
import visao.FXMLDocumentController;

public class codigoFinal {
    private FXMLDocumentController controller;
    private String[] vetorLinha;
    private String[] vetorID;
    private String[][] variaveis = new String[20][2];
    private Fila fila = new Fila(50); 
    private Fila depois = new Fila(50);
    private Fila prog = new Fila(50);
    private final funcoes funk;
    private int escrevendo = 0;
    private int ifs = 0;
    private int fors = 0;
    private int nvari = 0;
    
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
        for(String s : vetorLinha){
            vetorID = s.split(" - ");
            for(String sc : vetorID){
                fila.insereFila(sc);
            }
            
        }
        confgInicial();
        processamento();
        confFinal();
     }
    
    private void processamento(){
        int flag = 0;
        String registro1 = "";
        String escrever;
        for(int i=0; i<fila.tamanho();i++){
            String var = fila.removeFila();
            if(var.compareTo("_escr")==0){
                escrever = fila.removeFila();
                prog.insereFila("    	rcall msgdisp"+escrevendo+"\n");
                scriv(escrever);
            }else if(var.compareTo("_var")==0){
                escrever = fila.removeFila();
                variaveis[nvari][0] = escrever;
                variaveis[nvari][1] = "r"+(nvari+22);
                controller.getTxtFcd().replaceText(29, 30, "\n    	ldi "+variaveis[nvari][1]+",0x00\n");
                nvari++;
            }else if(var.compareTo("_if")==0){
                vero(); 
            }else if(var.compareTo("_atrib")==0){
                String vari = fila.removeFila();
                
                for(int j=0;j<nvari;j++){
                    String s = variaveis[j][0];
                    System.out.println(s);
                    if(vari.compareTo(s)==0){
                        registro1 = variaveis[j][1];
                    }
                }
                vari = fila.removeFila();
                vari = fila.removeFila();
                prog.insereFila("       ldi "+registro1+", "+vari+"\n");
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
    public String[] explode(String s) {
        String[] arr = new String[s.length()];
        for(int i = 0; i < s.length(); i++)
        {
            arr[i] = String.valueOf(s.charAt(i));
        }
        return arr;
    }
    private String asciiValue(String s){
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray())
                sb.append((int)c);

            BigInteger mInt = new BigInteger(sb.toString());
            String ret = mInt.toString();
            return ret;
    }
    
    
    private void scriv(String s){
        StringBuilder sb = new StringBuilder();
        String c[] = explode(s);
        controller.getTxtFcd().appendText(";Escrita "+escrevendo+"\n");
                
        controller.getTxtFcd().appendText("msgdisp"+escrevendo+":\n");
        
        for(int i=0;i<c.length;i++){            
            controller.getTxtFcd().appendText("    	ldi r19,"+asciiValue(c[i])+"\n"); 
            controller.getTxtFcd().appendText("    	mov r0, r19\n");
            controller.getTxtFcd().appendText("    	sts UDR0,r0\n");
            controller.getTxtFcd().appendText("    	rcall delays\n");
        }
        
        controller.getTxtFcd().appendText("    	ldi r19,13\n"); 
        controller.getTxtFcd().appendText("    	mov r0, r19\n");
        controller.getTxtFcd().appendText("    	sts UDR0,r0\n");
        controller.getTxtFcd().appendText("    	ldi r19,10\n"); 
        controller.getTxtFcd().appendText("    	mov r0, r19\n");
        controller.getTxtFcd().appendText("    	sts UDR0,r0\n");
        controller.getTxtFcd().appendText("    	rcall delays\n");
        controller.getTxtFcd().appendText("    	ret\n");
        controller.getTxtFcd().appendText("\n");
        escrevendo++;
    }
    
    private void fare(){
        for: 
            clr r18
            mov r17, r18 ;garente que i=0
          loop:
            mov r18, r17
            cpi r18, 15
            brge fim
                    ;o que vai ser executado

            
            mov r18, r17
            inc r18
            mov r17, r18
            rjmp loop
          fim:
                  ret
                  
        fors++;
    }
    
    private void ripeti(){
        
    }
    
    private void vero(){
        
        String vari = fila.removeFila();
        String registro1 = "";
        String registro2 = "";
        for(int i=0;i<nvari;i++){
            String s = variaveis[i][0];
            System.out.println(s);
            if(vari.compareTo(s)==0){
                registro1 = variaveis[i][1];
            }
        }
        String op = operador(fila.removeFila());
        registro2 = fila.removeFila();
        depois.insereFila("if"+ifs+":\n");
        depois.insereFila("	cpi "+registro1+", "+registro2+"\n");
	depois.insereFila("	"+op+" true"+ifs+" \n");
	depois.insereFila("	ret\n");
        depois.insereFila("true"+ifs+":\n");
        vari = fila.removeFila();
        System.out.println("Vari: -"+vari+"-");
        while(vari.compareTo("!")!=0){
            if(vari.compareTo("_escr")==0){
                vari = fila.removeFila();
                scriv(vari);
                depois.insereFila("    	rcall msgdisp"+(escrevendo-1)+"\n");
            }else if(vari.compareTo("_atrib")==0){
                vari = fila.removeFila();
                for(int i=0;i<nvari;i++){
                    String s = variaveis[i][0];
                    System.out.println(s);
                    if(vari.compareTo(s)==0){
                        registro1 = variaveis[i][1];
                    }
                }
                vari = fila.removeFila();
                vari = fila.removeFila();
                depois.insereFila("       ldi "+registro1+", "+vari+"\n");
            }  
            vari = fila.removeFila();  
            System.out.println("Vari: -"+vari+"-");
        }
	depois.insereFila("	ret\n");
        for(int i=0;i<(depois.tamanho()+1);i++){
            controller.getTxtFcd().appendText(depois.removeFila());
        }
        prog.insereFila("    	rcall if"+ifs+"\n");
        ifs++;
        
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
    
    private void confgInicial(){
        controller.getTxtFcd().appendText("Reset:\n");
        controller.getTxtFcd().appendText("    	RCALL USART_Init\n");
        controller.getTxtFcd().appendText("                                 \n");
        controller.getTxtFcd().appendText("    	rjmp prog\n");
        controller.getTxtFcd().appendText("            \n");
        controller.getTxtFcd().appendText("USART_Init:\n");
        controller.getTxtFcd().appendText("    	ldi r17, 0\n");
        controller.getTxtFcd().appendText("     ldi r16, 103\n");
        controller.getTxtFcd().appendText("     sts UBRR0H, r17\n");
        controller.getTxtFcd().appendText("     sts UBRR0L, r16\n");
        controller.getTxtFcd().appendText("     ldi r16, (1<<RXEN0)|(1<<TXEN0)\n");
        controller.getTxtFcd().appendText("     sts UCSR0B,r16\n");
        controller.getTxtFcd().appendText("     ldi r16, (1<<USBS0)|(3<<UCSZ00)\n");
        controller.getTxtFcd().appendText("     sts UCSR0C,r16\n");
        controller.getTxtFcd().appendText("     ret\n");
        controller.getTxtFcd().appendText("\n");
        controller.getTxtFcd().appendText("delays:\n");
        controller.getTxtFcd().appendText("    	ldi  r18, 5\n");
        controller.getTxtFcd().appendText("     ldi  r19, 15\n");
        controller.getTxtFcd().appendText("     ldi  r20, 242\n");
        controller.getTxtFcd().appendText("L1: dec  r20\n");
        controller.getTxtFcd().appendText("     brne L1\n");
        controller.getTxtFcd().appendText("     dec  r19\n");
        controller.getTxtFcd().appendText("     brne L1\n");
        controller.getTxtFcd().appendText("     dec  r18\n");
        controller.getTxtFcd().appendText("     brne L1\n");
        controller.getTxtFcd().appendText("    	ret\n");
        controller.getTxtFcd().appendText("\n");
    }
    private void confFinal(){
        controller.getTxtFcd().appendText("prog: \n");
        while((prog.tamanho()+1)>0){
            controller.getTxtFcd().appendText(prog.removeFila()+"\n");
        }
        controller.getTxtFcd().appendText("    	rjmp prog \n");
        controller.getTxtFcd().appendText("\n");
    }

}
