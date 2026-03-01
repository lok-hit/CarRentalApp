package bff.dto;

import java.util.List;

public record DashboardDto(
        List<CarDto> availableCars,
        UserProfileDto userProfile
) {}

