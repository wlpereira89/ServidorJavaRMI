/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
    }

    @Override
    public void chamar(String nomeCliente, InterfaceCli refCliente) {
        try {
            refCliente.echo("Servidor recebeu msg do cliente " + nomeCliente);
        } catch (RemoteException ex) {
            System.out.println("Classe ServImpl: erro ao tentar utilizar metodo echo" + ex);
        }
    }

    @Override
    public boolean procuraArquivo(String nomeArquivo) {
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
    public String[] downloadArquivo(String nomeArquivo) {
        String[] arq = null;
        if (arquivos.size() >= 1) {
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                if (arq[0].equals(nomeArquivo)) {
                    return arq;
                }
            }
        }
        return arq;
    }

    @Override
    public void listarArquivos() {
        if (arquivos.size() > 0) {
            System.out.println("\nEste peer possui os aqrquivos");
            String[] arq;
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                System.out.println(arq[0] + " - " + arq[1]);
            }
        } else {
            System.out.println("Este peer não possui arquivos");
        }
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void uploadArquivo(String[] arquivo) {
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
    public void registrarInteresse(String arquivo, InterfaceCli refCliente, Date dataLimite) {
        this.interesses.add(new Interesse(arquivo, refCliente, dataLimite));
    }

}
