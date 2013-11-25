/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geratabelaresultado;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class GeraTabelaResultado {

    public static List<String> filePaths = new ArrayList<>();
    public static List<String> fileNames = new ArrayList<>();

    public static void main(String[] args) {
        List<Resultado> resultados = new ArrayList<>();
        String diretorio = System.getProperty("user.dir");
        //List<String> textos = fileTreePrinter(new File(diretorio), 0);
        fileTreePrinter(new File(diretorio), 0);
        System.out.println("*******************************************");
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).contains(".arff.txt")) {
                Resultado res = new Resultado();
                res.setNomeClassificador(fileNames.get(i).split("_")[0]);
                res.setNomeTeste(fileNames.get(i).split("_")[1].replace(".arff.txt", ""));
                try {
                    res = getResultadosArquivo(filePaths.get(i), res);
                } catch (IOException ex) {
                    Logger.getLogger(GeraTabelaResultado.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println(fileNames.get(i));
                System.out.println(res.toString());
                resultados.add(res);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Classificador").append(" | ").append("Grams").append(" | ").append("Acertos").append(" | ").append("%Acertos").append(" | ").append("Erros").append(" | ").append("%Erros").append("\n");
        for (Resultado resultado : resultados) {
            sb.append(resultado.toString()).append("\n");
        }
        try {
            printFile("analiseResultados.csv", sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(GeraTabelaResultado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<String> fileTreePrinter(File initialPath, int initialDepth) {

        int depth = initialDepth++;
        if (initialPath.exists()) {
            File[] contents = initialPath.listFiles();
            for (File content : contents) {
                if (content.isDirectory()) {
                    fileTreePrinter(content, initialDepth + 1);
                } else {
                    char[] dpt = new char[initialDepth];
                    for (int j = 0; j < initialDepth; j++) {
                        dpt[j] = '+';
                    }

                    //System.out.println(new String(dpt) + content.getName() + " " + content.getPath());
                    // System.out.println(content.toString());
                    //System.out.println(content.getName());
                    fileNames.add(content.getName());
                    filePaths.add(content.toString());
                }
            }
        }
        return filePaths;
    }

    public static Resultado getResultadosArquivo(String filePath, Resultado resultado) throws FileNotFoundException, IOException {
        // StringBuilder linha = new StringBuilder();
        String linha;
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();

                if (linha.contains("Correctly Classified Instances")) {
                    String qtdeAcertos = "";
                    String[] palavras = linha.replace("Correctly Classified Instances", "").split(" ");
                    for (String palavra : palavras) {
                        if (!palavra.equals("") && !palavra.equals("%")) {
                            qtdeAcertos = palavra;
                            resultado.setQtdeAcertos(Integer.valueOf(palavra));
                            break;
                        }

                    }
                    for (String palavra : palavras) {
                        if (!palavra.equals("") && !palavra.equals("%") && !palavra.equals(qtdeAcertos)) {
                            resultado.setPorcentagemAcertos(palavra.replace(".", ","));
                            break;
                        }
                    }

                }
                if (linha.contains("Incorrectly Classified Instances")) {
                    String qtdeErros = "";
                    String[] palavras = linha.replace("Incorrectly Classified Instances", "").split(" ");
                    for (String palavra : palavras) {
                        if (!palavra.equals("") && !palavra.equals("%")) {
                            qtdeErros = palavra;
                            resultado.setQtdeErros(Integer.valueOf(palavra));
                            break;
                        }

                    }
                    for (String palavra : palavras) {
                        if (!palavra.equals("") && !palavra.equals("%") && !palavra.equals(qtdeErros)) {
                            resultado.setPorcentagemErros(palavra.replace(".", ","));
                            break;
                        }
                    }
                }
            }
            //System.out.println(resultado.getQtdeAcertos() + " " + resultado.getPorcentagemAcertos() + " " + resultado.getQtdeErros() + " " + resultado.getPorcentagemErros());
            br.close();
            fr.close();
        }
        return resultado;
    }

    public static void printFile(String fileName, String texto) throws IOException {
        try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(texto);

            bw.newLine();

            bw.close();
            fw.close();
        }
    }

}
