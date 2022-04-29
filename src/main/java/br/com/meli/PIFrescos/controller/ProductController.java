package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.OrderedProductDTO;
import br.com.meli.PIFrescos.controller.dtos.ProductDTO;
import br.com.meli.PIFrescos.controller.forms.ProductForm;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.service.BatchServiceImpl;
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

    @Autowired
    private BatchServiceImpl batchService;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductForm form){
        Product product = this.productService.createProduct(form.convert());
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> all(){
        List<Product> all = this.productService.listAllProducts();
        return ResponseEntity.ok(ProductDTO.convertList(all));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id, @RequestBody @Valid ProductForm form){
        form.setProductId(id);
        Product product = this.productService.updateProduct(form.convert());
        return ResponseEntity.ok(new ProductDTO(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    /**
     * endpoint para listar os produtos por categoria
     * @author Julio César Gama
     */

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getByType(@RequestParam String querytype){

        return new ResponseEntity(productService.listByType(querytype), HttpStatus.OK);
    }

    /**
     * endpoint para listar os batches de cada produto, recebendo a id do produto pela URI e ordenando por
     * Lote (L), CurrentQuantity(C) ou DueDate(F). Se não receber o segundo parametro, lista todas.
     * @author Ana Preis
     */
    @GetMapping("/batch/list")
    public ResponseEntity<List<OrderedProductDTO>> getByIdAndOrderBy(@RequestParam(required = false) Integer id,
                                                              @RequestParam(required = false) String orderBy){
        if(orderBy == null){ orderBy = ""; }
        List<Batch> batchList = batchService.findBatchesByProductOrderBy(id, orderBy);
        List<OrderedProductDTO> dtoList = OrderedProductDTO.convertList(batchList);

        return new ResponseEntity(dtoList, HttpStatus.OK);
    }
}
