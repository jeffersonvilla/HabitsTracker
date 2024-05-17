package com.jeffersonvilla.HabitsTracker.mapper;

public interface Mapper<T, D> {
    public D toDto(T model);
    public T fromDto(D dto);
}
