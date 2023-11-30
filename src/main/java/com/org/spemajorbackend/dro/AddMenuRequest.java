package com.org.spemajorbackend.dro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMenuRequest {
    private String day;
    private String breakfast;
    private String lunch;
    private String dinner;
}
