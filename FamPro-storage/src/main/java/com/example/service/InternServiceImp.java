package com.example.service;

import com.example.entity.FamilyMemberInfo;
import com.example.entity.InternEntity;

import java.util.Set;

public class InternServiceImp<T> implements InternService<T>{
    @Override
    public void check(T t) {
        System.out.println("Проверяю");
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
        return oldT;
    }
}
