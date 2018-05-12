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
    synchronized public void chamar(String nomeCliente, InterfaceCli refCliente) throws RemoteException {
        try {
            refCliente.echo("Servidor recebeu msg do cliente " + nomeCliente);
        } catch (RemoteException ex) {
            System.out.println("Classe ServImpl: erro ao tentar utilizar metodo echo" + ex);
        }
    }

    @Override
    synchronized public boolean procuraArquivo(String nomeArquivo) throws RemoteException{
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
    synchronized public String[] downloadArquivo(String nomeArquivo) throws RemoteException {
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
    synchronized public void listarArquivos() throws RemoteException{
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
    synchronized public void uploadArquivo(String[] arquivo) throws RemoteException {
        arquivos.add(arquivo);
        List<Integer> remover = new ArrayList();
        for (int i = 0; i < interesses.size(); i++) {
            Interesse interesse = interesses.get(i);
            if (interesse.getNomeArquivo().equals(arquivo[0])) {
                remover.add(i); //remover o interesse já respondido tanto notificado quanto expirado
                if (interesse.getDataLimite().compareTo(new Date(System.currentTimeMillis())) >= 0){
                    try {
                        interesse.getRefCliente().notificarInteresse(arquivo[0]);
                    } catch (RemoteException ex) {
                        System.out.println("Classe ServImpl: erro ao tentar contatar o cliente " + ex);
                    }
                }
                
            }
        }
        for (int i = remover.size()-1; i >= 0 ; i--){
            interesses.remove(i);
        }
    }

    @Override
    synchronized public void registrarInteresse(String arquivo, InterfaceCli refCliente, Date dataLimite) throws RemoteException {
        this.interesses.add(new Interesse(arquivo, refCliente, dataLimite));
    }
    synchronized public boolean escreverArquivo(String nome, String conteudo) throws RemoteException{
        String[] nova  = {nome, conteudo};        
        this.uploadArquivo(nova);
        return true;
    }
    @Override
    synchronized public boolean cancelarInteresse(String nome, InterfaceCli refCliente) throws RemoteException{
        for (int i = 0; i < interesses.size(); i++) {
            Interesse interesse = interesses.get(i);
            if ((interesse.getNomeArquivo().equals(nome))&&(refCliente.equals(interesse.getRefCliente()))) {
                interesses.remove(i);
                return true;
            }            
        }
        return false;
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
    @Override
    public List<String> listarInfoArquivos() throws RemoteException{
        List<String> infos = new ArrayList();
        if (arquivos.size() > 0) {
            String[] arq;
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                String nova = arq[0] + " - com " + arq[1].length() + " caracteres";
                infos.add(nova);
            }
        }
        else {
            infos.add("Servidor ainda sem arquivos");
        }
        return infos;
    }
}
