package com.mycompany.creditojuros.bd;

import com.mycompany.creditojuros.service.CreditoVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class CreditoDAO {
    
    private static final Logger LOGGER = Logger.getLogger(CreditoDAO.class.getName());
    
    static Connection conexao;
    
    public void salvarCredito(CreditoVO cred) {
        try {
            
            conexao = Conexao.getConexao();
            Statement statement = conexao.createStatement();
            String query = "insert into CREDITO (NOME_CLIENTE, VALOR_CREDITO, JUROS, RISCO) VALUES (\'" +
                    cred.getNome() + "\'," + cred.getValor() + ","+ cred.getJuros() + ",\'" + cred.getTipo() + "\')";
            statement.executeUpdate(query);
            
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally{
            try {
                conexao.close();
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }
    
    public ArrayList<CreditoVO> buscaCredito() {
        try {
            ArrayList<CreditoVO> listaCredito = new ArrayList<>();
            conexao = Conexao.getConexao();
            Statement statement = conexao.createStatement();
            String query = "select * from CREDITO";
            ResultSet rs = statement.executeQuery(query);
            
            while (rs.next()) {
                CreditoVO credit = new CreditoVO();
                credit.setNome(rs.getString("NOME_CLIENTE"));
                credit.setTipo(rs.getString("RISCO"));
                credit.setValor(rs.getBigDecimal("VALOR_CREDITO"));
                credit.setJuros(rs.getBigDecimal("JUROS"));
                listaCredito.add(credit);
            }
            
            return listaCredito;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally{
            try {
                conexao.close();
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return new ArrayList<>();
    }
    
}
