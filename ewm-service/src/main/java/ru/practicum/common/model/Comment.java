package ru.practicum.common.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.user.models.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime postedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private Event event;
}
