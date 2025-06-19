package com.bbva.cmek.lib.rme2.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.elara.utility.api.connector.APIConnector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;

public class CMEKRME2ImplTest {
    /* There are methods of the APX Architecture that require greater complexity to mock, for this reason
     * an instance of the class to be tested can be created with the overwritten methods and on these
     * methods the mocking of the classes is carried out, for example Header data
     * (The Mocking the header is only for libraries that are used online, in batch it would not work)
     *
     * Import section:
     * - import com.bbva.elara.domain.transaction.RequestHeaderParamsName;
     * - import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;
     *
     * Instance section:
     *     @Mock
     *  private CommonRequestHeader commonRequestHeader;
     *
     *  @InjectMocks
     *  private CMEKRME2Impl cmekRME2 = new CMEKRME2Impl() {
     *     @Override
     *     protected CommonRequestHeader getRequestHeader() {
     *        return commonRequestHeader;
     *     }
     *  };
     */

    //Generalmente, se define todas las variables relacionadas con utilidades que están en la clase abstracta
    @Mock
    private APIConnector internalApiConnector;

    //Se inicializa el objeto pasando todos los Mock a traves de get de variables
    @InjectMocks
    private CMEKRME2Impl cmekRME2 = new CMEKRME2Impl() {
        //como el mock es para la variable internalApiConnector se crea el correspondiente get
        private APIConnector getInternalApiConnector() {
            return internalApiConnector;
        }
    };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ThreadContext.set(new Context());
    }

    @Test
    public void executeTestExecuteValidateAccountToPayment() {
        //Se incluye para tener una cobertura del 100% pero no se usa
        cmekRME2.setApplicationConfigurationService(null);

        // when(commonRequestHeader.getHeaderParameter(RequestHeaderParamsName.COUNTRYCODE)).thenReturn("ES");
        // when(applicationConfigurationService.getProperty("config.property")).thenReturn("value");
        // when(qwaiR001.execute()).thenReturn(listCustomerDTO);
        String account = "accountNumber";
        long paymentAmount = 100000;

        //Escenario donde el servicio responde null
        boolean response = cmekRME2.executeValidateAccountToPayment(account, paymentAmount);
        Assert.assertFalse(response);

        //Escenario donde el servicio responde algo pero no tiene el campo balance
        String serviceResponse = "{}";
        ResponseEntity<String> responseService = new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        //Por medio de mockito se define la respuesta predeterminada del Mock para un metodo con unos parametros especificos
        when(this.internalApiConnector.getForEntity(any(), any(), anyMap())).thenReturn(responseService);
        response = cmekRME2.executeValidateAccountToPayment(account, paymentAmount);
        Assert.assertFalse(response);

        //Escenario donde el servicio responde con un balance inferior al monto a pagar
        serviceResponse = "{\n" +
                "  \"id\": \"00130037000200140761\",\n" +
                "  \"balance\": {\n" +
                "    \"amount\": 45000.00\n" +
                "  }\n" +
                "}\n";
        responseService = new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        //Por medio de mockito se define la respuesta predeterminada del Mock para un metodo con unos parametros especificos
        when(this.internalApiConnector.getForEntity(any(), any(), anyMap())).thenReturn(responseService);
        //Se relanza el metodo a probar las veces que sea requerido para probar diferentes escenarios
        response = cmekRME2.executeValidateAccountToPayment(account, paymentAmount);
        Assert.assertFalse(response);

        //Escenario donde el servicio responde con un balance superior al monto a pagar
        serviceResponse = "{\n" +
                "  \"id\": \"00130037000200140761\",\n" +
                "  \"balance\": {\n" +
                "    \"amount\": 450000.00\n" +
                "  }\n" +
                "}\n";
        responseService = new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        //Por medio de mockito se define la respuesta predeterminada del Mock para un metodo con unos parametros especificos
        when(this.internalApiConnector.getForEntity(any(), any(), anyMap())).thenReturn(responseService);
        //Se relanza el metodo a probar las veces que sea requerido para probar diferentes escenarios
        response = cmekRME2.executeValidateAccountToPayment(account, paymentAmount);
        //Siempre los test deben tener al menos un Assert, es lo que valida algo del proceso
        Assert.assertTrue(response);
    }

    @Test
    public void executeTestWSErrors() {
        //Test que cumple con la cobertura de los catch
        Mockito.doThrow(RestClientException.class).when(internalApiConnector).getForEntity(Matchers.anyString(), Matchers.any(), Matchers.anyMap());
        boolean response = cmekRME2.executeValidateAccountToPayment("accountNumber", 100000);
        Mockito.doThrow(BusinessException.class).when(internalApiConnector).getForEntity(Matchers.anyString(), Matchers.any(), Matchers.anyMap());
        response = cmekRME2.executeValidateAccountToPayment("accountNumber", 100000);
        Mockito.doThrow(TimeoutException.class).when(internalApiConnector).getForEntity(Matchers.anyString(), Matchers.any(), Matchers.anyMap());
        response = cmekRME2.executeValidateAccountToPayment("accountNumber", 100000);

        HttpClientErrorException error = new HttpClientErrorException(HttpStatus.CONFLICT);
        Mockito.doThrow(error).when(internalApiConnector).getForEntity(Matchers.anyString(), Matchers.any(), Matchers.anyMap());
        response = cmekRME2.executeValidateAccountToPayment("accountNumber", 100000);
        //Siempre los test deben tener al menos un Assert, es lo que válida algo del proceso
        Assert.assertFalse(response);
    }

}
