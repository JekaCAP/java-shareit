package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse addBooking(@Valid @RequestBody BookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse  findBookingById(@PathVariable Integer bookingId) {
        return bookingService.findBookingById(bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @PathVariable Integer bookingId,
                                                  @RequestParam boolean approved) {

        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping
    public List<BookingDtoResponse> findUsersBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                      @RequestParam(defaultValue = "ALL") String state) {

        return bookingService.findUsersBookings(userId, State.valueOf(state));
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> findUsersItemsBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                           @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findUsersItemsBookings(userId, State.valueOf(state));
    }
}

