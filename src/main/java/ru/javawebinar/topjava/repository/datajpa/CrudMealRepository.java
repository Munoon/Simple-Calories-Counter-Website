package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED) // REQUIRED используеться по умолчанию, я написал для наглядности
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    int deleteByIdAndUser_id(int id, int userId);

    @Override
    Meal save(Meal meal);

    Meal getMealByIdAndUserId(int id, int userId);

    List<Meal> getAllByUserId(int userId, Sort sort);

    // Пробовал сделать по названию, не получилось, да и название выходит длинное
    @Query("SELECT meal FROM Meal meal WHERE meal.user.id=:userId AND meal.dateTime BETWEEN :startDate AND :endDate")
    List<Meal> getBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") int userId, Sort sort);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId")
    Meal getMealWithUser(@Param("id") int id, @Param("userId") int userId);
}
