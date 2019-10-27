package programas;

import java.io.IOException;

public class lexico {

    private String[] linhas;
    private final funcoes funk;
    private final Fila lexicoResult = new Fila(500);
    private final Fila lexicoPosicao = new Fila(500);
    private String[][] mtokens;
    private boolean esperaEsp = false;
    private boolean esperaComp = false;

    public lexico() {
        this.funk = new funcoes();
    }

    private String espaco(String palavra, String tk) {
        if (tk.compareTo("t_escr") == 0) {
            esperaEsp = true;
        } else if (esperaEsp && tk.compareTo("t_fc") == 0) {
            esperaEsp = false;
        } else if (!esperaEsp) {
            if (tk.compareTo("id") == 0) {
                String[] esp = palavra.split(" ");
                for (String p : esp) {
                    if (p.length() != 0) {
                        return p;
                    }
                }
            }
        }
        return palavra;
    }

    public lexico(String[] linha) throws IOException {
        this.funk = new funcoes();
        this.linhas = linha;
        this.mtokens = funk.ltoken();
    }

    private String comparador(String palavra) {
        for (int i = 0; i < 33; i++) {
            if (palavra.compareTo(mtokens[i][0]) == 0) {
                return mtokens[i][1];
            }
        }
        if (!esperaComp) {
            esperaComp = true;
            return comparador(espaco(palavra, "id"));
        } else {
            esperaComp = false;
            return "id";
        }
    }

    public Fila analisador() throws IOException {
        char[] letras;
        String palavra = "";
        String[] palavras;
        int nEncontrou, primeira = 0, linhacount = 0, j;
        char[] string = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'w', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Z', 'Y', 'W', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '};
        for (String linhaAtual : this.linhas) {
            letras = linhaAtual.toCharArray();
            linhacount++;
            for (int i = 0; i < letras.length; i++) {
                nEncontrou = 0;
                for (j = 0; j < string.length; j++) {
                    if (letras[i] == string[j]) {
                        palavra += letras[i];
                        nEncontrou = 1;
                        if ((nEncontrou == 1) && (primeira == 0)) {
                            lexicoPosicao.insereFila(Integer.toString(linhacount));
                            lexicoPosicao.insereFila(Integer.toString(i));
                            primeira = 1;
                        }
                        break;
                    }
                }
                if ((j >= string.length) && nEncontrou == 0) {
                    palavra += "#";
                    palavra += letras[i];
                    palavra += "#";
                    primeira = 0;
                    lexicoPosicao.insereFila(Integer.toString(linhacount));
                    lexicoPosicao.insereFila(Integer.toString(i));
                }
            }
            palavras = palavra.split("#");
            for (String palavra1 : palavras) {
                palavra = palavra1;

                if (palavra.length() != 0) {
                    lexicoResult.insereFila(comparador(palavra));
                    lexicoResult.insereFila(lexicoPosicao.removeFila());
                    lexicoResult.insereFila(lexicoPosicao.removeFila());
                    lexicoResult.insereFila(espaco(palavra, comparador(palavra)));
                }
            }
            palavra = "";
        }
        return lexicoResult;
    }
}
