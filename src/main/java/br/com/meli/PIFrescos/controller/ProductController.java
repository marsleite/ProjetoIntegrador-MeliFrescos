package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.ProductDto;
import br.com.meli.PIFrescos.controller.forms.ProductForm;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Api de CRUD dos produtos
 * @author Juliano Alcione de Souza
*/


@RestController
@RequestMapping("/api/v1/fresh-products/products/")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductForm form){
        Product product = this.productService.createProduct(form.convert());
        return ResponseEntity.ok(new ProductDto(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> all(){
        List<Product> all = this.productService.listAllProducts();
        return ResponseEntity.ok(ProductDto.convertList(all));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Integer id, @RequestBody @Valid ProductForm form){
        form.setProductId(id);
        Product product = this.productService.updateProduct(form.convert());
        return ResponseEntity.ok(new ProductDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
