package ru.practicum.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateDto {

    List<UserRequestDto> confirmedRequests = new ArrayList<>();

    List<UserRequestDto> rejectedRequests = new ArrayList<>();

    public void addConfirmedRequest(UserRequestDto requestDto) {
        confirmedRequests.add(requestDto);
    }

    public void addRejectedRequest(UserRequestDto requestDto) {
        rejectedRequests.add(requestDto);
    }
}
