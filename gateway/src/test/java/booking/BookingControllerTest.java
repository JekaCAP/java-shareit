package booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Test
    void createBooking_EqualStartEnd_ReturnsBadRequest() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        BookingDtoRequest request = BookingDtoRequest.builder()
                .start(now)
                .end(now)
                .itemId(1L)
                .build();

        mockMvc.perform(post("/bookings")
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_EndBeforeStart_ReturnsBadRequest() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        BookingDtoRequest request = BookingDtoRequest.builder()
                .start(now)
                .end(now.minusDays(1))
                .itemId(1L)
                .build();

        mockMvc.perform(post("/bookings")
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_EndIsNull_ReturnsBadRequest() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        BookingDtoRequest request = BookingDtoRequest.builder()
                .start(now)
                .end(null)
                .itemId(1L)
                .build();

        mockMvc.perform(post("/bookings")
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnBookings_InvalidState_ReturnsBadRequest() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/bookings")
                        .header(USER_HEADER, 1L)
                        .param("state", "SSS")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }
}
