package com.example.foodlabel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "foods",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id", "name"})
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20, message = "Name cannot exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String name;

    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "food_labels",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<Label> labels = new HashSet<>();

    public void addLabel(Label label) {
        this.labels.add(label);
        label.getFoods().add(this);
    }

    public void removeLabel(Label label) {
        this.labels.remove(label);
        label.getFoods().remove(this);
    }
}
