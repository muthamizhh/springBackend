package bankBackend.bank.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Table(name = "payment_bills")
public class PaymentBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String billName;
    private BigDecimal amount;

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    private boolean paid = false;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dueDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public UserSpring getUser() {
        return user;
    }

    public void setUser(UserSpring user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserSpring user;
    public PaymentBill() {
    }
    // ✅ Constructor without ID (JPA will auto-generate it)
    public PaymentBill(String billName, BigDecimal amount, LocalDateTime dueDate, UserSpring user) {
        this.billName = billName;
        this.amount = amount;
        this.dueDate = dueDate;
        this.user = user;
        this.paid = false;
    }
}
