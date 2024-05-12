package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.admin.repository.LocationRepository;
import ru.practicum.admin.repository.UserRepository;
import ru.practicum.common.dto.EventFullDto;
import ru.practicum.common.dto.EventShortDto;
import ru.practicum.common.dto.EventUpdateRequest;
import ru.practicum.common.enums.EventRequestStatus;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.exception.AlreadyExistException;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.exception.ValidationException;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.common.mapper.UserRequestMapper;
import ru.practicum.common.model.Category;
import ru.practicum.common.model.Constants;
import ru.practicum.common.model.Event;
import ru.practicum.common.pagination.CustomPageRequest;
import ru.practicum.user.dto.EventRequestStatusUpdateDto;
import ru.practicum.user.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.user.dto.NewEventDto;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.models.Location;
import ru.practicum.user.models.NewEvent;
import ru.practicum.user.models.User;
import ru.practicum.user.models.UserRequest;
import ru.practicum.user.repository.UserEventRepository;
import ru.practicum.user.repository.UserRequestsRepository;

import java.util.List;
import java.util.stream.IntStream;

import static ru.practicum.common.enums.EventRequestStatus.CONFIRMED;
import static ru.practicum.common.enums.EventRequestStatus.REJECTED;
import static ru.practicum.common.enums.EventState.PENDING;

