package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.dtos.ProductDTO;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Api de CRUD dos produtos
 * @author Juliano Alcione de Souza
*/


@RestController
@RequestMapping("/fresh-products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductDTO dto){
        Product product = this.productService.createProduct(dto.convert());
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> all(){
        List<Product> all = this.productService.listAllProducts();
        return ResponseEntity.ok(ProductDTO.convertList(all));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id, @RequestBody @Valid ProductDTO dto){
        dto.setProductId(id);
        Product product = this.productService.updateProduct(dto.convert());
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getByType(@RequestParam String querytype){

        return new ResponseEntity(productService.listByType(querytype), HttpStatus.OK);
    }
}
