/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarmi;

import java.util.Date;

/**
 *
 * @author wagner
 */
public class Interesse {
    private String arq;
    private InterfaceCli cli;
    private Date ate;
    
    public Interesse(String nomeArquivo, InterfaceCli refCliente, Date dataLimite){
        this.arq = nomeArquivo;
        this.cli = refCliente;
        this.ate = dataLimite;
    }
    public String getNomeArquivo(){
        return this.arq;
    }
    public InterfaceCli getRefCliente(){
        return this.cli;
    }
    public Date getDataLimite(){
        return this.ate;
    }
}
