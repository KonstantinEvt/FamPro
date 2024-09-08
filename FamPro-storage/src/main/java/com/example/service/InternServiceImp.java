package com.example.service;

import com.example.entity.FamilyMemberInfo;
import com.example.entity.InternEntity;
import com.example.enums.Assignment;
import com.example.enums.CheckStatus;
import com.example.enums.Status;
import com.example.repository.InternRepo;
import com.example.utils.StringValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@AllArgsConstructor
@Getter
@Setter
public class InternServiceImp<T extends InternEntity> implements InternService<T> {
    private InternRepo<T> internRepo;
    @Override
    public void check(T t) {
        if (t.getAssignment() == null) t.setAssignment(Assignment.HOME);
        if (t.getStatus() == null) t.setStatus(Status.ON_LINK);
        if (t.getDescription() == null) t.setDescription("Доп.инфо остутствует");
        else StringValidation.checkForSwears(t.getDescription());
        t.setCheckStatus(CheckStatus.UNCHECKED);
    }
    void checkForCommunity(T t){}

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
    }

    @Override
    public Set<T> getAllInternEntityByNames(Set<String> names) {
        return internRepo.findAllByInternNameIn(names);
    }

    @Override
    public T merge(T oldT, T newT) {
        if (newT.getAssignment() != null) oldT.setAssignment(newT.getAssignment());
        if (newT.getStatus() != null) oldT.setStatus(newT.getStatus());
        if (newT.getDescription() != null) oldT.setDescription(newT.getDescription());
        return oldT;
    }

    public Map<String,T> mergeSetsOfInterns(Set<T> fromDto, Set<T> fromBase, Set<T> findInBase) {
        Map<String, T> resultMap = new HashMap<>();
        if (fromBase != null && !fromBase.isEmpty())
            for (T intern : fromBase) resultMap.put(intern.getInternName(), intern);
        if (!findInBase.isEmpty())
            for (T intern : findInBase) {checkForCommunity(intern); resultMap.putIfAbsent(intern.getInternName(), intern);}
        for (T intern : fromDto)
            if (!intern.getTechString().equals("uncorrected"))
                resultMap.merge(intern.getInternName(), intern, this::merge);
        return resultMap;
    }
}
