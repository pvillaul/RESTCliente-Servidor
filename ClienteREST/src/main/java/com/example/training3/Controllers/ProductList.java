package com.example.training3.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import com.example.training3.Models.Product;
import com.example.training3.Models.Error;

@Controller
public class ProductList {
	@Autowired
    private RestTemplate restTemplate;

    @Value("${rest.server.url}")
    private String restServerUrl;
	
	@RequestMapping(value = {"/","/login"})
	public String login() {
		return "index";
	}
	
	@RequestMapping(value = {"/logout"})
	public String logout() {
		return "redirect:/login";
	}
	/*
	@GetMapping("/")
	public String route() {
		return "index";
	}*/
	@SuppressWarnings("unchecked")
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/list")
	public String list(Model model) {
		List<Product> products = restTemplate.getForObject(restServerUrl + "products",List.class);
		model.addAttribute("productos",products);
		return "list";
	}
	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/show/{id}")
	public String show(@PathVariable String id, Model model) {
		Product prod = restTemplate.getForObject(restServerUrl + "show/" + id,Product.class);
		model.addAttribute("product",prod);
		return "resume";
	}
	
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/remove/{id}")
	public String delete(@PathVariable String id, Model model) {
		if(restTemplate.getForObject(restServerUrl + "exists/" + id,Boolean.class) != null) {
			//productRepository.delete(productRepository.findByCode(id));
			restTemplate.exchange(restServerUrl + "remove", HttpMethod.DELETE, new HttpEntity<>(id),String.class);
			//List<Product> productList = productRepository.findAll();
			//model.addAttribute("productos",productList);
			return "redirect:/list";
		} else {
			Error error = new Error("Error al Eliminar","No se ha podido eliminar el producto selecionado","list","Atras");
			model.addAttribute("error",error);
			return "aviso";
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/addForm")
	public String addProd(@ModelAttribute Product product) {
		return "addForm";
	}
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String newProd(@ModelAttribute("product") Product product,BindingResult result, ModelMap model) {
		if(restTemplate.getForObject(restServerUrl + "exists/" + product.getCode(),Boolean.class) == null) {
			model.addAttribute("code", product.getCode());
	        model.addAttribute("name", product.getName());
	        model.addAttribute("description", product.getDescription());
	        model.addAttribute("price", product.getPrice());
	        //productos.put(product.getCode(), product);
	        //productRepository.save(new Product(product.getCode(),product.getName(),product.getDescription(),product.getPrice()));
	        restTemplate.exchange(restServerUrl + "product", HttpMethod.POST, new HttpEntity<>(product),Product.class);
	        return "resume";
		} else {
			Error error = new Error("Error al Crear el Producto","El codigo introducido coincide con un producto existente","addForm","Volver al formulario");
			model.addAttribute("error",error);
			return "aviso";
		}
	}
}