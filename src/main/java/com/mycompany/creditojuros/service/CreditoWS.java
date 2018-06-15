package com.mycompany.creditojuros.service;

import com.google.gson.Gson;
import com.mycompany.creditojuros.bd.CreditoDAO;
import com.mycompany.creditojuros.util.ConstantUtils;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("credito")
public class CreditoWS {
    
    private static final Logger LOGGER = Logger.getLogger(CreditoWS.class.getName());
    
    public CreditoWS() {
    }

    @GET
    @Produces("application/json")
    @Path("salvarcredito/{nome}/{valor}/{tipo}")
    public Response salvarCredito(@PathParam("nome") String nome, 
            @PathParam("valor") String valor, @PathParam("tipo") String tipo) {
        
        Gson parser = new Gson();
        ArrayList<CreditoVO> listaCredito = new ArrayList<>();
        try{

            CreditoVO credito = populaParamsEntrada(nome, valor, tipo);

            aplicaRegraJuros(credito);

            CreditoDAO creditoDAO = new CreditoDAO();

            creditoDAO.salvarCredito(credito);

            listaCredito = creditoDAO.buscaCredito();

            
        }catch(Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        
        return Response
            .status(200)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Credentials", "true")
            .header("Access-Control-Allow-Methods", "GET")            
            .entity(parser.toJson(listaCredito))
            .build();
        
    }
    
    private void aplicaRegraJuros(CreditoVO credito) throws ParseException{
        BigDecimal valorJuros;
        BigDecimal juros;
        DecimalFormat format = new DecimalFormat();
        format.setParseBigDecimal(true);
        
        switch (credito.getTipo()) {
            case ConstantUtils.A:  
                juros = BigDecimal.ZERO;
                break;
            case ConstantUtils.B:  
                juros = (BigDecimal) format.parse(ConstantUtils.TEN_PERCENT);
                break;
            case ConstantUtils.C:  
                juros = (BigDecimal) format.parse(ConstantUtils.TWENTY_PERCENT);
                break;
            default: 
                juros = BigDecimal.ZERO;
                break;
        }
        
        valorJuros = credito.getValor().multiply(juros);
        
        credito.setJuros(valorJuros);
    }
    
    private CreditoVO populaParamsEntrada(String nome, String valor, String tipo) throws ParseException{
        CreditoVO credito = new CreditoVO();
        
        credito.setNome(nome);
        credito.setTipo(tipo);
        DecimalFormat format = new DecimalFormat();
        format.setParseBigDecimal(true);
        credito.setValor((BigDecimal) format.parse(valor));
        
        return credito;
        
    }
    
}
