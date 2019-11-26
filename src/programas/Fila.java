package programas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Tales
 */
public class Fila {
    private String [] v;
    private int primeiro;
    private int ultimo;
    
    
    // ---------------------------- construtor
    
    public Fila(int numElementos) {
        v = new String[numElementos];
        primeiro = 0;
        ultimo = -1;
    }
    
    // ---------------------------- insereFila
    
    public void insereFila(String elemento) {
        int m = v.length;
        
        if (ultimo < m-1) {
           ultimo++;
           v[ultimo] = elemento;
        } else {
            System.out.println("Status: Fila Cheia");
        }
    }
    
    // ---------------------------- removeFila
    
    public String removeFila() {
        String elemento;
        
        if (ultimo != -1) {
            elemento = v[primeiro];
            primeiro++;
            if (primeiro > ultimo) {
                primeiro = 0;
                ultimo = -1;
            }
            return(elemento);
        } else {
            System.out.println("Status: Fila Vazia");
            return ("-1");
        }
    }

    // ---------------------------- consultaFila
    
    public String consultaFila() {
        String elemento;
        
        if (ultimo != -1) {
            elemento = v[primeiro];
            return elemento;
        } else {
            System.out.println("Status: Fila Vazia");
            return("-1");
        }
    }

    // ---------------------------- exibeFila
    
    public String[] exibeFila() {
        return v;
    }
    
    // ---------------------------- retornaTamanho
    
    public int tamanho(){
        return ultimo;
    }
}

