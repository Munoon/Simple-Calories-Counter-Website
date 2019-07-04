package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    // почему-то у меня deleteByIdAndUserId не сработало, пришлось писать запрос
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Modifying
    @Override
    Meal save(Meal meal);

    Meal getMealByIdAndUserId(int id, int userId);

    List<Meal> getAllByUserId(int userId, Sort sort);

    // Пробовал сделать по названию, не получилось
    @Query("SELECT meal FROM Meal meal WHERE meal.user.id=:userId AND meal.dateTime BETWEEN :startDate AND :endDate")
    List<Meal> getBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") int userId, Sort sort);
}