@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService {

    private final UserEventRepository userEventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final UserRequestsRepository userRequestsRepository;

    private final UserRequestMapper userRequestMapper;

    private final EventMapper eventMapper;

    private static int validateUserRequest(Event event) {
        int participantLimit = event.getParticipantLimit();
        if (participantLimit == 0) {
            throw new ValidationException("Participant limit is not set");
        }

        if (!event.isRequestModeration()) {
            throw new ValidationException("Moderation is not required");
        }

        if (event.getConfirmedRequests() == participantLimit) {
            throw new AlreadyExistException("The participant limit has been reached");
        }
        return participantLimit;
    }

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) {
        NewEvent newEvent = eventMapper.toModel(newEventDto);
        User initiator = getUser(userId);
        Category category = categoryRepository.findById(newEvent.getCategoryId())
                .orElseThrow(() -> new ObjectNotFoundException("Category is not found"));
        Location eventLocation = locationRepository.save(newEvent.getLocation());
        Event fullEvent = eventMapper.toFullEvent(newEvent, category, initiator, PENDING, eventLocation);
        return eventMapper.toDto(userEventRepository.save(fullEvent));
    }

    @Override
    public List<EventShortDto> findAllEvents(Long userId, int from, int size) {
        getUser(userId);
        Pageable pageRequest = CustomPageRequest.of(from, size, Constants.ID_SORTING);
        return eventMapper.toShortDtoList(userEventRepository.findByInitiatorId(userId, pageRequest));
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        getUser(userId);
        Event fullEvent = getUserEvent(eventId, userId);
        return eventMapper.toDto(fullEvent);
    }

    @Override
    public EventFullDto patchEvent(Long userId, Long eventId, EventUpdateRequest eventDto) {
        getUser(userId);
        Event eventToUpdate = getUserEvent(eventId, userId);

        if (eventToUpdate.getState().equals(EventState.PUBLISHED)) {
            throw new AlreadyExistException("Event can not be modified");
        }
        changeEventState(eventDto, eventToUpdate);
        eventMapper.updateEvent(eventDto, eventToUpdate);
        return eventMapper.toDto(userEventRepository.save(eventToUpdate));
    }

    @Override
    public List<UserRequestDto> getRequestsByUser(Long userId, Long eventId) {
        getUser(userId);
        getUserEvent(eventId, userId);
        return userRequestMapper.toDtoList(userRequestsRepository.findAllByEventId(eventId));
    }

    @Override
    public EventRequestStatusUpdateDto patchUserRequest(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequestDto statusUpdate) {
        getUser(userId);
        Event event = getUserEvent(eventId, userId);
        int participantLimit = validateUserRequest(event);
        List<Long> requestIds = statusUpdate.getRequestIds();
        List<UserRequest> userRequests = userRequestsRepository.findAllByIdIn(requestIds);
        int lastConfirmedRequest = 0;
        EventRequestStatusUpdateDto eventRequestStatusUpdate = new EventRequestStatusUpdateDto();
        lastConfirmedRequest = updateEventStatus(
                statusUpdate,
                userRequests,
                eventRequestStatusUpdate,
                lastConfirmedRequest,
                event,
                participantLimit);
        rejectRemainingRequests(lastConfirmedRequest, userRequests, eventRequestStatusUpdate);
        return eventRequestStatusUpdate;
    }

    @Override
    public UserRequestDto addUserRequestToEvent(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEventById(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("It is not allowed not make request to your own event");
        }
        userRequestsRepository
                .findByRequesterIdAndEventId(userId, eventId)
                .ifPresent(userRequest -> {
                    throw new AlreadyExistException("User request is already exists");
                });
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Event is not published");
        }
        UserRequest userRequest = createParticipantRequest(user, event);
        return userRequestMapper.toDto(userRequestsRepository.save(userRequest));
    }

    @Override
    public List<UserRequestDto> findUserRequests(Long userId) {
        getUser(userId);
        return userRequestMapper.toDtoList(userRequestsRepository.findAllByRequesterId(userId));
    }

    @Override
    public UserRequestDto cancelUserRequest(Long userId, Long requestId) {
        getUser(userId);
        UserRequest userRequest = userRequestsRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("User request is not found"));
        if (!userRequest.getRequester().getId().equals(userId)) {
            throw new ValidationException("User is not authorized to cancel request");
        }
        userRequest.setStatus(EventRequestStatus.CANCELED);
        return userRequestMapper.toDto(userRequest);
    }

    private int updateEventStatus(
            EventRequestStatusUpdateRequestDto statusUpdate,
            List<UserRequest> userRequests,
            EventRequestStatusUpdateDto eventRequestStatusUpdate,
            int lastConfirmedRequest,
            Event event,
            int participantLimit) {
        for (UserRequest userRequest : userRequests) {
            if (!userRequest.getStatus().equals(EventRequestStatus.PENDING)) {
                throw new ValidationException("Can not update event status");
            }
            userRequest.setStatus(statusUpdate.getStatus());
            userRequestsRepository.save(userRequest);
            if (statusUpdate.getStatus().equals(CONFIRMED)) {
                eventRequestStatusUpdate.addConfirmedRequest(userRequestMapper.toDto(userRequest));
                lastConfirmedRequest++;
                int incrementedParticipants = event.addParticipant();
                userEventRepository.save(event);
                if (incrementedParticipants == participantLimit) {
                    break;
                }
            }
        }
        return lastConfirmedRequest;
    }

    private void rejectRemainingRequests(
            int lastConfirmedRequest,
            List<UserRequest> userRequests,
            EventRequestStatusUpdateDto eventRequestStatusUpdate) {
        IntStream.range(lastConfirmedRequest, userRequests.size())
                .mapToObj(userRequests::get)
                .forEach(userRequest -> {
                    userRequest.setStatus(REJECTED);
                    userRequestsRepository.save(userRequest);
                    eventRequestStatusUpdate.addRejectedRequest(userRequestMapper.toDto(userRequest));
                });
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User is not found."));
    }

    private Event getUserEvent(Long eventId, Long userId) {
        return userEventRepository.findByEventIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Event is not found"));
    }

    private Event getEventById(Long eventId) {
        return userEventRepository.getEventById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event is not found"));
    }

    private void changeEventState(EventUpdateRequest updateEvent, Event eventToUpdate) {
        if (updateEvent.getStateAction() == null) {
            return;
        }
        switch (updateEvent.getStateAction()) {
            case CANCEL_REVIEW:
                eventToUpdate.setState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                eventToUpdate.setState(EventState.PENDING);
                break;
        }
    }

    private UserRequest createParticipantRequest(User user, Event event) {
        UserRequest userRequest = UserRequest.builder()
                .requester(user)
                .event(event)
                .build();
        if (event.getConfirmedRequests() == event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ValidationException("Participant limit is exceeded");
        }
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            userRequest.setStatus(EventRequestStatus.CONFIRMED);
            event.addParticipant();
            userEventRepository.save(event);
        } else {
            userRequest.setStatus(EventRequestStatus.PENDING);
        }
        return userRequest;
    }
}
