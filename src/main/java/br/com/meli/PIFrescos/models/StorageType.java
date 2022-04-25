package br.com.meli.PIFrescos.models;

/**
 * @author Marcelo Leite
 *
 * Refactor
 * @author Julio César Gama
 * */
public enum StorageType {
    FRESH("FS"),
    REFRIGERATED("RF"),
    FROZEN("FF");

    private String value;

    StorageType(String value) {
        this.value = value;
    }

    public String getStorageType(){
        return value;
    }
}
