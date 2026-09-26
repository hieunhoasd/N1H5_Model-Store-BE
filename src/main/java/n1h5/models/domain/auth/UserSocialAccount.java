package n1h5.models.domain.auth;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "User_Social_Accounts",
    schema = "auth",
    uniqueConstraints = {
        @UniqueConstraint(name = "UQ_Provider_User", columnNames = {"Provider_Name", "Provider_User_Id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Social_Id")
    private Integer socialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "User_Id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "FK_Social_Users")
    )
    private Users user;

    @Column(name = "Provider_Name", nullable = false, length = 20)
    private String providerName;

    @Column(name = "Provider_User_Id", nullable = false)
    private String providerUserId;

    @Column(name = "Email")
    private String email;

    @Column(name = "Created_At", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "Updated_At")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}