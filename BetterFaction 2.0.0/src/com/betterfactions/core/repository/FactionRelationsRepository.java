package com.betterfactions.core.repository;

import com.betterfactions.core.domain.FactionRelation;

import java.util.Map;

public interface FactionRelationsRepository {

    void setRelation(String f1, String f2, FactionRelation relation);

    FactionRelation getRelation(String f1, String f2);

    Map<String, Map<String, FactionRelation>> getAllRelations();

    void setAllRelations(Map<String, Map<String, FactionRelation>> data);
}