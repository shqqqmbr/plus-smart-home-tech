package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.ScenarioCondition;

import java.util.Set;

public interface ConditionRepository extends JpaRepository<ScenarioCondition, Long> {

    void deleteAllByConditionIdIn(Set<Long> ids);
}
