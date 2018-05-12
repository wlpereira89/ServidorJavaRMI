/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class Servidor {
    public static ServImpl serv;
    public static Registry referenciaServicoNomes;
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Boolean permanecer = true;
        try {
            //while(true){
            serv = new ServImpl();
            //InterfaceServ stub = (InterfaceServ) UnicastRemoteObject.exportObject(serv, 1099);
            referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            referenciaServicoNomes.bind("Servidor2", serv);
            
            System.out.println("Servidor pronto");
            while (permanecer) {
                System.out.println("Digite o que deseja fazer:");

                System.out.println("1 - Escrever novo arquivo");
                System.out.println("2 - Listar próprios arquivos");
                System.out.println("3 - Listar interesses");
                System.out.println("0 - sair");

                String opp = in.readLine();
                String msg;
                String nome;
                String conteudo;
                switch (opp) {

                    // Sair
                    case "0":
                        System.exit(0);
                        break;
                    case "1":
                        System.out.println("Escreva o nome do arquivo");
                        nome = in.readLine();
                        System.out.println("Escreva o conteudo do arquivo");
                        conteudo = in.readLine();
                        if (serv.escreverArquivo(nome, conteudo)) {
                            System.out.println("Arquivo inserido com sucesso");
                        } else {
                            System.out.println("Erro na inserção do arquivo");
                        }
                        break;
                    case "2":
                        serv.listarArquivos();
                        break;
                    case "3":
                        serv.listarInteresses();
                        break;
                    default:
                        System.out.println("Opção inválida digite nova opção");
                        break;
                }
            }
            /*
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            ServImpl serv = new ServImpl();
            referenciaServicoNomes.bind("Servidor1", serv);
            */
            //}
        } catch (RemoteException ex) {
            System.out.println("Classe Servidor: Erro ao iniciar servico de nomes " + ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
