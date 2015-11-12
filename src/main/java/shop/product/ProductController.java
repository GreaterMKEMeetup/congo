package shop.product;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Controller
@RequestMapping("/product")
public class ProductController
{
	@Autowired
	ProductService productService;


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<ProductResource> getProduct(@PathVariable("id") long id)
	{
		Product product = productService.findProduct(id);

		ResponseEntity<ProductResource> response = null;
		if (null != product)
		{
			ProductResource productResource = createProductResource(product);
			response = new ResponseEntity<>(productResource, HttpStatus.OK);
		}
		else
		{
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return response;
	}


	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<ProductListResource> getProductList()
	{
		Collection<Product> products = productService.findAllProducts();
		ProductListResource productListResource = createProductListResource(products);
		return new ResponseEntity<>(productListResource, HttpStatus.OK);
	}


	private ProductListResource createProductListResource(Collection<Product> products)
	{
		ProductListResource productListResource = new ProductListResource();
		for(Product product : products)
		{
			ProductResource productResource = createProductResource(product);
			productListResource.embed(productResource);
		}

		productListResource.add(linkTo(methodOn(ProductController.class).getProductList()).withSelfRel());
		productListResource.add(new Link(new UriTemplate(String.format("%s/{%s}", linkTo(ProductController.class), "productId")), "product"));

		return productListResource;
	}


	private ProductResource createProductResource(Product product)
	{
		ProductResource productResource = new ProductResource(product.getName(), product.getPrice(), product.getDescription());

		productResource.add(linkTo(methodOn(ProductController.class).getProduct(product.getId())).withSelfRel());
		productResource.add(linkTo(methodOn(ProductController.class).getProductList()).withRel("products"));

		return productResource;
	}
}
