package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.ScenarioCondition;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;

    public Optional<Scenario> findScenarioByNameAndHubId(String name, String hubId) {
        return scenarioRepository.findByNameAndHubId(name, hubId);
    }

    @Transactional
    public Scenario add(Scenario scenario) {
        establishBidirectionalRelationships(scenario);
        return scenarioRepository.save(scenario);
    }

    @Transactional
    public Scenario upsert(Scenario scenario) {
        establishBidirectionalRelationships(scenario);
        scenarioRepository.findByNameAndHubId(scenario.getName(), scenario.getHubId())
                .ifPresent(scenarioRepository::delete);
        return scenarioRepository.save(scenario);
    }

    @Transactional
    public void delete(String name, String hubId) {
        scenarioRepository.deleteByNameAndHubId(name, hubId);
    }

    private void establishBidirectionalRelationships(Scenario scenario) {
        if (scenario.getScenarioActions() != null) {
            for (Action action : scenario.getScenarioActions()) {
                action.setScenario(scenario);
            }
        }

        if (scenario.getScenarioConditions() != null) {
            for (ScenarioCondition condition : scenario.getScenarioConditions()) {
                condition.setScenario(scenario);
            }
        }
    }

    @Transactional
    public Scenario createScenario(Scenario scenarioData) {
        if (scenarioRepository.findByNameAndHubId(scenarioData.getName(), scenarioData.getHubId()).isPresent()) {
            throw new IllegalArgumentException("Сценарий с именем '" + scenarioData.getName() +
                    "' уже существует для hubId: " + scenarioData.getHubId());
        }

        Scenario scenario = new Scenario();
        scenario.setName(scenarioData.getName());
        scenario.setHubId(scenarioData.getHubId());
        if (scenarioData.getScenarioActions() != null) {
            scenario.setScenarioActions(scenarioData.getScenarioActions());
            establishBidirectionalRelationships(scenario);
        }
        if (scenarioData.getScenarioConditions() != null) {
            scenario.setScenarioConditions(scenarioData.getScenarioConditions());
            establishBidirectionalRelationships(scenario);
        }
        return scenarioRepository.save(scenario);
    }
}