/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author allan
 */
public interface InterfaceServ extends Remote {
    
    public boolean procuraArquivo(String nomeArquivo) throws RemoteException; // verifica se arquivo existe no servidor
    public String[] downloadArquivo(String nomeArquivo) throws RemoteException; // baixa arquivo do servidor e retorna null para arquivo inexistente
    public void listarArquivos() throws RemoteException; // lista arquivos no servidor
    public void uploadArquivo(String[] arquivo) throws RemoteException; // insere um arquivo no servidor e notifica quem tenha interesse nesse arquivo
    public void registrarInteresse(String arquivo, InterfaceCli refCliente, Date dataLimite) throws RemoteException; // permite ao cliente registrar interesse em arquivo especifico no servidor
    public boolean cancelarInteresse(String nome, InterfaceCli refCliente) throws RemoteException; // cancela interesse do cliente por arquivo do servidor
    public List<String> listarInfoArquivos() throws RemoteException; // lista informacoes de arquivos do servidor
    
}
