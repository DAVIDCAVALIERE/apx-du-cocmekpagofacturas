
package com.bbva.cmek;

import com.bbva.cmek.dto.bill.PaymentDTO;
import com.bbva.cmek.lib.rme1.CMEKRME1;
import com.bbva.elara.domain.transaction.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoice payment transaction charged to account.
 */
public class CMEKTME101COTransaction extends AbstractCMEKTME101COTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMEKTME101COTransaction.class);

    /**
     * The execute method...
     */
    @Override
    public void execute() {
        //Definicion por defecto que hace la arquitectura al agregar la dependencia de la libreria
        CMEKRME1 cmekRME1 = this.getServiceLibrary(CMEKRME1.class);

        //Obtener la variable payment que se definio como entrada de la transaccion y se homologa
        //al objeto de entrada del metodo de la libreria a utiliza. No se requiere validacion sobre
        //los datos de la entrada de la transaccion porque se supone ya la hizo la arquitectura.
        //Todos los parametros de entrada de la transaccion que estan en la raiz / tiene un get
        PaymentDTO request = getPayment();

        //Ejecutar el metodo de la libreria y recibir la respuesta del metodo.
        PaymentDTO response = cmekRME1.executeDoBillPayment(request);

        //Se sugiere incluir la siguiente validacion para manejar la respuesta y la severidad o estatus
        //Si hay algun advice o error controlado se genera una respuesta con error, que retornara los
        //codigos configurado
        if (!getAdviceList().isEmpty()) {
            //Si se requiere una logica adicional se incluye aca
            this.setSeverity(Severity.ENR);
        } else {
            //Si no hay errores, se homologa la respuesta de la libreria a la salida de la transaccion
            //Todos los parametros de salida de la transaccion que estan en la raiz / tiene un set
            setPayment(response);
        }
    }

}

