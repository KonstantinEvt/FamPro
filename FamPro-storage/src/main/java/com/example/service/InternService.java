package com.example.service;

import com.example.entity.Email;
import com.example.entity.FamilyMemberInfo;

import java.util.Map;
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
    /**
     *Слияние новой информаии family_member сущности c info ходящимся в базе данных
     * @param newFmi новая сущность
     * @param fmiFromBase новая сущность
     */
    void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase);

    Set<T> getAllInternEntityByNames(Set<String> names);
    /**
    *Замена полей сущности, находящейся в базе данных, новыми значениями
    * @param newT новая сущность
    * @param oldT сущность из базы
    * @return модифиированная сущность из базы
    */
    T merge(T oldT, T newT);
    /**
     *Замена полей сущности, находящейся в базе данных, новыми значениями
     * @param fromDto set параметров новой сущности
     * @param fromBase set параметров сущности из базы
     * @param findInBase set параметров содержащихся во всей базе
     * @return мапа параметров для устанавливаемой сущности
     */
    Map<String,T> mergeSetsOfInterns(Set<T> fromDto, Set<T> fromBase, Set<T> findInBase);
    /**
     *Проверка сущности, на соответствие иным сущностям ходящимся в базе данных
     * @param t новая сущность
     */
    void checkForCommunity(T t);
}