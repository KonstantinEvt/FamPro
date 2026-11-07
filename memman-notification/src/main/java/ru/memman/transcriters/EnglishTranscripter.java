package ru.memman.transcriters;


import ru.memman.enums.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class EnglishTranscripter implements AbstractTranscripter {
    public static final Map<String, String> matrixOfChange = new HashMap<>();
    public final Map<String, String> matrixOfTextGeneration = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE.name().concat("1"), "The main information entered:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE.name().concat("2"), "<br>corresponds to another person in the database. Data entry has been cancelled.<br><strong>ID</strong> of person: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_HIDE.name().concat("1"), "The main information entered:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_HIDE.name().concat("2"), "<br>corresponds to the alternative name of another person in the database. Data entry has been cancelled.<br><strong>ID</strong> of person: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER.name().concat("1"), "The entered alternative name:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER.name().concat("2"), "<br>identical to the alternative name of another person in the database. This name has been ignored.<br><strong>ID</strong> of person: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE.name().concat("1"), "The entered alternative name:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE.name().concat("2"), "<br>corresponds to another person in the database. This name has been ignored.<br><strong>ID</strong> of person: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HARD.name().concat("1"), "The entered or former alternative name:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HARD.name().concat("2"), "<br>combined with the change in the date of birth, it is identical to an alternative name of another person in the database. Data entry has been cancelled.<br><strong>ID</strong> of person: "),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE_HARD.name().concat("1"), "The entered or former alternative name:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.DUPLICATE_OTHER_HIDE_HARD.name().concat("2"), "<br>combined with the change in the date of birth, it is corresponds to another person in the database. Data entry has been cancelled.<br><strong>ID</strong> of person: "),
            new AbstractMap.SimpleEntry<>(Subject.RIGHTS.name().concat("1"), "You do not have the right to take any action regarding the person:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.RIGHTS.name().concat("2"), "<br>Data entry has been cancelled."),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_CHILD.name().concat("1"), "The found child:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_CHILD.name().concat("2"), "<br>is under moderation/voting. Data entry has been cancelled. Please try again later.<br><strong>ID</strong> of child: "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_FATHER.name().concat("1"), "The found father:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_FATHER.name().concat("2"), "<br>is under moderation/voting. This entry position has been ignored. Please try again later.<br><strong>ID</strong> of father: "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_MOTHER.name().concat("1"), "The found mother:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_MOTHER.name().concat("2"), "<br>is under moderation/voting. This entry position has been ignored. Please try again later.<br><strong>ID</strong> of mother:  "),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_WARNING.name().concat("1"), "Person:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.MODERATION_WARNING.name().concat("2"), "<br>is under moderation/voting. Data entry has been cancelled. Please try again later.<strong>ID</strong> of person: "),
            new AbstractMap.SimpleEntry<>(Subject.LAST_UPDATE.name().concat("1"), "Person:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.LAST_UPDATE.name().concat("2"), "<br>between your requests has changed. Requests has been aborted. Please repeat them from the beginning.<strong>ID</strong> of person: "),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_DATE.name().concat("1"), "The entered dates of birth of the parent(s):<br>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_DATE.name().concat("2"), "<br>cannot correspond to reality.This entry position(s) has been ignored."),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_SEX.name().concat("1"), "Mistake in the entered person field:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_SEX.name().concat("2"), "<br>Data entry has been cancelled."),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_PARENTS.name().concat("1"), "Parents cannot have such names:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_PARENTS.name().concat("2"), "<br>Data entry has been cancelled."),
            new AbstractMap.SimpleEntry<>(Subject.VOTING.name().concat("1"), "Voting has begun to determine the presence of a relationship between: <br>"),
            new AbstractMap.SimpleEntry<>(Subject.VOTING.name().concat("2"), "<br>and<br>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_OTHER.name().concat("1"), "The entered alternative name:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.WRONG_INFO_OTHER.name().concat("2"), "<br>is uncorrected."),
            new AbstractMap.SimpleEntry<>(Subject.LINK.name().concat("1"), "You requested to be associated with a person:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.LINK.name().concat("2"), "<br>Voting has begun."),
            new AbstractMap.SimpleEntry<>(Subject.LINK_POSITIVE.name().concat("1"), "Successful associated of the user with the person:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.LINK_POSITIVE.name().concat("2"), "<br>Congratulate."),
            new AbstractMap.SimpleEntry<>(Subject.LINK_NEGATIVE.name().concat("1"), "Association of the user with the person:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.LINK_NEGATIVE.name().concat("2"), "<br>is failed"),
            new AbstractMap.SimpleEntry<>(Subject.NOT_FULLY.name().concat("1"), "The information entered:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.NOT_FULLY.name().concat("2"), "<br>is not fully."),
            new AbstractMap.SimpleEntry<>("USER_NICK","User with nickname <em>(on request moment)</em>: <strong>"),
            new AbstractMap.SimpleEntry<>("USER_NICK1","</strong> want "),
            new AbstractMap.SimpleEntry<>("USER_NICK2","linking himself with person:<br>"),
            new AbstractMap.SimpleEntry<>("USER_NICK3","set relationship between:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.END_VOTING.name(), "The family members have completed the voting based on the request of user with nickname <em>(на момент запроса)</em>: <strong>"),
            new AbstractMap.SimpleEntry<>(Subject.END_VOTING.name().concat("1"), "</strong> which wanted "),
            new AbstractMap.SimpleEntry<>(Subject.END_VOTING.name().concat("2"), "linking himself with person:<br>"),
            new AbstractMap.SimpleEntry<>(Subject.END_VOTING.name().concat("3"), "set relationship between:<br>"),
            new AbstractMap.SimpleEntry<>(Attention.LINK_REQUESTER.name().concat("1"), "The family members have completed the voting based on the your request for linking with person:<br>"),
            new AbstractMap.SimpleEntry<>(Attention.LINK_REQUESTER.name().concat("2"), "<br>Waiting result."),
            new AbstractMap.SimpleEntry<>(Attention.CONTACT_REQUESTER.name().concat("1"), "User:<br>"),
            new AbstractMap.SimpleEntry<>(Attention.CONTACT_REQUESTER.name().concat("2"), "<br>has reviewed your request for contact. Waiting result."),
            new AbstractMap.SimpleEntry<>(Attention.VOTING_REQUESTER.name().concat("1"), "The family members have completed the voting based on the your request for set relationship between:<br>"),
            new AbstractMap.SimpleEntry<>(Attention.VOTING_REQUESTER.name().concat("2"), "<br>Waiting result.")
    );

    @Override
    public String transcritWordToLocalisation(String str) {
        return str;
    }

    @Override
    public String transcritWordFromLocalisation(String str) {
        return str;
    }

    @Override
    public String getAbsentLocalisation() {
        return "<em>(This message have not your localisation)</em><br>";
    }

    @Override
    public String getAbsent() {
        return "absent in database parents";
    }
    @Override
    public String getElection() {
        return "Are you agree with this operation?";
    }
    @Override
    public String getUser() {
        return "User";
    }
    @Override
    public String getUnknown() {
        return "Unknown";}
    @Override
    public String getPerson() {
        return "person";
    }
    @Override
    public String getRequest() {
        return "Your request ";
    }
    @Override
    public String getInfoNotFully() {
        return CheckStatus.NOT_FULLY.getComment();
    }

    @Override
    public String getIncorrectInfo() {
        return CheckStatus.UNCORRECTED.getComment();
    }

    @Override
    public String getBirthdayString() {
        return " . Birthday: ";
    }

    @Override
    public String getOut() {
        return "Information is absent";
    }

    @Override
    public String getClose() {
        return "Hidden";
    }

    @Override
    public String empty() {
        return "<empty>";
    }

    @Override
    public Map<String, String> getMatrixOfChange() {
        return matrixOfChange;
    }

    @Override
    public String getTextSubject(Subject subject) {
        return subject.getCommit();
    }

    @Override
    public Map<String, String> getMatrixOfTextGeneration() {
        return matrixOfTextGeneration;
    }

    @Override
    public String getTextSwitchPosition(SwitchPosition switchPosition) {
        return switchPosition.getCommit();
    }

    @Override
    public String getAnd() {
        return " and ";
    }

    @Override
    public String getRelation(Relation relation) {
        return relation.getCommit();
    }
}

