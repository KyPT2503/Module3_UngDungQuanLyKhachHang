package service;

import model.Customer;

import java.util.List;

public interface IGeneralService<E> {
    List<E> findAll();

    E save(E E);

    E updateById(String id, E E);
    void removeById(String id);
    E findById(String id);
}
