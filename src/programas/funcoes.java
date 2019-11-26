package programas;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class funcoes {

    private final String[][] tokens = new String[33][2];

    public funcoes() {
    }
    
    public boolean operadorM(String valor) {
        String[] op = {"t_mais", "t_mul", "t_menos", "t_div"};
        for (String op1 : op) {
            if (op1.compareTo(valor) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean operadorL(String valor) {
        String[] op = {"t_maior", "t_menor", "t_igual", "t_maiorIgual", "t_menorIgual","t_nIgual"};
        for (String op1 : op) {
            if (op1.compareTo(valor) == 0) {
                return true;
            }
        }
        return false;
    }
    
    public String[] limpeza(String[] vetor){
        String[] limpo;
        int count = 0;
        for(String tam : vetor){
            if(tam != null){
                count++;
            }
        }
        limpo = new String[count];
        for(int i=0;i<count;i++){
            limpo[i]=vetor[i];
        }
        return limpo;
    }
    public InputStream local(String resource) throws IOException {
        // this is the path within the jar file
        InputStream input = funcoes.class.getResourceAsStream("/arquivos/" + resource);
        if (input == null) {
            // this is how we load file within editor (eg eclipse)
            input = funcoes.class.getClassLoader().getResourceAsStream(resource);
        }
        return input;
    }

    public String[][] ltoken() throws IOException {
        String[] linhas = null;
        String linha;
        int j = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(local("tokens.csv")))) {
            while ((linha = br.readLine()) != null) {
                linhas = linha.split(";");
                for (int i = 0; i <= 1; i++) {
                    tokens[j][i] = linhas[i];
                }
                j++;
            }
            br.close();
        } catch (IOException ex) {
            System.out.println("Erro " + ex.getMessage());
        }
        return tokens;
    }

    public String getFile(String arquivoAberto) throws IOException {
        File dir1 = new File(".");
        String localDoArquivo = dir1.getCanonicalPath();
        char[] localChar = localDoArquivo.toCharArray();
        char barra = '\\';
        localDoArquivo = "";
        for (int i = 0; i < localChar.length; i++) {
            if (localChar[i] == barra) {
                localDoArquivo += "/";
            } else {
                localDoArquivo += localChar[i];
            }
        }
        localDoArquivo += "/" + arquivoAberto;
        return localDoArquivo;
    }

    public int tamanho(String[] vetor) {
        int tam = 0;
        while(vetor[tam]!=null){
            tam++;
        }
        return tam;
    }

}
