package ru.practicum.common.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.admin.model.Compilation;
import ru.practicum.common.enums.EventState;
import ru.practicum.user.models.Location;
import ru.practicum.user.models.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @ManyToMany
    @JoinTable(
            name = "event_compilation",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id")
    )
    @ToString.Exclude
    final List<Compilation> compilations = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private final List<Comment> comments = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    Category category;

    LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    User initiator;

    Boolean paid;

    String description;

    String title;

    @Column(name = "participant_limit")
    int participantLimit;

    @Column(name = "request_moderation")
    boolean requestModeration;

    @Enumerated(EnumType.STRING)
    EventState state;

    @CreationTimestamp
    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    @ToString.Exclude
    Location location;

    @Column(name = "confirmed_requests")
    int confirmedRequests;

    long views;

    public int addParticipant() {
        return ++confirmedRequests;
    }

    public void addToCompilation(Compilation compilation) {
        compilations.add(compilation);
    }

    public void addCommentToEvent(Comment comment) {
        comments.add(comment);
    }
}
