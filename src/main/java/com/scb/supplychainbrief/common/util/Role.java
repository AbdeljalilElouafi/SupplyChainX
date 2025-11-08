package com.scb.supplychainbrief.common.util;

public enum Role {

    ADMIN,

    // Approvisionnement
    GESTIONNAIRE_APPROVISIONNEMENT,
    RESPONSABLE_ACHATS,
    SUPERVISEUR_LOGISTIQUE,

    // Production
    CHEF_PRODUCTION,
    PLANIFICATEUR,
    SUPERVISEUR_PRODUCTION,

    // Livraison
    GESTIONNAIRE_COMMERCIAL,
    RESPONSABLE_LOGISTIQUE,
    SUPERVISEUR_LIVRAISONS
}