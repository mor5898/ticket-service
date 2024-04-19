package com.benevolo.mapper;

import com.benevolo.client.TicketTypeClient;
import com.benevolo.dto.TicketDTO;
import com.benevolo.entity.TicketEntity;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URI;
import java.util.List;

@ApplicationScoped
public class TicketMapper {

    @RestClient
    TicketTypeClient ticketTypeClient;
    private final JsonWebToken jsonWebToken;

    @Inject
    public TicketMapper(JsonWebToken jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    public List<TicketDTO> map(List<TicketEntity> tickets) {
        return tickets.stream().map(this::map).toList();
    }

    public TicketDTO map(TicketEntity ticket) {
        return new TicketDTO(ticket.getId(), ticket.getPublicId(), ticket.getPrice(), ticket.getTaxRate(), ticket.getStatus(), ticket.getBookedAt(), CustomerMapper.map(ticket.getCustomer()), ticketTypeClient.findById("Bearer " + jsonWebToken.getRawToken(), ticket.getTicketTypeId()));
    }

}
