package com.example.service;

import java.util.*;
import java.util.stream.Collectors;

public interface SimpleFamilyService {
    default Set<UUID> getAllUuidFromInfo(String string) {
        if (string != null && !string.isBlank())
            return Arrays.stream(string.split(" ")).map(UUID::fromString).collect(Collectors.toSet());
        return new HashSet<>();
    }

    default boolean findUuidInInfo(String string, UUID uuid) {
        if (string != null && !string.isBlank())
            return findUuidInInfo(string, uuid.toString());
        else return false;
    }

    default String addUuidToInfo(String string, UUID uuid) {
        return addUuidToInfo(string, uuid.toString());
    }

    default Optional<String> removeUuidFromInfo(String string, UUID uuid) {
        return removeUuidFromInfo(string, uuid.toString());
    }

    default String changeUuidInInfo(String string, UUID oldUuid, UUID newUuid) {
        return changeUuidInInfo(string, oldUuid.toString(), newUuid.toString());
    }

    default String changeUuidInInfo(String string, String oldUuid, String newUuid) {
        if (string == null || string.equals("")) return "";
        List<String> uuids = new ArrayList<>(Arrays.stream(string.split(" ")).toList());
        if (uuids.contains(oldUuid)) {
            uuids.remove(oldUuid);
            uuids.add(newUuid);
            return uuids.stream().reduce((x, y) -> x.concat(" ").concat(y)).orElseThrow(()->new RuntimeException("newUuid of member is absent"));
        }
        return string;
    }

    default boolean findUuidInInfo(String string, String uuid) {
        if (string == null || string.equals("") || string.isBlank()) return false;
        return Arrays.asList(string.split(" ")).contains(uuid);
    }

    default String addUuidToInfo(String string, String uuid) {
        if (string == null) return uuid;
        else return string.concat(" ").concat(uuid);
    }

    default Optional<String> removeUuidFromInfo(String string, String uuid) {
        return Arrays.stream(string.split(" ")).filter(x -> !Objects.equals(x, uuid)).reduce((x, y) -> x.concat(" ").concat(y));
    }

    /**
     * Определение UUID которые надо удалить
     *
     * @param saved    UUID, которые надо сохранить
     * @param toRemove UUID, которые подлежат прореживанию
     * @return мапа параметров для устанавливаемой сущности
     */
    default Set<UUID> getUuidsToRemove(Set<UUID> saved, Set<UUID> toRemove) {
        Set<UUID> result = new HashSet<>();
        for (UUID u :
                saved) {
            if (!toRemove.contains(u)) result.add(u);
        }
        return result;
    }
}
