package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_BY_DATE = new Sort(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = crudUserRepository.getOne(userId);
        meal.setUser(ref);

        if (meal.isNew()) {
            return crudRepository.save(meal);
        } else {
            return get(meal.getId(), userId) == null ? null : crudRepository.save(meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteByIdAndUser_id(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getMealByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAllByUserId(userId, SORT_BY_DATE);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.getBetween(startDate, endDate, userId, SORT_BY_DATE);
    }

    @Override
    public Meal getMealWithUser(int id, int userId) throws UnsupportedOperationException {
        return crudRepository.getMealWithUser(id, userId);
    }
}
