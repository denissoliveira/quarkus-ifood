package com.quarkus.ifood.cadastro;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.quarkus.ifood.cadastro.model.Prato;
import com.quarkus.ifood.cadastro.model.Restaurante;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Restaurantes")
public class RestauranteResource {

    @GET
    public Response buscar() {
        return Response.ok(Restaurante.listAll()).build();
    }
    
    @POST
    @Transactional
    public Response adicionar(Restaurante dto) {
    	dto.persist();
        return Response.status(Status.CREATED).build();
    }
    
    @PUT
    @Path("{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, Restaurante dto) {
    	Optional<Restaurante> restauranteOp = obterRestaurante(id);
        var restaurante = restauranteOp.get();
        restaurante.nome = dto.nome;
        restaurante.persist();
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
    	Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
    	restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {throw new NotFoundException();});
    	return Response.ok().build();
    }
    
    @GET
    @Tag(name = "Pratos")
    @Path("{idRestaurante}/pratos")
    public Response buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
    	Optional<Restaurante> restauranteOp = obterRestaurante(idRestaurante);
        return Response.ok(Prato.list("restaurante", restauranteOp.get())).build();
    }
    
    @POST
    @Tag(name = "Pratos")
    @Path("{idRestaurante}/pratos")
    @Transactional
    public Response adicionarPratos(@PathParam("idRestaurante") Long idRestaurante, Prato dto) {
    	Optional<Restaurante> restauranteOp = obterRestaurante(idRestaurante);
    	Prato prato = new Prato(dto.nome, dto.descricao, dto.restaurante, dto.preco);
    	prato.persist();
        return Response.status(Status.CREATED).build();
    }
    
    @PUT
    @Tag(name = "Pratos")
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    public Response atualizarPratos(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato dto) {
    	Optional<Restaurante> restauranteOp = obterRestaurante(idRestaurante);
    	Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        if (pratoOp.isEmpty()) {
			throw new NotFoundException();
		}
        var prato = pratoOp.get();
        prato.preco = dto.preco;
        prato.persist();
        return Response.ok().build();
    }
    
    @DELETE
    @Tag(name = "Pratos")
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    public Response deletarPratos(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
    	Optional<Restaurante> restauranteOp = obterRestaurante(idRestaurante);
    	Optional<Prato> pratoOp = Prato.findByIdOptional(id);
    	pratoOp.ifPresentOrElse(Prato::delete, () -> {throw new NotFoundException();});
    	return Response.ok().build();
    }
    
    private Optional<Restaurante> obterRestaurante(Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}
		return restauranteOp;
	}
    
}