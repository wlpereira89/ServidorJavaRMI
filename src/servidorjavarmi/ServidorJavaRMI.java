/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjavarmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allan
 */
public class ServidorJavaRMI {

    public static void main(String[] args) {
        
        try {
            //while(true){
            ServImpl serv = new ServImpl();
            //InterfaceServ stub = (InterfaceServ) UnicastRemoteObject.exportObject(serv, 1099);
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            referenciaServicoNomes.bind("Servidor2", serv);
            
            System.out.println("Servidor pronto");

            /*
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            ServImpl serv = new ServImpl();
            referenciaServicoNomes.bind("Servidor1", serv);
            */
            //}
        } catch (RemoteException ex) {
            System.out.println("Classe Servidor: Erro ao iniciar servico de nomes " + ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(ServidorJavaRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
