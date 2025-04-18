package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDto {
    private Integer id;
    @NotNull
    @JsonFormat
    private LocalDateTime start;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;
    @NotNull
    private Integer itemId;
    private Integer bookerId;
    private Status status;
}