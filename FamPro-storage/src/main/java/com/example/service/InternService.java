package com.example.service;

import com.example.entity.Email;
import com.example.entity.FamilyMemberInfo;

import java.util.Set;
import java.util.UUID;

public interface InternService<T> {
    /**
     *Замена полей сущности, находящейся в базе данных, новыми значениями
     * @param t сущность, подвергаемая проверке и изменению, согласно прописанным для нее правилам
     * Если в результате проверки основное поле сущности будет установлено "uncorrected",
     * сущность будет исключена из дальнейшего процесса
     */
     void check(T t);

    void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase);

    Set<T> getAllInternEntityByNames(Set<String> names);
/**
*Замена полей сущности, находящейся в базе данных, новыми значениями
 * @param newT новая сущность
 * @param oldT сущность из базы
 * @return модифиированная сущность из базы
 */
    T merge(T oldT, T newT);
}