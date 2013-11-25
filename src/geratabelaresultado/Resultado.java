/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geratabelaresultado;

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

    @Override
    public String toString() {
        return getNomeClassificador() + " | " + getNomeTeste() + " | " + getQtdeAcertos() + " | " + getPorcentagemAcertos() + " | " + getQtdeErros() + " | " + getPorcentagemErros();
    }

}
