package com.app.zhardem.services;


public interface CrudService<ENTITY, REQUEST, RESPONSE> {

    RESPONSE getById(long id);
    RESPONSE create(REQUEST requestDto);
    RESPONSE update(long id, REQUEST requestDto);
    void delete(long id);

    ENTITY getEntityById(long id);

}
