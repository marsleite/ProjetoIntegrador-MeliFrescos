package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Warehouse;
import br.com.meli.PIFrescos.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

  @Autowired
  private WarehouseRepository warehouseRepository;

  public List<Warehouse> getAllWarehouses() {
    return warehouseRepository.findAll();
  }
}
