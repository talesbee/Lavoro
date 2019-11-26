package programas;

import visao.FXMLDocumentController;


public class codigoIntermediario {
    private FXMLDocumentController controller;
    private String[] vetorFila;
    private String[] vetorID;
    private String[] vetorTK;
    private final funcoes funk;
    private Fila fila;
    
    public void setController(FXMLDocumentController controller1) {
        this.controller = controller1;
    }
    
    public codigoIntermediario(){
        this.funk = new funcoes();
    }
    public codigoIntermediario(String[] filaCodigo){
        this.funk = new funcoes();
        this.vetorFila = funk.limpeza(filaCodigo);
        this.fila = new Fila(50);
    }


    public void getIntermediario(){
        organizador();
        //exibe();
        conversor();
    }
    
    public void organizador(){
        int tam = vetorFila.length/4;
        int count = 0;
        vetorID = new String[tam];
        vetorTK = new String[tam];
        for(int i=3;i<vetorFila.length;i=i+4){
            vetorID[count]=vetorFila[i];
            vetorTK[count]=vetorFila[i-3];
            count++;            
        }
        
    }
    
    public void exibe(){
        for(String var : vetorTK){
            controller.getTxtIcd().appendText(var+"\n");
            System.out.println(var);
        }
    }
    
    public void conversor(){
        int flag = 0, flag2 = 0;
        for(int i =0; i<vetorID.length;i++){
            if(flag==0){
                if(vetorID[i].compareTo("scriv")==0){
                    controller.getTxtIcd().appendText("_escr "+vetorID[i+3]+"\n");
                }else if(vetorID[i].compareTo("var")==0){
                    controller.getTxtIcd().appendText("_var "+vetorID[i+2]+"\n");
                    //Func Fare
                }else if(vetorID[i].compareTo("fare")==0){
                    controller.getTxtIcd().appendText("_while ");
                    flag=1;
                    flag2=1;
                }else if(vetorID[i].compareTo("vero")==0){
                    controller.getTxtIcd().appendText("_if ");
                    flag=1;
                    flag2=1;
                }else if(vetorID[i].compareTo("ripeti")==0){
                    controller.getTxtIcd().appendText("_for ");
                    flag=2;
                    flag2=1;  
                }
                
                //Fare, vero
            }else if(flag==1){
                if(flag2==1){
                    if(vetorTK[i].compareTo("id")==0){
                        controller.getTxtIcd().appendText(vetorID[i]+" ");
                    }else if(funk.operadorL(vetorTK[i])){
                        controller.getTxtIcd().appendText(vetorID[i]+" ");
                    }else if(vetorID[i].compareTo("{")==0){
                        flag2=2;
                        controller.getTxtIcd().appendText("\n");
                    }
                }else if(flag2==2){
                    if(vetorTK[i].compareTo("id")==0){
                        controller.getTxtIcd().appendText("   "+vetorID[i]);
                        flag2=3;    
                    }else if(vetorID[i].compareTo("}")==0){
                        flag=0;
                        flag2=0;
                    }
                }else if(flag2==3){
                    if(vetorTK[i].compareTo("id")==0){
                        controller.getTxtIcd().appendText(" "+vetorID[i]+" ");
                    }else if(vetorTK[i].compareTo("t_p")==0){
                        controller.getTxtIcd().appendText(" <- ");
                    }else if(funk.operadorM(vetorTK[i])){
                        controller.getTxtIcd().appendText(" "+vetorID[i]+" ");    
                    }else if(vetorTK[i].compareTo("t_fimLinha")==0){
                        controller.getTxtIcd().appendText("\n");
                        flag2=2;
                    }
                }    
                
                //ripeti
            }else if(flag==2){
                if(flag2==1){
                    if(vetorTK[i].compareTo("id")==0){
                        controller.getTxtIcd().appendText(vetorID[i]+" ");
                    }else if(funk.operadorL(vetorTK[i])){
                        controller.getTxtIcd().appendText(vetorID[i]+" ");
                    }else if(vetorTK[i].compareTo("t_a")==0){
                        controller.getTxtIcd().appendText(" ; ");
                    }else if(vetorID[i].compareTo("{")==0){
                        flag2=2;
                        controller.getTxtIcd().appendText("\n");
                    }
                }else if(flag2==2){
                    if(vetorTK[i].compareTo("id")==0){
                        controller.getTxtIcd().appendText("   "+vetorID[i]);
                        flag2=3;    
                    }else if(vetorID[i].compareTo("}")==0){
                        flag=0;
                        flag2=0;
                    }
                }else if(flag2==3){
                    if(vetorTK[i].compareTo("id")==0){
                        controller.getTxtIcd().appendText(" "+vetorID[i]+" ");
                    }else if(vetorTK[i].compareTo("t_p")==0){
                        controller.getTxtIcd().appendText(" <- ");
                    }else if(funk.operadorM(vetorTK[i])){
                        controller.getTxtIcd().appendText(" "+vetorID[i]+" ");    
                    }else if(vetorTK[i].compareTo("t_fimLinha")==0){
                        controller.getTxtIcd().appendText("\n");
                        flag2=2;
                    }
                }
                
            }
        }
    }
    
}