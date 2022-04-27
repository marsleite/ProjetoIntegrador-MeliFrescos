package br.com.meli.PIFrescos.models;

import lombok.Getter;

/**
 * @author Ana Preis
 */
public enum OrderStatus {
    OPENED,
    FINISHED;

    public static boolean isOrderStatus(String value) {
        for (OrderStatus os : OrderStatus.values()) {
            if ( os.name().equalsIgnoreCase(value) ) return true;
        }
        return false;
    }
}