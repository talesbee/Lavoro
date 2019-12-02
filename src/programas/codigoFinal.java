package programas;

import java.math.BigInteger;
import visao.FXMLDocumentController;

public class codigoFinal {
    private FXMLDocumentController controller;
    private String[] vetorLinha;
    private String[] vetorID;
    private String[][] variaveis = new String[20][2];
    private Fila fila = new Fila(500); 
    private Fila depois;
    private Fila prog = new Fila(50);
    private final funcoes funk;
    private int escrevendo = 0;
    private int ifs = 0;
    private int fors = 0;
    private int whils = 0;
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
        String registro2 = "";
        String registro3 = "";
        String escrever;
        for(int i=0; i<(fila.tamanho()+1);i++){
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
                atribP();
            }else if(var.compareTo("_for")==0){
                ripeti();
            }else if(var.compareTo("_while")==0){
                fare();
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
        if(variavel(s).compareTo(s)==0){
            for(int i=0;i<c.length;i++){            
                controller.getTxtFcd().appendText("    	ldi r19,"+asciiValue(c[i])+"\n"); 
                controller.getTxtFcd().appendText("    	mov r0, r19\n");
                controller.getTxtFcd().appendText("    	sts UDR0,r0\n");
                controller.getTxtFcd().appendText("    	rcall delays\n");
            }
        }else{
            controller.getTxtFcd().appendText("    	mov r0, "+variavel(s)+"\n");
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
    
    private void ripeti(){
        depois = new Fila(50);
        String vari = fila.removeFila();
        String registro1 = "";
        String registro2 = "";
        registro1 = variavel(vari);

        depois.insereFila("for"+fors+":\n"); 
        depois.insereFila("    	mov r17, "+registro1+"\n");
        depois.insereFila("floop"+fors+":\n");
        registro1 = variavel(fila.removeFila());
        String op = operadorL(fila.removeFila());
        registro2 = fila.removeFila();
        depois.insereFila("    	mov "+registro1+", r17\n");
        depois.insereFila("    	cpi "+registro1+", "+registro2+"\n");
        depois.insereFila("    	"+op+" ffim"+fors+"\n");
        depois.insereFila("    	mov "+registro1+", r17 \n");
        operadorM();
        depois.insereFila("    	mov r17, "+registro1+"\n");
        
        vari = fila.removeFila();
        while(vari.compareTo("!")!=0){
            if(vari.compareTo("_escr")==0){
                vari = fila.removeFila();
                scriv(vari);
                depois.insereFila("    	rcall msgdisp"+(escrevendo-1)+"\n");
            }else if(vari.compareTo("_atrib")==0){
                atribF();
            }  
            vari = fila.removeFila();  
        }
        depois.insereFila("    	rjmp floop"+fors+"\n");
        depois.insereFila("ffim"+fors+":\n");
        depois.insereFila("    	ret\n");
        for(int i=0;i<(depois.tamanho()+1);i++){
            controller.getTxtFcd().appendText(depois.removeFila());
        }
        prog.insereFila("    	rcall for"+fors+"\n");
        fors++;
    }
    
    private void fare(){
        depois = new Fila(50);
        String vari = fila.removeFila();
        String registro1 = "";
        String registro2 = "";
        registro1 = variavel(vari);
        String op = operadorL(fila.removeFila());
        registro2 = fila.removeFila();
        depois.insereFila("while"+whils+":\n");
        depois.insereFila("	cpi "+registro1+", "+registro2+"\n");
	depois.insereFila("	"+op+" wloop"+whils+" \n");
	depois.insereFila("	ret\n");
        depois.insereFila("wloop"+whils+":\n");
        vari = fila.removeFila();
        while(vari.compareTo("!")!=0){
            if(vari.compareTo("_escr")==0){
                vari = fila.removeFila();
                scriv(vari);
                depois.insereFila("    	rcall msgdisp"+(escrevendo-1)+"\n");
            }else if(vari.compareTo("_atrib")==0){
                atribF();
            }  
            vari = fila.removeFila();  
        }
	depois.insereFila("	rjmp while"+whils+"\n");
        for(int i=0;i<(depois.tamanho()+1);i++){
            controller.getTxtFcd().appendText(depois.removeFila());
        }
        prog.insereFila("    	rcall while"+whils+"\n");
        whils++;
        
    }
    
    private void vero(){
        depois = new Fila(50);
        String vari = fila.removeFila();
        String registro1 = "";
        String registro2 = "";
        registro1 = variavel(vari);
        String op = operadorL(fila.removeFila());
        registro2 = fila.removeFila();
        depois.insereFila("if"+ifs+":\n");
        depois.insereFila("	cpi "+registro1+", "+registro2+"\n");
	depois.insereFila("	"+op+" itrue"+ifs+" \n");
	depois.insereFila("	ret\n");
        depois.insereFila("itrue"+ifs+":\n");
        vari = fila.removeFila();
        while(vari.compareTo("!")!=0){
            if(vari.compareTo("_escr")==0){
                vari = fila.removeFila();
                scriv(vari);
                depois.insereFila("    	rcall msgdisp"+(escrevendo-1)+"\n");
            }else if(vari.compareTo("_atrib")==0){
                atribF();
            }  
            vari = fila.removeFila();  
        }
	depois.insereFila("	ret\n");
        for(int i=0;i<(depois.tamanho()+1);i++){
            controller.getTxtFcd().appendText(depois.removeFila());
        }
        prog.insereFila("    	rcall if"+ifs+"\n");
        ifs++;
        
    }
    private void atribP(){
        String vari = fila.removeFila();
        String s[] = new String[5];
        int count = 0;
        while (vari.compareTo("!")!=0){
            
            s[count] = vari;
            System.out.println("String: "+s[count]+" Posic: "+count);
            count++;
            
            vari = fila.removeFila();
        }
        if(count==3){
            if(variavel(s[2]).compareTo(s[2])==0){
                prog.insereFila("    	ldi "+variavel(s[0])+", "+s[2]+"\n");
            }else{
                prog.insereFila("    	mov "+variavel(s[0])+", "+variavel(s[2])+"\n");
                
            }
        }else if(count==5){
            if(operadorMi(s[3]).compareTo("add")==0){
                if(variavel(s[2]).compareTo(s[2])==0){
                    prog.insereFila("    	ldi r17, 0\n");
                    prog.insereFila("    	ldi r16, "+s[2]+"\n");
                    prog.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    prog.insereFila("    	ldi r16, "+s[4]+"\n");
                    prog.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    prog.insereFila("    	mov "+variavel(s[0])+", r17\n");
                }else{
                    prog.insereFila("    	ldi r17, 0\n");
                    prog.insereFila("    	mov r16, "+variavel(s[2])+"\n");
                    prog.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    prog.insereFila("    	ldi r16, "+s[4]+"\n");
                    prog.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    prog.insereFila("    	mov "+variavel(s[0])+", r17\n");
                }
            }else if(operadorMi(s[3]).compareTo("sub")==0){
                if(variavel(s[2]).compareTo(s[2])==0 && variavel(s[4]).compareTo(s[4])==0){
                    prog.insereFila("    	ldi r16, "+s[2]+"\n");
                    prog.insereFila("    	"+operadorMi(s[3])+" "+variavel(s[0])+", r16\n");
                    prog.insereFila("    	ldi r16, "+s[4]+"\n");
                    prog.insereFila("    	"+operadorMi(s[3])+" "+variavel(s[0])+", r16\n");
                    
                }else if(variavel(s[2]).compareTo(s[2])!=0 && variavel(s[4]).compareTo(s[4])==0){
                    prog.insereFila("    	ldi r16, "+s[4]+"\n");
                    prog.insereFila("    	"+operadorMi(s[3])+" "+variavel(s[2])+", r16\n");
                    prog.insereFila("    	mov "+variavel(s[0])+", "+variavel(s[2])+"\n");
                    
                }else if(variavel(s[2]).compareTo(s[2])!=0 && variavel(s[4]).compareTo(s[4])!=0){
                    prog.insereFila("    	"+operadorMi(s[3])+" "+variavel(s[2])+", "+variavel(s[4])+"\n");
                    prog.insereFila("    	mov "+variavel(s[0])+", "+variavel(s[2])+"\n");
                }
             }else if(operadorMi(s[3]).compareTo("mul")==0){
                if(variavel(s[2]).compareTo(s[2])==0 && variavel(s[4]).compareTo(s[4])==0){
                    prog.insereFila("    	ldi r17, 1\n");
                    prog.insereFila("    	ldi r16, "+s[2]+"\n");
                    prog.insereFila("           "+operadorMi(s[3])+" r17, r16\n");
                    prog.insereFila("    	mov "+variavel(s[0])+", r0\n");
                    prog.insereFila("    	ldi r16, "+s[4]+"\n");
                    prog.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    prog.insereFila("    	add "+variavel(s[0])+", r0\n");
                }else if(variavel(s[2]).compareTo(s[2])!=0 && variavel(s[4]).compareTo(s[4])==0){
                    prog.insereFila("    	ldi r16, "+s[2]+"\n");
                    prog.insereFila("           "+operadorMi(s[3])+" "+variavel(s[2])+", r16\n");
                    prog.insereFila("           mov "+variavel(s[0])+", r0\n");    
                }else if(variavel(s[2]).compareTo(s[2])!=0 && variavel(s[4]).compareTo(s[4])!=0){
                    prog.insereFila("           "+operadorMi(s[3])+" "+variavel(s[2])+", "+variavel(s[4])+"\n");
                    prog.insereFila("           mov "+variavel(s[0])+", r0\n");    
                }
             }
        }
            
           
    }
        private void atribF(){
        String vari = fila.removeFila();
        String s[] = new String[5];
        int count = 0;
        while (vari.compareTo(":")!=0){
            s[count] = vari;
            System.out.println("String: "+s[count]+" Posic: "+count);
            count++;
            
            vari = fila.removeFila();
        }
        if(count==3){
            if(variavel(s[2]).compareTo(s[2])==0){
               depois.insereFila("    	ldi "+variavel(s[0])+", "+s[2]+"\n");
            }else{
                depois.insereFila("    	mov "+variavel(s[0])+", "+variavel(s[2])+"\n");
                
            }
        }else if(count==5){
            if(operadorMi(s[3]).compareTo("add")==0){
                if(variavel(s[2]).compareTo(s[2])==0){
                    depois.insereFila("    	ldi r17, 0\n");
                    depois.insereFila("    	ldi r16, "+s[2]+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    depois.insereFila("    	ldi r16, "+s[4]+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    depois.insereFila("    	mov "+variavel(s[0])+", r17\n");
                }else{
                    depois.insereFila("    	ldi r17, 0\n");
                    depois.insereFila("    	mov r16, "+variavel(s[2])+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    depois.insereFila("    	ldi r16, "+s[4]+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    depois.insereFila("    	mov "+variavel(s[0])+", r17\n");
                }
            }else if(operadorMi(s[3]).compareTo("sub")==0){
                if(variavel(s[2]).compareTo(s[2])==0 && variavel(s[4]).compareTo(s[4])==0){
                    depois.insereFila("    	ldi r16, "+s[2]+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" "+variavel(s[0])+", r16\n");
                    depois.insereFila("    	ldi r16, "+s[4]+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" "+variavel(s[0])+", r16\n");
                    
                }else if(variavel(s[2]).compareTo(s[2])!=0 && variavel(s[4]).compareTo(s[4])==0){
                    depois.insereFila("    	ldi r16, "+s[4]+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" "+variavel(s[2])+", r16\n");
                    depois.insereFila("    	mov "+variavel(s[0])+", "+variavel(s[2])+"\n");
                    
                }else if(variavel(s[2]).compareTo(s[2])!=0 && variavel(s[4]).compareTo(s[4])!=0){
                    depois.insereFila("    	"+operadorMi(s[3])+" "+variavel(s[2])+", "+variavel(s[4])+"\n");
                    depois.insereFila("    	mov "+variavel(s[0])+", "+variavel(s[2])+"\n");
                }
             }else if(operadorMi(s[3]).compareTo("mul")==0){
                if(variavel(s[2]).compareTo(s[2])==0 && variavel(s[4]).compareTo(s[4])==0){
                    depois.insereFila("    	ldi r17, 1\n");
                    depois.insereFila("    	ldi r16, "+s[2]+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    depois.insereFila("    	mov "+variavel(s[0])+", r0\n");
                    depois.insereFila("    	ldi r16, "+s[4]+"\n");
                    depois.insereFila("    	"+operadorMi(s[3])+" r17, r16\n");
                    depois.insereFila("    	add "+variavel(s[0])+", r0\n");
                }else if(variavel(s[2]).compareTo(s[2])!=0 && variavel(s[4]).compareTo(s[4])==0){
                    depois.insereFila("    	ldi r16, "+s[4]+"\n");
                    depois.insereFila("           "+operadorMi(s[3])+" "+variavel(s[2])+", r16\n");
                    depois.insereFila("           mov "+variavel(s[0])+", r0\n");    
                }else if(variavel(s[2]).compareTo(s[2])!=0 && variavel(s[4]).compareTo(s[4])!=0){
                    depois.insereFila("           "+operadorMi(s[3])+" "+variavel(s[2])+", "+variavel(s[4])+"\n");
                    depois.insereFila("           mov "+variavel(s[0])+", r0\n");    
                }
             }
        }
            
           
    }
    
    private String operadorL(String s){
        String op[] = {"<",">","<=",">=","<=>","=="};
        String ap[] = {"BRLO","BRGE","BRSH","BRGE","BRNE","BREQ"};
        for(int i=0;i<op.length;i++){
            if(s.compareTo(op[i])==0){
                return ap[i];
            }
        }
        return "error";
    }
    
    private void operadorM(){
        String n1 = variavel(fila.removeFila());
        String s = fila.removeFila();
        String n2 = fila.removeFila();
        String op[] = {"+","-","*"};
        String ap[] = {"add","sub","mul"};
        for(int i=0;i<op.length;i++){
            if(s.compareTo(op[i])==0){
                depois.insereFila("    	ldi r16, "+n2+"\n");
                depois.insereFila("    	"+ap[i]+" "+n1+", r16\n");
            }
        }
    }
    private String operadorMi(String s){
        String op[] = {"+","-","*"};
        String ap[] = {"add","sub","mul"};
        for(int i=0;i<op.length;i++){
            if(s.compareTo(op[i])==0){
                return ap[i];
            }
        }
        return "error";
    }
    
    private String variavel(String vari){
        for(int i=0;i<nvari;i++){
            String s = variaveis[i][0];
            if(vari.compareTo(s)==0){
                return variaveis[i][1];
            }
        }
        return vari;
    }
    
    private void confgInicial(){
        controller.getTxtFcd().appendText("Reset:\n");
        controller.getTxtFcd().appendText("    	RCALL USART_Init\n");
        controller.getTxtFcd().appendText("    	ldi r19,13\n");
    	controller.getTxtFcd().appendText("    	mov r0, r19\n");
    	controller.getTxtFcd().appendText("    	sts UDR0,r0\n");
    	controller.getTxtFcd().appendText("    	ldi r19,10\n");
    	controller.getTxtFcd().appendText("    	mov r0, r19\n");
    	controller.getTxtFcd().appendText("    	sts UDR0,r0\n");
    	controller.getTxtFcd().appendText("    	rcall delays\n");
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
        controller.getTxtFcd().appendText(" RX:\n");
	controller.getTxtFcd().appendText("    	lds r16,UCSR0A\n");
	controller.getTxtFcd().appendText("    	sbrs r16,RXC0 \n");
	controller.getTxtFcd().appendText("    	rjmp RX\n");
	controller.getTxtFcd().appendText("    	lds r0, UDR0\n");
	controller.getTxtFcd().appendText("    	ret\n");
        controller.getTxtFcd().appendText("\n");
    }
    private void confFinal(){
        controller.getTxtFcd().appendText("prog: \n");
        while((prog.tamanho()+1)>0){
            controller.getTxtFcd().appendText(prog.removeFila()+"\n");
        }
        controller.getTxtFcd().appendText("    	rcall RX \n");
        controller.getTxtFcd().appendText("    	rjmp prog \n");
        controller.getTxtFcd().appendText("\n");
    }

}
