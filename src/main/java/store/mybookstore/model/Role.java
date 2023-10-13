package store.mybookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE roles SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleName roleName;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN
    }
}
