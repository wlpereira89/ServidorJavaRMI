/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allan
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    private List<String[]> arquivos;
    private List<Interesse> interesses;

    public ServImpl() throws RemoteException {
        arquivos = new ArrayList<>();
        interesses = new ArrayList<>();
    }

    @Override
    public void chamar(String nomeCliente, InterfaceCli refCliente) throws RemoteException {
        try {
            refCliente.echo("Servidor recebeu msg do cliente " + nomeCliente);
        } catch (RemoteException ex) {
            System.out.println("Classe ServImpl: erro ao tentar utilizar metodo echo" + ex);
        }
    }

    @Override
    public boolean procuraArquivo(String nomeArquivo) throws RemoteException{
        if (arquivos.size() > 0) {
            String[] arq;
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                if (arq[0].equals(nomeArquivo)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String[] downloadArquivo(String nomeArquivo) throws RemoteException {
        String[] arq = null;
        if (arquivos.size() >= 1) {
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                if (arq[0].equals(nomeArquivo)) {
                    return arq;
                }
            }
        }
        return null;
    }

    @Override
    public void listarArquivos() throws RemoteException{
        if (arquivos.size() > 0) {
            String[] arq;
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                System.out.println(arq[0] + " - " + arq[1]);
            }
        } else {
            System.out.println("Não existem arquivos no servidor no momento");
        }
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void uploadArquivo(String[] arquivo) throws RemoteException {
        arquivos.add(arquivo);
        for (int i = 0; i < interesses.size(); i++) {
            Interesse interesse = interesses.get(i);
            if ((interesse.getNomeArquivo().equals(arquivo[0]))
                    && (interesse.getDataLimite().compareTo(new Date(System.currentTimeMillis())) <= 0)) {
                
                try {
                    interesse.getRefCliente().notificarInteresse(arquivo[0]);
                } catch (RemoteException ex) {
                    System.out.println("Classe ServImpl: erro ao tentar utilizar metodo echo" + ex);
                }

            }
        }
    }

    @Override
    public void registrarInteresse(String arquivo, InterfaceCli refCliente, Date dataLimite) throws RemoteException {
        this.interesses.add(new Interesse(arquivo, refCliente, dataLimite));
    }
    public boolean escreverArquivo(String nome, String conteudo){
        String[] nova  = {nome, conteudo};
        
        arquivos.add(nova);
        return true;
    }
    public void listarInteresses(){
        if (interesses.size() > 0) {
            Interesse inter;
            for (int i = 0; i < interesses.size(); i++) {
                inter = interesses.get(i);
                System.out.println("Arquivo - "+inter.getNomeArquivo()+" Data Limite: "+inter.getDataLimite()+" Cliente: "+inter.getRefCliente());
            }
        } else {
            System.out.println("Não foram registrados interesses até o momento");
        }
    }

}
