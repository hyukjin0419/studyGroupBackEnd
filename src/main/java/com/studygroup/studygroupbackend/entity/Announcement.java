package com.studygroup.studygroupbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announcement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(nullable = false)
    private LocalDateTime publishDate;

    @Column(nullable = true)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean important;

    private Announcement(String title, String content, Member author, 
                        LocalDateTime publishDate, LocalDateTime expiryDate, boolean important) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.publishDate = publishDate;
        this.expiryDate = expiryDate;
        this.important = important;
    }

    public static Announcement of(String title, String content, Member author, 
                                 LocalDateTime publishDate, LocalDateTime expiryDate, boolean important) {
        return new Announcement(title, content, author, publishDate, expiryDate, important);
    }

    public void update(String title, String content, LocalDateTime publishDate, LocalDateTime expiryDate, boolean important) {
        this.title = title;
        this.content = content;
        if (publishDate != null) {
            this.publishDate = publishDate;
        }
        this.expiryDate = expiryDate;
        this.important = important;
    }
}
