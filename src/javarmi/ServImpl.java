/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author allan
 * @author wagner
 * 
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    private List<String[]> arquivos; // lista de arquivos presentes no Servidor
    private List<Interesse> interesses; // lista de clientes com interesses em arquivos futuros do servidor

    public ServImpl() throws RemoteException {
        arquivos = new ArrayList<>();
        interesses = new ArrayList<>();
    }

    /**
     * Método responsável por procurar arquivo no servidor
     * @param nomeArquivo nome do arquivo procurado no servidor
     * @return boolean - true se o arquivo for encontrado no servidor e false caso contrário
     * @throws RemoteException
     */
    @Override
    synchronized public boolean procuraArquivo(String nomeArquivo) throws RemoteException{
        if (arquivos.size() > 0) {
            String[] arq;
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                // arq[0] = nome     arq[1] = conteudo
                if (arq[0].equals(nomeArquivo)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Método responsável por baixar o arquivo do servidor
     * @param nomeArquivo nome do arquivo que se deseja baixar do servidor
     * @return String[] - vetor de String contendo o nome do arquivo baixado e o conteúdo do mesmo, ou retorna null caso arquivo não encontrado
     * @throws RemoteException
     */
    @Override
    synchronized public String[] downloadArquivo(String nomeArquivo) throws RemoteException {
        String[] arq = null;
        if (arquivos.size() >= 1) {
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                // arq[0] = nome     arq[1] = conteudo
                if (arq[0].equals(nomeArquivo)) {
                    return arq;
                }
            }
        }
        return null;
    }

    /**
     * método responsável por listar os arquivos do servidor
     * @throws RemoteException
     */
    @Override
    synchronized public void listarArquivos() throws RemoteException{
        if (arquivos.size() > 0) {
            String[] arq;
            System.out.println("\nArquivos no servidor:");
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                // arq[0] = nome     arq[1] = conteudo
                System.out.println(arq[0] + " - " + arq[1]);
            }
        } else {
            System.out.println("\nNão existem arquivos no servidor no momento");
        }
    }

    /**
     * Método responsável por enviar arquivo para servidor
     * @param arquivo nome do arquivo aser enviado para servidor
     * @throws RemoteException
     */
    @Override
    @SuppressWarnings("empty-statement")
    synchronized public void uploadArquivo(String[] arquivo) throws RemoteException {        
        if (this.removeArq(arquivo[0]))
            System.out.println("Arquivo já existia e foi atualizado");            
        arquivos.add(arquivo);
        List<Interesse> remover = new ArrayList<>();
        for (int i = 0; i < interesses.size(); i++) {
            Interesse interesse = interesses.get(i);
            // arq[0] = nome     arq[1] = conteudo
            Calendar agora = Calendar.getInstance();
            if (interesse.getNomeArquivo().equals(arquivo[0])) {
                remover.add(interesse); //remover o interesse já respondido, tanto notificado quanto expirado                
                if (interesse.getDataLimite().compareTo( agora.getTime() ) >= 0){    
                    try {
                        // arq[0] = nome     arq[1] = conteudo
                        interesse.getRefCliente().notificarInteresse(arquivo[0]);
                    } catch (RemoteException ex) {
                        System.out.println("Classe ServImpl: erro ao tentar contatar o cliente " + ex);
                    }
                }                
            }
            else if(interesse.getDataLimite().compareTo( agora.getTime() ) < 0){                    
                        remover.add(interesse);
                }
        }
        for (int i = 0; i < remover.size(); i++){
            Interesse interesse = remover.get(i);
            interesses.remove(interesse);
        }
    }
    
    /**
     * Método responsável por registrar interesse em arquivo que será criado futuramente no servidor
     * @param arquivo nome do arquivo objeto de interesse
     * @param refCliente referência do cliente interessado no arquivo
     * @param dataLimite data limite para manutenção do interesse pelo arquivo na lista do servidor
     * @throws RemoteException
     */
    @Override
    synchronized public void registrarInteresse(String arquivo, InterfaceCli refCliente, Date dataLimite) throws RemoteException {
        this.interesses.add(new Interesse(arquivo, refCliente, dataLimite));
    }
    
    /**
     * Método responsável por escrever arquivo no servidor, criando um arquivo atráves de seu nome e conteúdo
     * @param nome nome do arquivo que será criado
     * @param conteudo conteúdo do arquivo que será criado
     * @return boolean - true caso arquivo seja criado com sucesso, ou false do contrário
     * @throws RemoteException
     */
    synchronized public boolean escreverArquivo(String nome, String conteudo) throws RemoteException{
        String[] nova  = {nome, conteudo};        
        this.uploadArquivo(nova);
        return true;
    }
    
    /**
     * Método responsável por cancelar interesse do cliente por arquivo futuro do servidor
     * @param nomeArq nome do arquivo objeto de interesse
     * @param refCliente referência do cliente interessado pelo arquivo do servidor
     * @return boolean - true caso cancelamento do interesse seja realizado com sucesso, ou false do contrário
     * @throws RemoteException
     */
    @Override
    synchronized public boolean cancelarInteresse(String nomeArq, InterfaceCli refCliente) throws RemoteException{
        for (int i = 0; i < interesses.size(); i++) {
            Interesse interesse = interesses.get(i);
            if ((interesse.getNomeArquivo().equals(nomeArq))&&(refCliente.equals(interesse.getRefCliente()))) {
                interesses.remove(i);
                return true;
            }            
        }
        return false;
    }
    
    /**
     * Método responsável por listar clientes interessados em arquivos que ainda não existem no servidor
     */
    public void listarInteresses(){
        if (interesses.size() > 0) {
            Interesse inter;
            System.out.println("\nLista de interesses no servidor:");
            for (int i = 0; i < interesses.size(); i++) {
                inter = interesses.get(i);
                System.out.println("Arquivo - "+inter.getNomeArquivo()+" Data Limite: "+inter.getDataLimite()+" Cliente: "+inter.getRefCliente());
            }
        } else {
            System.out.println("\nNão foram registrados interesses até o momento");
        }
    }
    
    /**
     * Método responsável por listar informações dos arquivos presentes no servidor
     * @return List<String> - lista de arquivos e a quantidade de caracteres dos mesmos
     * @throws RemoteException
     */
    @Override
    public List<String> listarInfoArquivos() throws RemoteException{
        List<String> infos = new ArrayList();
        if (arquivos.size() > 0) {
            String[] arq;
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                // arq[0] = nome     arq[1] = conteudo
                String nova =  arq[0] + " - com " + arq[1].length() + " caracteres";
                infos.add(nova);
            }
        }
        else {
            infos.add("\nServidor ainda sem arquivos");
        }
        return infos;
    }
    /**
     * Método responsável por obter um arquivo da lista de arquivos do cliente
     * @param nomeArquivo  nome do arquivo a ser obtido
     * @return Boolean - true se houver arquivo removido, false se não houver arquivo
     */
    private boolean removeArq(String nomeArquivo){
        String[] arq = null;
        if (arquivos.size() >= 1) {
            for (int i = 0; i < arquivos.size(); i++) {
                arq = arquivos.get(i);
                if (arq[0].equals(nomeArquivo)) {
                    arquivos.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
}
