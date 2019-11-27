package programas;

import java.util.Arrays;
import visao.FXMLDocumentController;

public class codigoFinal {
    private FXMLDocumentController controller;
    private String[] vetorFila;
    private String[] vetorID;
    private final funcoes funk;
    private Fila fila;
    private boolean serialActve = false;
    
    public void setController(FXMLDocumentController controller1) {
        this.controller = controller1;
    }
    
    public codigoFinal(){
        this.funk = new funcoes();
    }
    
    public codigoFinal(String[] filaCodigo){
        this.funk = new funcoes();
        this.fila = new Fila(50);
    }
    
    public void getCodFinal(){
        serialActive();
        vetorID = controller.getTxtIcd().getText().split("\n");
        String sa[] = new String[10];
        for(String s : vetorID){
            sa = s.split(".");
            organizador(sa);
        }
        sdelay();
    }
    
    private void organizador(String s[]){
        int flag = 0;
        for(String var : s){
            System.out.println(var);
            if(flag == 0){
                if(var.compareTo("_escr")==0){
                    flag = 1;
                }
            }else if(flag==1){
                String chr[] = conversorStr(var).clone();
                creatTxt(chr);
                flag=0;
            }
        }
    }
    
    private void serialActive(){
        controller.getTxtFcd().appendText("USART_Init:\n");
	controller.getTxtFcd().appendText("     ldi r17, 0\n");
	controller.getTxtFcd().appendText("     ldi r16, 103\n");
	controller.getTxtFcd().appendText("     sts UBRR0H, r17\n");
	controller.getTxtFcd().appendText("     sts UBRR0L, r16\n");
	controller.getTxtFcd().appendText("     ldi r16, (1<<RXEN0)|(1<<TXEN0)\n");
	controller.getTxtFcd().appendText("     sts UCSR0B,r16\n");
	controller.getTxtFcd().appendText("     ldi r16, (1<<USBS0)|(3<<UCSZ00)\n");
	controller.getTxtFcd().appendText("     sts UCSR0C,r16\n");
	controller.getTxtFcd().appendText("     ret\n");
        controller.getTxtFcd().appendText("Reset:\n");
	controller.getTxtFcd().appendText("     RCALL USART_Init\n");
    }
    
    private void sdelay(){
        controller.getTxtFcd().appendText("\n");
        controller.getTxtFcd().appendText("sdelay:\n");    
        controller.getTxtFcd().appendText("     ldi  r18, 11\n");
        controller.getTxtFcd().appendText("     ldi  r19, 99\n");
        controller.getTxtFcd().appendText("L1: dec  r19\n");
        controller.getTxtFcd().appendText("     brne L1\n");
        controller.getTxtFcd().appendText("     dec  r18\n");
        controller.getTxtFcd().appendText("     brne L1\n");
        controller.getTxtFcd().appendText("     ret\n");
    }
    
    private String[] conversorStr(String Str){
        char chr[] = Str.toCharArray();
        String s[] = new String[chr.length];
        for(int i=0;i<chr.length;i++){
           int ascii = (int)chr[i];
           s[i] = Integer.toString(ascii);
        }
        return s;
    }
    
    private void creatTxt(String s[]){
        serialActve = true;
        controller.getTxtFcd().appendText("\n ;Escrevendo Serial "+s.toString()+"\n");
        for(String txt : s){
            if(txt.compareTo("\n")!=0){
                controller.getTxtFcd().appendText("     ldi r16,"+txt+"\n");
                controller.getTxtFcd().appendText("     sts UDR0, r16\n");
                controller.getTxtFcd().appendText("     rcall sdelay\n");
            }
        }        
        controller.getTxtFcd().appendText("\n");
    }
}
