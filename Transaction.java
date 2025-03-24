package bankBackend.bank.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
//@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String transactionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public UserSpring getUser() {
        return user;
    }

    public void setUser(UserSpring user) {
        this.user = user;
    }

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserSpring user;
    public Transaction() {
    }
    public Transaction(BigDecimal amount, String transactionName, LocalDateTime transactionDate, UserSpring user) {
        this.amount = amount;
        this.transactionName = transactionName;
        this.transactionDate = transactionDate;
        this.user = user;
    }

}
