package com.example.cosmo_cats_marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CosmoCatsMarketplaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmoCatsMarketplaceApplication.class, args);
	}

}
