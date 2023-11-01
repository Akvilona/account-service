/**
 * Создал Андрей Антонов 10/31/2023 5:35 PM.
 **/

package ru.gmm.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.gmm.demo.controller.api.AccApi;
import ru.gmm.demo.mapper.AccMapper;
import ru.gmm.demo.model.AccEntity;
import ru.gmm.demo.model.api.AccRegistrationRq;
import ru.gmm.demo.model.api.AccRegistrationRs;
import ru.gmm.demo.model.api.AccRs;
import ru.gmm.demo.model.api.AccUpdateRq;
import ru.gmm.demo.service.AccApiService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccApiController implements AccApi {
    private final AccApiService accApiService;
    private final AccMapper accMapper;

    @Override
    public ResponseEntity<AccRegistrationRs> createAcc(final AccRegistrationRq accRegistrationRq) {
        final AccEntity accEntity = accMapper.mapToAccEntity(accRegistrationRq);
        accApiService.createAcc(accEntity);
        final AccRegistrationRs accRegistrationRs = accMapper.mapToAccRegistrationRs(accEntity);
        return ResponseEntity.ok(accRegistrationRs);
    }

    @Override
    public ResponseEntity<List<AccRs>> getAllAcc() {
        final List<AccRs> accRsList = accApiService.getAll().stream()
            .map(accMapper::mapToAccRs)
            .toList();
        return ResponseEntity.ok(accRsList);
    }

    @Override
    public ResponseEntity<AccRs> getAccById(final String id) {
        final AccEntity accEntity = accApiService.findById(id);
        final AccRs accRs = accMapper.mapToAccRs(accEntity);
        return ResponseEntity.ok(accRs);
    }

    @Override
    public ResponseEntity<AccUpdateRq> updateAcc(final String id, final AccUpdateRq accUpdateRq) {
        final AccEntity entity = accMapper.mapToAccEntity(accUpdateRq);
        final AccEntity accEntity = accApiService.updateAcc(id, entity);
        final AccUpdateRq updateRq = accMapper.mapToAccUpdateRq(accEntity);
        return ResponseEntity.ok(updateRq);
    }

    @Override
    public ResponseEntity<Void> deleteAccById(final String id) {
        accApiService.deleteAccById(Long.parseLong(id));
        return ResponseEntity.ok(null);
    }
}

