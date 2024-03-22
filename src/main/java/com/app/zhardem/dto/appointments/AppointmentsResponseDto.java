package com.app.zhardem.dto.appointments;

public record AppointmentsResponseDto(
        String time,
        boolean disable
) {
    public void setDisable(boolean disable) {
        disable = disable;
    }

    public boolean isDisable() {
        return disable;
    }
}
