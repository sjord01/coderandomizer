package coderandomizer.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "codes")
public class CodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=64)
    private String code;

    @Column(nullable=false)
    private Boolean cleaned = false;

    @Column(nullable=false)
    private Instant createdAt = Instant.now();

    public CodeEntity() {}

    public CodeEntity(String code) {
        this.code = code;
        this.cleaned = isAlphanumeric(code);
    }

    public static boolean isAlphanumeric(String s) {
        return s != null && s.matches("^[a-z1-9]+$");
    }

    // getters/setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) {
        this.code = code;
        this.cleaned = isAlphanumeric(code);
    }

    public Boolean getCleaned() { return cleaned; }
    public void setCleaned(Boolean cleaned) { this.cleaned = cleaned; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}