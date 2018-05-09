/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjavarmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allan
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ{

    public ServImpl() throws RemoteException {
    }
    
    
    @Override
    public void chamar(String nomeCliente, InterfaceCli refCliente) throws RemoteException{
        try {
            refCliente.echo("Servidor recebeu msg do cliente " + nomeCliente);
        } catch (RemoteException ex) {
            System.out.println("Classe ServImpl: erro ao tentar utilizar metodo echo" + ex);
        }
    }
    
}
