package com.example.soft.inventoryPro.Secuencia.Service.Impl;

import com.example.soft.inventoryPro.Secuencia.Models.Secuencia;
import com.example.soft.inventoryPro.Secuencia.Models.TipoSecuencia;
import com.example.soft.inventoryPro.Secuencia.Repository.SecuenciaRepository;
import com.example.soft.inventoryPro.Secuencia.Service.NumeracionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NumeracionServiceImpl implements NumeracionService {

    private final SecuenciaRepository secuenciaRepository;

    @Override
    @Transactional
    public String siguiente(TipoSecuencia tipo) {
        // El seeder garantiza la fila al arrancar; el orElseGet es una red de seguridad.
        Secuencia secuencia = secuenciaRepository.findByTipoForUpdate(tipo.name())
                .orElseGet(() -> secuenciaRepository.save(new Secuencia(tipo.name(), 0L)));

        long nuevoValor = secuencia.getValor() + 1;
        secuencia.setValor(nuevoValor);
        secuenciaRepository.save(secuencia);

        return tipo.getPrefijo() + "-" + String.format("%06d", nuevoValor);
    }
}
