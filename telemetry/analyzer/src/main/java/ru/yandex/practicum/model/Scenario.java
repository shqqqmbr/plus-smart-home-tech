package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "scenarioId")
@Builder
@NamedEntityGraph(
        name = "Scenario.withConditionsAndActions",
        attributeNodes = {
                @NamedAttributeNode("scenarioConditions"),
                @NamedAttributeNode("scenarioActions")
        }
)
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "scenarios")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long scenarioId;

    @Column(name = "name")
    private String name;

    @Column(name = "hub_id")
    private String hubId;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ScenarioCondition> scenarioConditions;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Action> scenarioActions;

    // Добавьте эти методы для правильного управления связями:

    public void addCondition(ScenarioCondition condition) {
        if (this.scenarioConditions == null) {
            this.scenarioConditions = new HashSet<>();
        }
        condition.setScenario(this); // Устанавливаем обратную связь
        this.scenarioConditions.add(condition);
    }

    public void addAction(Action action) {
        if (this.scenarioActions == null) {
            this.scenarioActions = new HashSet<>();
        }
        action.setScenario(this); // Устанавливаем обратную связь
        this.scenarioActions.add(action);
    }

    public void removeCondition(ScenarioCondition condition) {
        if (this.scenarioConditions != null) {
            this.scenarioConditions.remove(condition);
            condition.setScenario(null);
        }
    }

    public void removeAction(Action action) {
        if (this.scenarioActions != null) {
            this.scenarioActions.remove(action);
            action.setScenario(null);
        }
    }
}