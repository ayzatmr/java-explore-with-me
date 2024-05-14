package ru.practicum.admin.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.common.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compilations")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    boolean pinned;

    @ManyToMany(mappedBy = "compilations")
    @ToString.Exclude
    List<Event> events = new ArrayList<>();
}
