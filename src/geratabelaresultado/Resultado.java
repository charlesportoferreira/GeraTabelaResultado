/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geratabelaresultado;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class Resultado {

    private String nomeTeste;
    private String nomeClassificador;
    private int qtdeAcertos;
    private int qtdeErros;
    private String porcentagemErros;
    private String porcentagemAcertos;
    private List<Fold> folds;
    private int numeroAtributos;

    public int getNumeroAtributos() {
        return numeroAtributos;
    }

    public void setNumeroAtributos(int numeroAtributos) {
        this.numeroAtributos = numeroAtributos;
    }

    public Resultado() {
        this.folds = new ArrayList<>();
    }

    public List<Fold> getFolds() {
        return folds;
    }

    public void setFolds(List<Fold> folds) {
        this.folds = folds;
        calculaPorcentagemAcertosTotal();
    }

    public void addFold(Fold fold) {
        this.folds.add(fold);
    }

    public void addAll(List<Fold> folds) {
        this.folds.addAll(folds);
    }

    public String getNomeClassificador() {
        return nomeClassificador;
    }

    public void setNomeClassificador(String nomeClassificador) {
        this.nomeClassificador = nomeClassificador;
    }

    public String getNomeTeste() {
        return nomeTeste;
    }

    public void setNomeTeste(String nomeTeste) {
        this.nomeTeste = nomeTeste;
    }

    public int getQtdeAcertos() {
        return qtdeAcertos;
    }

    public void setQtdeAcertos(int qtdeAcertos) {
        this.qtdeAcertos = qtdeAcertos;
    }

    public int getQtdeErros() {
        return qtdeErros;
    }

    public void setQtdeErros(int qtdeErros) {
        this.qtdeErros = qtdeErros;
    }

    public String getPorcentagemErros() {
        return porcentagemErros;
    }

    public void setPorcentagemErros(String porcentagemErros) {
        this.porcentagemErros = porcentagemErros;
    }

    public String getPorcentagemAcertos() {
        return porcentagemAcertos;
    }

    public void setPorcentagemAcertos(String porcentagemAcertos) {
        this.porcentagemAcertos = porcentagemAcertos;
    }

    public void setMinMaxFreq(String freq) {
        if (this.nomeTeste.contains("0-0minFreq")) {
            nomeTeste = nomeTeste.replace("0-0minFreq", freq + "minFreq");
        }
    }

    @Override
    public String toString() {
        //return getNomeClassificador() + " | " + getNomeTeste() + " | " + getPorcentagemAcertos() + " | " + getPorcentagemErros() + " | " + getQtdeAcertos() + " | " + getQtdeErros();
        StringBuilder resultado = new StringBuilder(getNomeClassificador() + " | " + getNomeTeste());
        resultado.append(" | ").append(numeroAtributos);
        resultado.append(" | ").append(porcentagemAcertos);
        for (Fold fold : folds) {
            resultado.append(" | ").append(fold.getAcertos()).append(" | ")
                    .append(fold.getNrIntancias()).append(" | ");

        }

        return resultado.toString();
    }

    private void calculaPorcentagemAcertosTotal() {
        double acertosTotais = 0;
        double nrInstanciasTotais = 0;
        for (Fold fold : folds) {
            acertosTotais += fold.getAcertos();
            nrInstanciasTotais += fold.getNrIntancias();
        }

        porcentagemAcertos = String.valueOf((acertosTotais * 100) / nrInstanciasTotais);

    }

}
