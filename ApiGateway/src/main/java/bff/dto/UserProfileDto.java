package bff.dto;


public record UserProfileDto(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        int activeReservations,
        boolean vip
) {}

