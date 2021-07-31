package com.quarkus.ifood.cadastro.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "prato")
public class Prato extends PanacheEntityBase {
	
	public Prato() {}
	
	public Prato(String nome, String descricao, Restaurante restaurante, BigDecimal preco) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.restaurante = restaurante;
		this.preco = preco;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
	
	public String nome;
	public String descricao;
	
	@ManyToOne
	public Restaurante restaurante;
	
	public BigDecimal preco;

}
