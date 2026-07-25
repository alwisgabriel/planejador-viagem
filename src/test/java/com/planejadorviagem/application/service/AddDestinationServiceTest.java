package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.in.AddDestinationCommand;
import com.planejadorviagem.application.port.out.DestinationRepository;
import com.planejadorviagem.domain.model.Destination;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddDestinationServiceTest {

    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private AddDestinationService addDestinationService;

    @Test
    void shouldAddDestinationToTrip() {
        UUID tripId = UUID.randomUUID();
        AddDestinationCommand command = new AddDestinationCommand(tripId, "São Paulo", "Brasil", 1);

        addDestinationService.add(command);

        ArgumentCaptor<Destination> captor = ArgumentCaptor.forClass(Destination.class);
        verify(destinationRepository).save(captor.capture());

        Destination saved = captor.getValue();
        assertThat(saved.getTripId()).isEqualTo(tripId);
        assertThat(saved.getCity()).isEqualTo("São Paulo");
        assertThat(saved.getCountry()).isEqualTo("Brasil");
        assertThat(saved.getDisplayOrder()).isEqualTo(1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void shouldRejectBlankCity() {
        AddDestinationCommand command = new AddDestinationCommand(UUID.randomUUID(), "", "Brasil", 1);

        assertThatThrownBy(() -> addDestinationService.add(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A cidade não pode estar em branco");
    }

    @Test
    void shouldRejectBlankCountry() {
        AddDestinationCommand command = new AddDestinationCommand(UUID.randomUUID(), "São Paulo", "", 1);

        assertThatThrownBy(() -> addDestinationService.add(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O país não pode estar em branco");
    }

    @Test
    void shouldRejectInvalidDisplayOrder() {
        AddDestinationCommand command = new AddDestinationCommand(UUID.randomUUID(), "São Paulo", "Brasil", 0);

        assertThatThrownBy(() -> addDestinationService.add(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A ordem deve ser maior que zero");
    }
}
