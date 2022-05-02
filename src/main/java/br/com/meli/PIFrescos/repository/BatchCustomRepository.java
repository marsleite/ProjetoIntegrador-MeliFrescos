package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.StorageType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * Cria uma query customizada e dinâmica para parâmetros não obrigatórios e retorna a Lista de Batches resultante.
 * @author Ana Preis
 */

@Repository
public class BatchCustomRepository {

    private final EntityManager em;

    public BatchCustomRepository(EntityManager em){
        this.em = em;
    }

    public List<Batch> find(LocalDate maxDueDate, StorageType storageType, String order){

        String query = "select b from Batch b ";
        String condicao = "where";

        if(maxDueDate != null){
            query += condicao + " b.dueDate <= :maxDueDate";
            condicao = " and ";
        }

        if(storageType != null){
            query += condicao + " b.product.productType = :storageType";
        }

        if (order != null) {
            if(order.equalsIgnoreCase("asc")){
                query += " order by b.dueDate asc";
            }
            if(order.equalsIgnoreCase("desc")){
                query += " order by b.dueDate desc";
            }
        }

        var q = em.createQuery(query, Batch.class);

        if(maxDueDate != null){
            q.setParameter("maxDueDate", maxDueDate);
        }

        if(storageType != null){
            q.setParameter("storageType", storageType);
        }

        return q.getResultList();
    }
}
