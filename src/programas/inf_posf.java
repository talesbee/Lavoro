
package programas;

public class inf_posf {
    private Pilha p;
    private char expr[];
    private int i = 0;
    private char c, t;
    
    public inf_posf(){
        
    }
    public inf_posf(char x[]){
        this.expr = x;
    }
    
    public char[] conv(){
        char result[];
        result = new char[30];
        int count = 0;
        p.empilhar('(');
        
        do{
            c = expr[i];
            i++;
            if(c >= 'a' && c<= 'z'){
                result[count] = c;
                count++;
            }else if(c == '('){
                p.empilhar('(');
            }else if(c == ')' || c == '\0'){
                do{
                    t = (char)p.desempilhar();
                    if(t != '('){
                        result[count] = t;
                        count++;
                    }
                }while(t !='(');
            }else if(c == '+' || c == '-' ||
                     c == '*' || c == '/' ||
                     c == '^'){
                while(true){
                    t = (char)p.desempilhar();
                    if(prioridade(c,t)){
                       p.empilhar(t);
                       p.empilhar(t);
                       break;
                    }else{
                        result[count] = t;
                        count++;
                    }
                }
            }
        }while(c != '\0');
        
        return result;
    }
    public boolean prioridade(char cc, char tt){
        int pc = 0;
        int pt = 0;
        
        if(cc == '^')
            pc = 4;
        else if(cc == '*' || cc == '/')
            pc = 2;
        else if(cc == '+' || cc == '-')
            pc = 1;
        else if(cc =='(')
            pc = 4;
        
        if(tt == '^')
            pc = 3;
        else if(tt == '*' || tt == '/')
            pc = 2;
        else if(t == '+' || tt == '-')
            pc = 1;
        else if(t =='(')
            pc = 0;
        
        return (pc > pt);
    }
}
    
    