package com.example.soft.inventoryPro.Secuencia;

import com.example.soft.inventoryPro.Factura.Repository.FacturaRepository;
import com.example.soft.inventoryPro.Pedido.Repository.PedidoRepository;
import com.example.soft.inventoryPro.Secuencia.Models.Secuencia;
import com.example.soft.inventoryPro.Secuencia.Models.TipoSecuencia;
import com.example.soft.inventoryPro.Secuencia.Repository.SecuenciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Crea al arrancar una fila por cada {@link TipoSecuencia} si aún no existe.
 * Así la numeración siempre encuentra su contador y se evita la carrera de alta cuando
 * dos peticiones piden el primer número a la vez. Es idempotente: no pisa contadores ya
 * avanzados en reinicios posteriores.
 * <p>
 * El valor inicial es el número más alto ya emitido con ese prefijo (0 si no hay ninguno):
 * las bases que vienen del contador en memoria anterior ya tienen documentos como
 * FAC-751116, y arrancar en 0 podría chocar con ellos por la restricción única.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecuenciaSeeder implements ApplicationRunner {

    private final SecuenciaRepository secuenciaRepository;
    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        for (TipoSecuencia tipo : TipoSecuencia.values()) {
            if (!secuenciaRepository.existsById(tipo.name())) {
                long valorInicial = maximoEmitido(tipo);
                secuenciaRepository.save(new Secuencia(tipo.name(), valorInicial));
                log.info("Secuencia {} inicializada en {}", tipo.name(), valorInicial);
            }
        }
    }

    private long maximoEmitido(TipoSecuencia tipo) {
        List<String> numeros = switch (tipo) {
            case FACTURA -> facturaRepository.findAllNumerosFactura();
            case PEDIDO -> pedidoRepository.findAllNumerosPedido();
        };
        String prefijo = tipo.getPrefijo() + "-";
        return numeros.stream()
                .filter(numero -> numero != null && numero.startsWith(prefijo))
                .map(numero -> numero.substring(prefijo.length()))
                .filter(sufijo -> !sufijo.isEmpty() && sufijo.length() <= 18 && sufijo.chars().allMatch(Character::isDigit))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);
    }
}
