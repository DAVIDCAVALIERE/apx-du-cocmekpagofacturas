
package com.bbva.cmek.lib.rme1.impl;

import com.bbva.apx.exception.db.NoResultException;
import com.bbva.cmek.dto.account.AccountDTO;
import com.bbva.cmek.dto.bill.BillDTO;
import com.bbva.cmek.dto.bill.PaymentDTO;
import com.bbva.cmek.lib.rme2.CMEKRME2;
import com.bbva.elara.domain.transaction.Advice;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.elara.utility.jdbc.JdbcUtils;
import com.datiobd.daas.DaasMongoConnector;
import com.datiobd.daas.Parameters;
import com.datiobd.daas.conf.MongoConnectorException;
import com.datiobd.daas.model.DocumentWrapper;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class CMEKRME1ImplTest {


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
     *  private CMEKRME1Impl cmekRME1 = new CMEKRME1Impl() {
     *     @Override
     *     protected CommonRequestHeader getRequestHeader() {
     *        return commonRequestHeader;
     *     }
     *  };
     */
    //Generalmente se define todas las variables relacionadas con utilidades que estan en la clase abstracta
    @Mock
    private JdbcUtils jdbcUtils;
    @Mock
    private CMEKRME2 cmekRME2;
    @Mock
    private DaasMongoConnector daasMongoConnector;

    //Se inicializa el objeto pasando todos los Mock a traves de get de variables
    @InjectMocks
    private CMEKRME1Impl cmekRME1 = new CMEKRME1Impl() {

        JdbcUtils getJdbcUtils() {
            return jdbcUtils;
        }

        CMEKRME2 getCmekRME2() {
            return cmekRME2;
        }

        DaasMongoConnector getDaasMongoConnector() {
            return daasMongoConnector;
        }

    };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ThreadContext.set(new Context());
    }

    @Test
    public void executeTest() {
        // when(commonRequestHeader.getHeaderParameter(RequestHeaderParamsName.COUNTRYCODE)).thenReturn("ES");
        // when(applicationConfigurationService.getProperty("config.property")).thenReturn("value");
        // when(qwaiR001.execute()).thenReturn(listCustomerDTO);
        cmekRME1.setApplicationConfigurationService(null);

        //Se crea el objeto de entrada que se enviara al metodo
        PaymentDTO request = new PaymentDTO();

        //Se inicializan los datos del pago de prueba
        request.setBill(new BillDTO());
        request.getBill().setAmount(10000);
        request.getBill().setNumber("125498");
        request.setAccount(new AccountDTO());
        request.getAccount().setId("00130037000200140761");
        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);
        //Se obtiene la lista de errores
        List<Advice> advices = cmekRME1.getAdviceList();
        Assert.assertEquals(2, advices.size());
        //Se desocupa la lista para no afectar los siguientes escenarios
        cmekRME1.getAdviceList().clear();

        //Se define la consulta como null
        when(jdbcUtils.queryForMap(any(), anyString())).thenReturn(null);

        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);
        //Se obtiene la lista de errores
        advices = cmekRME1.getAdviceList();
        Assert.assertEquals(2, advices.size());
        //Se desocupa la lista para no afectar los siguientes escenarios
        cmekRME1.getAdviceList().clear();

        Date currentDate = new Date();

        //Se incluye una factura no valida
        Map<String, Object> billRecord = new HashMap<>();
        billRecord.put("BILL_ID", request.getBill().getNumber());
        billRecord.put("GENERATION_DATE", currentDate);
        billRecord.put("EXPIRATION_DATE", currentDate);
        billRecord.put("BILL_AMOUNT", new BigDecimal(1000L));
        billRecord.put("BILL_STATUS", "VENCIDO");
        billRecord.put("BILL_USER", "TEST");
        billRecord.put("BILL_PROVIDER", "PROVEEDOR");
        billRecord.put("BILL_UPDATE_DATE", currentDate);
        //Se define la consulta
        when(jdbcUtils.queryForMap(any(), anyString())).thenReturn(billRecord);

        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);
        //Se obtiene la lista de errores
        advices = cmekRME1.getAdviceList();
        Assert.assertEquals(1, advices.size());
        //Se desocupa la lista para no afectar los siguientes escenarios
        cmekRME1.getAdviceList().clear();

        //---- Escenario Factura Vigente pero con fecha expiracion anterior
        //Se incluye una factura no valida
        billRecord.replace("BILL_STATUS", "VIGENTE");

        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);
        //Se obtiene la lista de errores
        advices = cmekRME1.getAdviceList();
        Assert.assertEquals(1, advices.size());
        //Se desocupa la lista para no afectar los siguientes escenarios
        cmekRME1.getAdviceList().clear();

        //---- Escenario Factura Vigente pero con fecha expiracion posterior
        Calendar calendar = Calendar.getInstance();
        calendar.set(2030, Calendar.JANUARY, 1, 0, 0, 0); // 1 de enero de 2030
        Date expirationDate = calendar.getTime();

        billRecord.replace("EXPIRATION_DATE", expirationDate);

        //Se deja fija la respuesta de mongo para que agregue el id del objeto al
        //insertarlo en la base de datos
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Obtener el parámetro mongoDbParameters
                Map<String, Object> args = invocation.getArgumentAt(1, Map.class);

                // Obtener el DocumentWrapper
                DocumentWrapper documentWrapper = (DocumentWrapper) args.get(Parameters.DOCUMENT);

                // Verificar si el DocumentWrapper es nulo
                if (documentWrapper == null) {
                    documentWrapper = new DocumentWrapper(new Document());
                    args.put(Parameters.DOCUMENT, documentWrapper);
                }

                // Agregar el _id al DocumentWrapper
                documentWrapper.put("_id", new ObjectId());

                args.replace(Parameters.DOCUMENT, documentWrapper);
                return null;
            }
        }).when(daasMongoConnector).executeWithNoReturn(any(), anyMap());

        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);
        //Se obtiene la lista de errores
        advices = cmekRME1.getAdviceList();
        Assert.assertEquals(2, advices.size());
        //Se desocupa la lista para no afectar los siguientes escenarios
        cmekRME1.getAdviceList().clear();

        //---- Escenario Factura Vigente pero con fecha expiracion posterior
        billRecord.replace("BILL_AMOUNT", new BigDecimal(request.getBill().getAmount()));

        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);
        //Se obtiene la lista de errores
        advices = cmekRME1.getAdviceList();
        Assert.assertEquals(1, advices.size());
        //Se desocupa la lista para no afectar los siguientes escenarios
        cmekRME1.getAdviceList().clear();

        //---- Escenario Factura Valida y cuenta valida
        when(cmekRME2.executeValidateAccountToPayment(anyString(), anyLong())).thenReturn(true);
        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);
        //Se obtiene la lista de errores
        advices = cmekRME1.getAdviceList();
        Assert.assertEquals(0, advices.size());
        //Se desocupa la lista para no afectar los siguientes escenarios
        cmekRME1.getAdviceList().clear();

        //---- Escenario Actualizacion registro retorna 1
        when(jdbcUtils.update(any(), anyMap())).thenReturn(1);

        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);

        verify(daasMongoConnector, times(2)).executeWithNoReturn(any(), anyMap());
        //Se obtiene la lista de errores
        advices = cmekRME1.getAdviceList();
        Assert.assertEquals(0, advices.size());
        //Se desocupa la lista para no afectar los siguientes escenarios
        cmekRME1.getAdviceList().clear();

        //---- Escenario Mongo Genera error de conexion
        doThrow(new MongoConnectorException()).when(daasMongoConnector).executeWithNoReturn(any(), anyMap());
        //se ejecuta el pago
        cmekRME1.executeDoBillPayment(request);
        Assert.assertEquals(0, advices.size());

        //---- Datos no encontrados en BD Oracle
        when(jdbcUtils.queryForMap(any(), anyString())).thenThrow(new NoResultException(""));

        cmekRME1.executeDoBillPayment(request);
        Assert.assertEquals(2, advices.size());
    }
}

