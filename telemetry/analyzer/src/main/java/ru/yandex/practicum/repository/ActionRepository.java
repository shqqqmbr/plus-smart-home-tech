package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Action;

import java.util.Set;

public interface ActionRepository extends JpaRepository<Action, Long> {

    void deleteAllByActionIdIn(Set<Long> actionIds);

}
