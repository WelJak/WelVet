package com.weljak.welvet.webapi;

public class Endpoints {
    public static final String OWNER_ENDPOINT = "/owner";
    public static final String CREATE_OWNER_ENDPOINT = OWNER_ENDPOINT + "/create";
    public static final String CURRENT_OWNER_DETAILS_ENDPOINT = OWNER_ENDPOINT + "/me";

    public static final String ANIMAL_ENDPOINT = "/animal";
    public static final String CREATE_ANIMAL_ENDPOINT = ANIMAL_ENDPOINT + "/create";
    public static final String GET_ANIMAL_ENDPOINT = ANIMAL_ENDPOINT + "/{id}";
    public static final String GET_CURRENT_OWNER_ALL_ANIMALS = ANIMAL_ENDPOINT + "/all";

    public static final String VET_ENDPOINT = "/vet";
    private static final String VET_GET_OWNER_ENDPOINT = VET_ENDPOINT + "/owner";
    public static final String VET_GET_OWNER_BY_USERNAME_ENDPOINT = VET_GET_OWNER_ENDPOINT + "/{username}";
    public static final String VET_GET_OWNER_UUID_ENDPOINT = VET_GET_OWNER_ENDPOINT + "/uuid";
    public static final String VET_GET_OWNER_BY_UUID_ENDPOINT = VET_GET_OWNER_UUID_ENDPOINT + "/{uuid}";
    private static final String VET_GET_ANIMAL_ENDPOINT = VET_ENDPOINT + "/animal";
    public static final String VET_GET_ANIMAL_BY_ID_ENDPOINT = VET_GET_ANIMAL_ENDPOINT + "/{animalId}";
    public static final String VET_CHANGE_ANIMAL_TREATMENT_ENDPOINT = VET_GET_ANIMAL_ENDPOINT + "/treatment";


}
