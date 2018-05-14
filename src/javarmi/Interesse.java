/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarmi;

import java.util.Date;

/**
 *
 * @author allan
 * @author wagner
 * 
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
    
    /**
     * Método responsável por retornar o nome do arquivo objeto de interesse
     * @return String - nome do arquivo objeto de interesse
     */
    public String getNomeArquivo(){
        return this.arq;
    }
    
    /**
     * Método responsável por retornar a referência do cliente interessado no arquivo
     * @return InterfaceCli - referência do cliente interessado no arquivo
     */
    public InterfaceCli getRefCliente(){
        return this.cli;
    }
    
    /**
     * Método responsável por retornar a data limite do interesse pelo arquivo
     * @return Date - data limite do interesse por notificações pelo arquivo
     */
    public Date getDataLimite(){
        return this.ate;
    }
}
