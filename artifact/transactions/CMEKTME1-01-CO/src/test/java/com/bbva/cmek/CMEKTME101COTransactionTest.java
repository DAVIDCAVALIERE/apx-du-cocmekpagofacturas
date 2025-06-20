package com.bbva.cmek;

import com.bbva.cmek.dto.bill.PaymentDTO;
import com.bbva.cmek.lib.rme1.CMEKRME1;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Advice;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.RequestHeaderParamsName;
import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CMEKTME101COTransactionTest {

    private Map<String, Object> parameters;

    private Map<Class<?>, Object> serviceLibraries;

    @Mock
    private ApplicationConfigurationService applicationConfigurationService;

    @Mock
    private CommonRequestHeader commonRequestHeader;

    //Toda libreria que se use en la transaccion debe definirse como mock
    @Mock
    private CMEKRME1 cmekRME1;

    private final CMEKTME101COTransaction transaction = new CMEKTME101COTransaction() {
        @Override
        protected void addParameter(String field, Object obj) {
            if (parameters != null) {
                parameters.put(field, obj);
            }
        }

        @Override
        protected Object getParameter(String field) {
            return parameters.get(field);
        }

        @Override
        protected <T> T getServiceLibrary(Class<T> serviceInterface) {
            return (T) serviceLibraries.get(serviceInterface);
        }

        @Override
        public String getProperty(String keyProperty) {
            return applicationConfigurationService.getProperty(keyProperty);
        }

        @Override
        protected CommonRequestHeader getRequestHeader() {
            return commonRequestHeader;
        }
    };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        initializeTransaction();
        // Set the mocks created from the libraries to the transaction, e.g.:
        // setServiceLibrary(QWAIR001.class, qwaiR001);
        //Toda libreria que se defina como mock debe incluirse con la siguiente linea
        setServiceLibrary(CMEKRME1.class, cmekRME1);
    }

    private void initializeTransaction() {
        transaction.setContext(new Context());
        parameters = new HashMap<>();
        serviceLibraries = new HashMap<>();
    }

    private void setServiceLibrary(Class<?> clasz, Object mockObject) {
        serviceLibraries.put(clasz, mockObject);
    }

    private void setParameterToTransaction(String parameter, Object value) {
        parameters.put(parameter, value);
    }

    private Object getParameterFromTransaction(String parameter) {
        return parameters.get(parameter);
    }

    @Test
    public void executeTest() {
        // when(commonRequestHeader.getHeaderParameter(RequestHeaderParamsName.COUNTRYCODE)).thenReturn("ES");
        // when(applicationConfigurationService.getProperty("config.property")).thenReturn("value");
        // when(qwaiR001.execute()).thenReturn(listCustomerDTO);
        // setParameterToTransaction("customerIn", new CustomerDTO());

        //Se define la entrada de la transaccion
        setParameterToTransaction("payment", new PaymentDTO());

        //Se ejecuta la transaccion
        transaction.execute();
        Assert.assertEquals(0, transaction.getAdviceList().size());

        //Se define el camino cuando hay addvices
        transaction.getAdviceList().add(new Advice());
        //Se ejecuta la transaccion
        transaction.execute();
        Assert.assertEquals(1, transaction.getAdviceList().size());
    }
}