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

//        try {
//            getNumeroAtributos(diretorio+ "/analiseResultados.csv");
//        } catch (IOException ex) {
//            Logger.getLogger(GeraTabelaResultado.class.getName()).log(Level.SEVERE, null, ex);
//            System.exit(0);
//        }
        //List<String> textos = fileTreePrinter(new File(diretorio), 0);
        fileTreePrinter(new File(diretorio), 0);
        System.out.println("**********************************************");
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).contains(".arff.txt")) {
                Resultado res = new Resultado();
                res.setNomeClassificador(fileNames.get(i).split("_")[0]);
                res.setNomeTeste(fileNames.get(i).split("_")[1].replace(".arff.txt", ""));
                String pathArquivoLog = diretorio + "/logs/" + fileNames.get(i).split("_")[1].replace(".txt", "erro.txt");
                res.setNumeroAtributos(Integer.parseInt(getNumeroAtributos(pathArquivoLog)));
                res.setMinMaxFreq(getMinMaxFreq(pathArquivoLog));
                try {
                    //res = getResultadosArquivo(filePaths.get(i), res);
                    res = getPredictions(filePaths.get(i), res);
                    int a = 2;
                } catch (IOException ex) {
                    Logger.getLogger(GeraTabelaResultado.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println(fileNames.get(i));
                System.out.println(res.toString());
                resultados.add(res);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Classificador").append(" | ").append("base")
                .append(" | ").append("stoplist")
                .append(" | ").append("ngram")
                .append(" | ").append("MinMaxFreq")
                .append(" | ").append("desvio")
                .append(" | ").append("MinMaxFiles")
                .append(" | ").append("Medida")
                .append(" | ").append("Atributos")
                .append(" | ").append("Total").append(" | ");
        for (int i = 0; i < 10; i++) {
            sb.append(i).append(" | ").append(i).append(" | " + "-" + " | ");
        }

        sb.append("\n");

        for (Resultado resultado : resultados) {
            sb.append(resultado.toString()).append("\n");
        }

        try {
            printFile("analiseResultados.csv", sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(GeraTabelaResultado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SendMail sendMail = new SendMail();
        sendMail.sendMail("charlesportoferreira@gmail.com", "charlesportoferreira@gmail.com", "resultados", "Resultados dos experimentos");
        
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

    public static Resultado getPredictions(String filePath, Resultado resultado) throws FileNotFoundException, IOException {
        // StringBuilder linha = new StringBuilder();
        int anterior = 0;
        int atual = 0;

        List<Fold> folds = new ArrayList<>();
        String linha;
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();

                if (linha.contains("error prediction")) {
                    Fold fold = new Fold();
                    while (br.ready()) {
                        linha = br.readLine();
                        if (!linha.equals("")) {
                            String[] palavras = linha.split(" ");
                            for (String palavra : palavras) {
                                if (palavra.matches("[0-9]+")) {
                                    atual = Integer.valueOf(palavra);
                                    break;
                                }
                            }

                            if (atual < anterior) {
                                folds.add(fold);
                                fold = new Fold();
                                anterior = atual;
                            } else {
                                anterior = atual;
                            }
                            fold.addInstancia();
                            if (linha.contains("+")) {
                                fold.addErro();
                            }
                        } else {
                            folds.add(fold);
                            break;
                        }

                    }
                }

            }
            resultado.setFolds(folds);
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

    public static String getNumeroAtributos(String filePath) {
        //filePath = "C50.arfferro.txt";
        String linha;
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
                String str;
                while (in.ready()) {
                    str = in.readLine();
                    if (str.contains("Number of Stems")) {
                        str = str.replace("Number of Stems                                              ", "");
                        return str;
                    }
                    //System.out.println("***Show****");
                    //System.exit(0);
                    //process(str);
                }
            }
        } catch (IOException e) {
        }

        return "0";
    }

    public static String getMinMaxFreq(String filePath) {
        //filePath = "C50.arfferro.txt";
        String linha;
        String min = "";
        String max = "";
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
                String str;
                while (in.ready()) {
                    str = in.readLine();
                    if (str.contains("std_dev min")) {
                        str = str.replace("Number of Stems                                              ", "");
                        min = str;
                        // return str;
                    }
                    if (str.contains("max freq")) {
                        str = str.replace("Number of Stems                                              ", "");
                        max = str;
                        return min + "-" + max;
                    }
                    //System.out.println("***Show****");
                    //System.exit(0);
                    //process(str);
                }
            }
        } catch (IOException e) {
        }

        return "0";
    }

}
