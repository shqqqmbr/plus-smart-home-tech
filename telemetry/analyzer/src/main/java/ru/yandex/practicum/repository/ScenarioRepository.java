package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Scenario;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    void deleteByNameAndHubId(String scenarioName, String hubId);

    Set<Scenario> findByHubId(String hubId);

    Optional<Scenario> findByNameAndHubId(String name, String hubId);

}
