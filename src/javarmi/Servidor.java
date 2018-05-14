/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allan
 * @author wagner
 * 
 */
public class Servidor {
    public static ServImpl serv;
    public static Registry referenciaServicoNomes;
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Boolean permanecer = true;
        try {
            serv = new ServImpl();
            referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            referenciaServicoNomes.bind("Servidor2", serv);
            System.out.println("Servidor pronto");
            while (permanecer) {
                System.out.println("\nDigite o que deseja fazer:");
                System.out.println("1 - Escrever novo arquivo");
                System.out.println("2 - Listar próprios arquivos");
                System.out.println("3 - Listar interesses");
                System.out.println("0 - sair");
                String opp = in.readLine();
                String msg;
                String nome;
                String conteudo;
                switch (opp) {
                    case "0": // Sair
                        System.exit(0);
                        break;
                    case "1": // escrever novo arquivo
                        System.out.println("\nEscreva o nome do arquivo");
                        nome = in.readLine();
                        System.out.println("\nEscreva o conteudo do arquivo");
                        conteudo = in.readLine();
                        if (serv.escreverArquivo(nome, conteudo)) {
                            System.out.println("\nArquivo inserido com sucesso");
                        } else {
                            System.out.println("\nErro na inserção do arquivo");
                        }
                        break;
                    case "2": // Listar proprios arquivos
                        serv.listarArquivos();
                        break;
                    case "3": // Listar interesses
                        serv.listarInteresses();
                        break;
                    default:
                        System.out.println("\nOpção inválida digite nova opção");
                        break;
                }
            }
        } catch (RemoteException ex) {
            System.out.println("Classe Servidor: Erro ao iniciar servico de nomes " + ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
