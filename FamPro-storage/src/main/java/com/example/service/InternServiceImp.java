package com.example.service;

import com.example.entity.FamilyMemberInfo;
import com.example.entity.InternEntity;
import com.example.enums.Assignment;
import com.example.enums.CheckStatus;
import com.example.enums.SecretLevel;
import com.example.enums.Status;
import com.example.repository.InternRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class InternServiceImp<T extends InternEntity> implements PlaceService<T> {
    private InternRepo<T> internRepo;

    @Override
    public void check(T t) {
        if (t.getAssignment() == null) t.setAssignment(Assignment.HOME);
        if (t.getStatus() == null) t.setStatus(Status.ON_LINK);
        if (t.getDescription() == null) t.setDescription("Info is absent");
        if (t.getCheckStatus() == null) t.setCheckStatus(CheckStatus.UNCHECKED);
        if (t.getSecretLevel() == null) t.setSecretLevel(SecretLevel.OPEN);
    }

    @Override
    public void checkForCommunity(T t, Set<T> other, Set<T> findInBase) {
        if ((other == null || !other.contains(t)) && findInBase.contains(t)) {
            if (t.getCheckStatus() != CheckStatus.CHECKED) {
                t.setTechString("COMMUNITY");
                t.setUuid(null);
                t.setDescription("Community. For change - check instance");
            } else if (!t.getTechString().equals("COMMUNITY")) t.setTechString("uncorrected");
        }
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
    }

    @Override
    public Set<T> getAllInternEntityByNames(Set<String> names) {
        return internRepo.findAllByInternNameIn(names);
    }

    @Override
    //для изменения полей сущности нужно либо неподвержденное, но 1 вхождение в базу. Либо изменять
    // ее должен владелец, который подтвердил свое право
    public T merge(T oldT, T newT) {
        if (!oldT.getTechString().equals("COMMUNITY") || oldT.getUuid() != null) {
            if (newT.getAssignment() != null) oldT.setAssignment(newT.getAssignment());
            if (newT.getStatus() != null) oldT.setStatus(newT.getStatus());
            if (newT.getDescription() != null) oldT.setDescription(newT.getDescription());
        }
        return oldT;
    }

    public Map<String, T> mergeSetsOfInterns(Set<T> fromDto, Set<T> fromBase, Set<T> findInBase) {
        Map<String, T> resultMap = new HashMap<>();
        if (fromBase != null && !fromBase.isEmpty())
            for (T intern : fromBase) resultMap.put(intern.getInternName(), intern);
        if (!findInBase.isEmpty())
            for (T intern : findInBase) {
                checkForCommunity(intern, fromBase, findInBase);
                resultMap.putIfAbsent(intern.getInternName(), intern);
            }
        if (fromDto != null) for (T intern : fromDto)
            if (!intern.getTechString().equals("uncorrected"))
                resultMap.merge(intern.getInternName(), intern, this::merge);
        return resultMap;
    }
}
