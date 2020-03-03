package com.weljak.welvet.webapi;

public class Endpoints {
    public static final String OWNER_ENDPOINT = "/owner";
    public static final String CREATE_OWNER_ENDPOINT = OWNER_ENDPOINT + "/create";
    public static final String CURRENT_OWNER_DETAILS_ENDPOINT = OWNER_ENDPOINT + "/me";

    public static final String ANIMAL_ENDPOINT = "/animal";
    public static final String CREATE_ANIMAL_ENDPOINT = ANIMAL_ENDPOINT + "/create";
    public static final String GET_ANIMAL_ENDPOINT = ANIMAL_ENDPOINT + "/{id}";
    public static final String GET_CURRENT_OWNER_ALL_ANIMALS = ANIMAL_ENDPOINT + "/all";
}
