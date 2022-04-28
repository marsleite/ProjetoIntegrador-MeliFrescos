package br.com.meli.PIFrescos.models;

import br.com.meli.PIFrescos.controller.forms.ProductCartForm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ana Preis
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User user;
    private LocalDate date = LocalDate.now();
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.OPENED;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProductsCart> cartList;

    public PurchaseOrder(User user, List<ProductsCart> cartList) {
        this.user = user;
        this.cartList = cartList;
    }
}