package bankBackend.bank.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Data
@AllArgsConstructor
@Table(name = "users")
public class UserSpring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private BigDecimal savings;
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSavings(BigDecimal savings) {
        this.savings = savings;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setPaymentBills(List<PaymentBill> paymentBills) {
        this.paymentBills = paymentBills;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getSavings() {
        return savings;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<PaymentBill> getPaymentBills() {
        return paymentBills;
    }

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PaymentBill> paymentBills = new ArrayList<>();

    public UserSpring() {
    }

    public UserSpring(String username, String password, BigDecimal savings, String status) {
        this.username = username;
        this.password = password;
        this.savings = savings;
        this.status = status;
    }
}