package shop.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Relation("products")
public class ProductListResource extends ResourceSupport
{
	private final List<ProductResource> products = new ArrayList<>();


	public int getCount()
	{
		return products.size();
	}


	public void embed(ProductResource productResource)
	{
		products.add(productResource);
	}


	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("_embedded")
	public Map<String, Collection<ProductResource>> getEmbedded()
	{
		Map<String, Collection<ProductResource>> embedded = new HashMap<>(1);
		embedded.put("product", products);
		return embedded;
	}
}
