package com.example.service;

import com.example.entity.Email;
import com.example.entity.FamilyMemberInfo;
import com.example.entity.InternEntity;
import com.example.enums.Assignment;
import com.example.enums.Status;
import com.example.utils.StringValidation;

import java.util.*;

public class InternServiceImp<T extends InternEntity> implements InternService<T> {
    @Override
    public void check(T t) {
        if (t.getAssignment() == null) t.setAssignment(Assignment.HOME);
        if (t.getStatus() == null) t.setStatus(Status.ON_LINK);
        if (t.getDescription() == null) t.setDescription("Доп.инфо остутствует");
        else StringValidation.checkForSwears(t.getDescription());
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
    }

    @Override
    public Set<T> getAllInternEntityByNames(Set<String> names) {
        return null;
    }

    @Override
    public T merge(T oldT, T newT) {
        if (newT.getAssignment() != null) oldT.setAssignment(newT.getAssignment());
        if (newT.getStatus() != null) oldT.setStatus(newT.getStatus());
        if (newT.getDescription() != null) oldT.setDescription(newT.getDescription());
        return oldT;
    }

    public Collection<T> mergeSetsOfInterns(Set<T> fromDto, Set<T> fromBase, Set<T> findInBase) {
        Map<String, T> resultMap = new HashMap<>();
        if (fromBase != null && !fromBase.isEmpty())
            for (T intern : fromBase) resultMap.put(intern.getTechString(), intern);
        if (!findInBase.isEmpty())
            for (T intern : findInBase) resultMap.putIfAbsent(intern.getTechString(), intern);
        for (T intern : fromDto)
            if (!intern.getTechString().equals("uncorrected"))
                resultMap.merge(intern.getTechString(), intern, this::merge);
        return resultMap.values();
    }
}
