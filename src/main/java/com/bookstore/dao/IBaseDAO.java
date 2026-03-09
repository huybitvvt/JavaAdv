package com.bookstore.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic DAO Interface - Định nghĩa các phương thức CRUD cơ bản
 * @param <T> Entity type
 */
public interface IBaseDAO<T> {

    /**
     * Lấy tất cả bản ghi
     * @return List of entities
     */
    List<T> getAll() throws SQLException;

    /**
     * Lấy bản ghi theo ID
     * @param id Primary key
     * @return Entity object
     */
    T getById(int id) throws SQLException;

    /**
     * Thêm mới bản ghi
     * @param entity Entity to insert
     * @return true if successful
     */
    boolean insert(T entity) throws SQLException;

    /**
     * Cập nhật bản ghi
     * @param entity Entity to update
     * @return true if successful
     */
    boolean update(T entity) throws SQLException;

    /**
     * Xóa bản ghi theo ID
     * @param id Primary key
     * @return true if successful
     */
    boolean delete(int id) throws SQLException;

    /**
     * Tìm kiếm với điều kiện
     * @param keyword Search keyword
     * @return List of matching entities
     */
    List<T> search(String keyword) throws SQLException;

    /**
     * Đếm tổng số bản ghi
     * @return Total count
     */
    int count() throws SQLException;
}
