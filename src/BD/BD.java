/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BD;

/**
 *
 * @author andre
 */
import formularioInicial.Variaveis;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class BD {
    static final String banco = "jdbc:mysql://localhost:3306/anamnese";//armazena na String banco o caminho do banco de dados
    Connection conexao = null;
    Statement consulta = null;
    ResultSet resultados = null;
    Connection aux = null;
    PreparedStatement minhaConsulta = null;
    
        public Connection conectar(){
            try{
                
                aux = DriverManager.getConnection(banco,"root","");
                //System.out.println(aux);
            }
            catch (SQLException erro){
                erro.printStackTrace();
            }finally{
                return aux;}
        }
        public void cadastrarMedico (String CRM, String Nome, String Login, String Senha){//função da classe BD que passa por parametro uma string, função de tipo void, que não retorna nada além do que é executado dentro dela
        try {
            conexao = DriverManager.getConnection(banco,"root","");//conecta ao banco de dados com o usuario root e a senha
            minhaConsulta = conexao.prepareStatement("INSERT INTO medico VALUES (null, ?, ?, ?, ?)");// faz um select na tabela aluno com situação expecifica na condição where
            minhaConsulta.setString(1,CRM);
            minhaConsulta.setString(2,Nome);
            minhaConsulta.setString(3,Login);
            minhaConsulta.setString(4,Senha);
            minhaConsulta.executeUpdate();
        }
        catch (SQLException erro){
            erro.printStackTrace();
        }
        finally {
            try{
                minhaConsulta.close();
                conexao.close();
            }
            catch (Exception erronovo){
                erronovo.printStackTrace();
            }
        }
    }
        public void cadastrarEmpresa (String CNPJ, String nome, String endereco){//função da classe BD que passa por parametro uma string, função de tipo void, que não retorna nada além do que é executado dentro dela
        try {
            conexao = DriverManager.getConnection(banco,"root","");//conecta ao banco de dados com o usuario root e a senha
            minhaConsulta = conexao.prepareStatement("INSERT INTO empresa VALUES (null, ?, ?, ?)");// faz um select na tabela aluno com situação expecifica na condição where
            minhaConsulta.setString(1,CNPJ);
            minhaConsulta.setString(2,nome);
            minhaConsulta.setString(3,endereco);
            minhaConsulta.executeUpdate();
        }
        catch (SQLException erro){
            erro.printStackTrace();
        }
        finally {
            try{
                minhaConsulta.close();
                conexao.close();
            }
            catch (Exception erronovo){
                erronovo.printStackTrace();
            }
        }
    }
        public void cadastrarPaciente (String nome, String data, String sexo, String cor, String estadoCivil, String RG, String Endereco, String Cidade, String Cargo, String pai, String mae,String naturalidade, int id_Empresa){//função da classe BD que passa por parametro uma string, função de tipo void, que não retorna nada além do que é executado dentro dela
        try {
            conexao = DriverManager.getConnection(banco,"root","");//conecta ao banco de dados com o usuario root e a senha
            minhaConsulta = conexao.prepareStatement("INSERT INTO Paciente VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)");// faz um select na tabela aluno com situação expecifica na condição where
            minhaConsulta.setString(1,nome);
            minhaConsulta.setString(2,data);
            minhaConsulta.setString(3,sexo);
            minhaConsulta.setString(4,cor);
            minhaConsulta.setString(5,estadoCivil);
            minhaConsulta.setString(6,RG);
            minhaConsulta.setString(7,Endereco);
            minhaConsulta.setString(8,Cidade);
            minhaConsulta.setString(9,Cargo);
            minhaConsulta.setString(10,pai);
            minhaConsulta.setString(11,mae);
            minhaConsulta.setString(12,naturalidade);
            minhaConsulta.setInt(13,id_Empresa);
            minhaConsulta.executeUpdate();
        }
        catch (SQLException erro){
            erro.printStackTrace();
        }
        finally {
            try{
                minhaConsulta.close();
                conexao.close();
            }
            catch (Exception erronovo){
                erronovo.printStackTrace();
            }
        }
    }
        public void attPaciente (String nome, String data, String sexo, String cor, String estadoCivil, String RG, String Endereco, String Cidade, String Cargo, String pai, String mae,String naturalidade, int id_Empresa, int idPaciente){//função da classe BD que passa por parametro uma string, função de tipo void, que não retorna nada além do que é executado dentro dela
        try {
            conexao = DriverManager.getConnection(banco,"root","");//conecta ao banco de dados com o usuario root e a senha
            minhaConsulta = conexao.prepareStatement("UPDATE Paciente set nome = ?, dataNasc = ?, Sexo = ?, Cor = ?, estadoCivil = ?, RG = ?, Endereco = ?, Cidade = ?, Cargo = ?, pai = ? , mae = ? , naturalidade = ?, id_Empresa = ? WHERE idPaciente = ?");// faz um select na tabela aluno com situação expecifica na condição where
            minhaConsulta.setString(1,nome);
            minhaConsulta.setString(2,data);
            minhaConsulta.setString(3,sexo);
            minhaConsulta.setString(4,cor);
            minhaConsulta.setString(5,estadoCivil);
            minhaConsulta.setString(6,RG);
            minhaConsulta.setString(7,Endereco);
            minhaConsulta.setString(8,Cidade);
            minhaConsulta.setString(9,Cargo);
            minhaConsulta.setString(10,pai);
            minhaConsulta.setString(11,mae);
            minhaConsulta.setString(12,naturalidade);
            minhaConsulta.setInt(13,id_Empresa);
            minhaConsulta.setInt(14,idPaciente);
            minhaConsulta.executeUpdate();
        }
        catch (SQLException erro){
            erro.printStackTrace();
        }
        finally {
            try{
                minhaConsulta.close();
                conexao.close();
            }
            catch (Exception erronovo){
                erronovo.printStackTrace();
            }
        }
    }
    
    public void consultarSenha (String login, String senha){//função da classe BD que passa por parametro uma string, função de tipo void, que não retorna nada além do que é executado dentro dela
        try {
            conexao = DriverManager.getConnection(banco,"root","");//conecta ao banco de dados com o usuario root e a senha
            minhaConsulta = conexao.prepareStatement("select senha from medico where login = ?");// faz um select na tabela condição where
            minhaConsulta.setString(1,login);//passa a variavel situação do tipo String para condição da query
            resultados = minhaConsulta.executeQuery();//executa a query e armazena na variavel resultados do tipo ResultSet
            while (resultados.next()){
            String senhaSQL = resultados.getString("senha");

            if(senhaSQL.equals(senha)){            
                Variaveis.login = 1;
            }else {
                Variaveis.login = 0;
            }
        }}
        catch (SQLException erro){
            erro.printStackTrace();
        }
        finally {
            try{
                resultados.close();
                minhaConsulta.close();
                conexao.close();
            }
            catch (Exception erronovo){
                erronovo.printStackTrace();
            }
        }
    }
    public void consultarEmpresa (int idEmpresa){//função da classe BD que passa por parametro uma string, função de tipo void, que não retorna nada além do que é executado dentro dela
        try {
            conexao = DriverManager.getConnection(banco,"root","");//conecta ao banco de dados com o usuario root e a senha
            minhaConsulta = conexao.prepareStatement("select CNPJ, nome, endereco from empresa where idEmpresa = ?");// faz um select na tabela condição where
            minhaConsulta.setInt(1,idEmpresa);//passa a variavel situação do tipo String para condição da query
            resultados = minhaConsulta.executeQuery();//executa a query e armazena na variavel resultados do tipo ResultSet
            while (resultados.next()){
            Variaveis.CPNJ = resultados.getString("CNPJ");
            Variaveis.NomeEmpresa = resultados.getString("nome");
            Variaveis.EnderecoEmpresa = resultados.getString("endereco");
        }}
        catch (SQLException erro){
            erro.printStackTrace();
        }
        finally {
            try{
                resultados.close();
                minhaConsulta.close();
                conexao.close();
            }
            catch (Exception erronovo){
                erronovo.printStackTrace();
            }
        }
    }
    public void consultarPaciente (int idPaciente){//função da classe BD que passa por parametro uma string, função de tipo void, que não retorna nada além do que é executado dentro dela
        try {
            conexao = DriverManager.getConnection(banco,"root","");//conecta ao banco de dados com o usuario root e a senha
            minhaConsulta = conexao.prepareStatement("select * from paciente where idPaciente = ?");// faz um select na tabela condição where
            minhaConsulta.setInt(1,idPaciente);//passa a variavel situação do tipo String para condição da query
            resultados = minhaConsulta.executeQuery();//executa a query e armazena na variavel resultados do tipo ResultSet
            while (resultados.next()){
            Variaveis.nome = resultados.getString("nome");
            Variaveis.data = resultados.getString("dataNasc");
            Variaveis.sexo = resultados.getString("Sexo");
            Variaveis.cor = resultados.getString("Cor");
            Variaveis.estadoCivil = resultados.getString("estadoCivil");
            Variaveis.RG = resultados.getString("RG");
            Variaveis.Endereco = resultados.getString("Endereco");
            Variaveis.Cidade = resultados.getString("Cidade");
            Variaveis.Cargo = resultados.getString("Cargo");
            Variaveis.idEmpresa = resultados.getString("id_empresa");
            Variaveis.pai = resultados.getString("pai");
            Variaveis.mae = resultados.getString("mae");
            Variaveis.naturalidade = resultados.getString("naturalidade");
        }}
        catch (SQLException erro){
            erro.printStackTrace();
        }
        finally {
            try{
                resultados.close();
                minhaConsulta.close();
                conexao.close();
            }
            catch (Exception erronovo){
                erronovo.printStackTrace();
            }
        }
    }
}


