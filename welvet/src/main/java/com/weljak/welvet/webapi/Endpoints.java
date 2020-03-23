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

    private static final String APPOINTMENT_ENDPOINT = "/appointment";
    public static final String GET_APPOINTMENT_ENDPOINT = APPOINTMENT_ENDPOINT + "/{uuid}";
    private static final String VET_APPOINTMENT_ENDPOINT = VET_ENDPOINT + APPOINTMENT_ENDPOINT;
    public static final String VET_GET_ALL_APPOINTMENTS_ENDPOINT = VET_APPOINTMENT_ENDPOINT + "/all";
    private static final String VET_DELETE_APPOINTMENT_ENDPOINT = VET_APPOINTMENT_ENDPOINT + "/delete";
    public static final String VET_DELETE_APPOINTMENTS_ENDPOINT = VET_DELETE_APPOINTMENT_ENDPOINT + "/{uuid}";
    private static final String VET_MARK_APPOINTMENT_COMPLETION_ENDPOINT = VET_APPOINTMENT_ENDPOINT + "/complete";
    public static final String VET_MARK_APPOINTMENTS_COMPLETION_ENDPOINT = VET_MARK_APPOINTMENT_COMPLETION_ENDPOINT + "/{uuid}";
    public static final String VET_POSTPONE_APPOINTMENT_ENDPOINT = VET_APPOINTMENT_ENDPOINT + "/postpone";
    private static final String CANCEL_APPOINTMENT_ENDPOINT = APPOINTMENT_ENDPOINT + "/cancel";
    public static final String CANCEL_APPOINTMENTS_ENDPOINT = CANCEL_APPOINTMENT_ENDPOINT + "/{uuid}";

    private static final String APPOINTMENT_REQUEST_ENDPOINT = APPOINTMENT_ENDPOINT + "/request";
    private static final String OWNER_APPOINTMENT_REQUEST_ENDPOINT = OWNER_ENDPOINT + APPOINTMENT_REQUEST_ENDPOINT;
    private static final String VET_APPOINTMENT_REQUEST_ENDPOINT = VET_ENDPOINT + APPOINTMENT_REQUEST_ENDPOINT;
    public static final String GET_APPOINTMENT_REQUEST_DETAILS_ENDPOINT = APPOINTMENT_REQUEST_ENDPOINT + "/{uuid}";
    public static final String CREATE_APPOINTMENT_REQUEST_ENDPOINT = OWNER_APPOINTMENT_REQUEST_ENDPOINT + "/create";
    private static final String CONFIRM_APPOINTMENT_REQUEST_ENDPOINT = VET_APPOINTMENT_REQUEST_ENDPOINT + "/confirm";
    public static final String CONFIRM_APPOINTMENT_REQUESTS_ENDPOINT = CONFIRM_APPOINTMENT_REQUEST_ENDPOINT + "/{uuid}";
    private static final String CANCEL_APPOINTMENT_REQUEST_ENDPOINT = APPOINTMENT_REQUEST_ENDPOINT + "/cancel";
    public static final String CANCEL_APPOINTMENT_REQUESTS_ENDPOINT = CANCEL_APPOINTMENT_REQUEST_ENDPOINT + "/{uuid}";
    public static final String OWNER_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT = OWNER_APPOINTMENT_REQUEST_ENDPOINT + "/all";
    public static final String VET_GET_PENDING_APPOINTMENT_REQUESTS_ENDPOINT = VET_APPOINTMENT_REQUEST_ENDPOINT + "/all";


}
