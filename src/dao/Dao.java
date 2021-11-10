package dao;

import javafx.collections.ObservableList;

import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id);

    Optional<T> getBy(String field, String value);

    ObservableList<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
