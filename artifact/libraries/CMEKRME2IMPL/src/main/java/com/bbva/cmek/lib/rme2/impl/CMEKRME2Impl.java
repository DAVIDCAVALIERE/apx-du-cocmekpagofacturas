package com.bbva.cmek.lib.rme2.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.cmek.dto.account.GetAccountRequestDTO;
import com.bbva.cmek.dto.account.GetAccountResponseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;

/**
 * The CMEKRME2Impl class...
 */
public class CMEKRME2Impl extends CMEKRME2Abstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMEKRME2Impl.class);

    //Constante con el nombre de la configuracion apiconnector a usar
    private static final String LOGICAL_RESOURCE_GET_ACCOUNT = "aso.getAccount.request";
    private static final String LOGGER_REST_CLIENT_EXCEPTION = "Throw Error HttpClientErrorException: {}";
    private static final String LOGGER_REST_STATUS_CLIENT_EXCEPTION = "Throw Status Error HttpClientException: {}";
    private static final String LOGGER_REST_BODY_CLIENT_EXCEPTION = "Throw Body Error HttpClientException: {}";
    private static final String LOGGER_BUSINESS_EXCEPTION = "Throw Error BusinessException: {}";
    private static final String LOGGER_STATUS_CODE = "Status Code: {}";
    private static final String EXC_VAR = "EXC";
    private static final String EXC_VAR1 = "EXC1";

    /**
     * Este es el metodo que utiliza el servicio Rest interno,
     * como solo se usara dentro de la clase su modificador de
     * acceso será private
     */
    private GetAccountResponseDTO executeGetAccount(GetAccountRequestDTO request) {
        LOGGER.info(">>> executeGetAccount - Start <<< {}", request);
        //Se define el objeto de salida
        GetAccountResponseDTO response = new GetAccountResponseDTO();

        /**
         //Se crea este objeto para incluir las cabeceras que se van a enviar en el request
         HttpHeaders httpHeader = new HttpHeaders();
         //Se incluye la cabecera ContentType para definir que los mensajes son JSON
         httpHeader.setContentType(MediaType.APPLICATION_JSON);
         //Cuando se requiere enviar un payload para operaciones post, put, patch se define asi
         //La variable requestString es el payload JSON en String
         HttpEntity<String> requestEntity = new HttpEntity<>(requestString, httpHeader);
         **/

        //Toda variable que se defina en la url mediante {nombreVariable}, se le
        //asigna el valor mediante put dentro del hash, se define todas las variables
        //que se requieran
        HashMap<String, Object> queryParams = new HashMap<>();

        //La URL de la práctica tiene la variable {account} como
        //pathparam, por ello se asigna el valor.
        queryParams.put("account", request.getAccountNumber());

        try {
            //Se arma la peticion y mediante la utilidad "internalApiConnector" se llama la operacion GET,
            //si se requiere post es postForEntity y así con todas las operaciones HTTP
            //para POST seria
            //ResponseEntity<String> responseService = this.internalApiConnector.postForEntity(LOGICAL_RESOURCE_GET_ACCOUNT,requestEntity, String.class,queryParams);
            ResponseEntity<String> responseService = this.internalApiConnector.getForEntity(LOGICAL_RESOURCE_GET_ACCOUNT, String.class, queryParams);

            //Se maneja y transforma la respuesta siempre y cuando se reciba un valor
            if (responseService != null) {
                //Se imprime el HTTP Status de manera informativa
                LOGGER.info(LOGGER_STATUS_CODE, responseService.getStatusCode());
                //Se obtiene el mensaje de respuesta en String
                String responseExecute = responseService.getBody();

                //Se transforma el String Json recibido en el objeto que se requiere
                Gson gson = getGsonObject();
                response = gson.fromJson(responseExecute, GetAccountResponseDTO.class);
            }

            //Se recomienda manejar los siguientes catch para todos API que se use
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            LOGGER.info(LOGGER_REST_CLIENT_EXCEPTION, ex.getMessage());
            response.setErrorCode(ex.getStatusCode().toString());
            response.setErrorMessage(ex.getResponseBodyAsString());
            LOGGER.info(LOGGER_REST_STATUS_CLIENT_EXCEPTION, response.getErrorCode());
            LOGGER.info(LOGGER_REST_BODY_CLIENT_EXCEPTION, response.getErrorMessage());
        } catch (RestClientException ex) {
            LOGGER.info(LOGGER_REST_CLIENT_EXCEPTION, ex.getMessage());
            response.setErrorCode(EXC_VAR);
            response.setErrorMessage(ex.getMessage());
        } catch (BusinessException | TimeoutException ex) {
            LOGGER.info(LOGGER_BUSINESS_EXCEPTION, ex.getMessage());
            response.setErrorCode(EXC_VAR1);
            response.setErrorMessage(ex.getMessage());
        }

        LOGGER.info(">>> executeGetAccount - End <<< {}", response);
        return response;
    }


    @Override
    public boolean executeValidateAccountToPayment(String accountNumber, long amountToValidate) {
        boolean response = false;

        //Se arma la peticion para al webservice
        GetAccountRequestDTO accountRequestDTO = new GetAccountRequestDTO();
        accountRequestDTO.setAccountNumber(accountNumber);

        //se consume el servicio y se obtiene la respuesta
        GetAccountResponseDTO accountResponseDTO = this.executeGetAccount(accountRequestDTO);

        //Se valida que se tenga respuesta del servicio sin errores
        response = accountResponseDTO.getErrorMessage() == null;

        // Las validaciones multiples con AND && se sugiere colocarlas así para no anidar IF, ni poner multiples
        // condiciones en una misma línea
        if (response) {
            //Válida que la respuesta tenga un objeto balance que contiene una variable con el saldo
            response = accountResponseDTO.getBalance() != null;
        }

        if (response) {
            //Válida que el saldo de la cuenta sea mayor o igual al monto a pagar
            response = accountResponseDTO.getBalance().getAmount() >= amountToValidate;
        }

        return response;
    }

    //Utilidad para generar el objeto conversor de Objeto a String JSON y viceversa
    private Gson getGsonObject() {
        return new GsonBuilder()
                .create();
    }
}