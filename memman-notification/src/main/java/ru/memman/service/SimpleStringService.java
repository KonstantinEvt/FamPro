package ru.memman.service;

import java.util.*;
import java.util.stream.Collectors;

public interface SimpleStringService {
    default Set<UUID> getAllUuidFromInfo(String string) {
        if (string != null && !string.isBlank())
            return Arrays.stream(string.split(" ")).map(UUID::fromString).collect(Collectors.toSet());
        return new HashSet<>();
    }

    default Set<String> getAllStringUuidFromInfo(String string) {
        if (string != null && !string.isBlank())
            return Arrays.stream(string.split(" ")).collect(Collectors.toSet());
        return new HashSet<>();
    }
    default String getInfoStringFromStringUuids(Set<String> strings) {
        if (strings == null || strings.isEmpty()) return null;
        return strings.stream().reduce((x, y) -> x.concat(" ").concat(y)).orElseThrow(() -> new RuntimeException("collect Info error"));
    }
    default String changeUuidInInfo(String string, String oldUuid, String newUuid) {
        if (string == null || string.equals("")) return "";
        List<String> uuids = new ArrayList<>(Arrays.stream(string.split(" ")).toList());
        if (uuids.contains(oldUuid)) {
            uuids.remove(oldUuid);
            uuids.add(newUuid);
            return uuids.stream().reduce((x, y) -> x.concat(" ").concat(y)).orElseThrow(() -> new RuntimeException("newUuid of member is absent"));
        }
        return string;
    }

    default String mergeInfo(String donor, String merged) {
        if ((donor == null || donor.isBlank()) && (merged == null || merged.isBlank())) return null;
        if (donor == null || donor.isBlank()) return merged;
        if (merged == null || merged.isBlank()) return donor;
        else {
            Set<String> result = Arrays.stream(merged.split(" ")).collect(Collectors.toSet());
            result.addAll(Arrays.stream(donor.split(" ")).collect(Collectors.toSet()));
            return result.stream().reduce((x, y) -> x.concat(" ").concat(y)).orElseThrow(() -> new RuntimeException("merge Info error"));
        }
    }
    default String mergeInfo(Set<String> donor, String merged) {
        if ((donor == null || donor.isEmpty()) && (merged == null || merged.isBlank())) return null;
        if (donor == null || donor.isEmpty()) return merged;
        if (merged == null || merged.isEmpty()) return getInfoStringFromStringUuids(donor);
        else {
            Set<String> result = Arrays.stream(merged.split(" ")).collect(Collectors.toSet());
            result.addAll(donor);
            return result.stream().reduce((x, y) -> x.concat(" ").concat(y)).orElseThrow(() -> new RuntimeException("merge Info error"));
        }
    }
    default boolean findUuidInInfo(String string, String uuid) {
        if (string == null || string.equals("") || string.isBlank()) return false;
        return Arrays.asList(string.split(" ")).contains(uuid);
    }

    default String addUuidToInfo(String string, String uuid) {
        if (string == null || string.isBlank()) return uuid;
        if (uuid == null || uuid.isBlank()) return string;
        else return string.concat(" ").concat(uuid);
    }

    default Optional<String> removeUuidFromInfo(String string, String uuid) {
        if (string == null || string.isBlank()) return Optional.empty();
        if (uuid == null || uuid.isBlank()) return Optional.of(string);
        return Arrays.stream(string.split(" ")).filter(x -> !Objects.equals(x, uuid)).reduce((x, y) -> x.concat(" ").concat(y));
    }

}
