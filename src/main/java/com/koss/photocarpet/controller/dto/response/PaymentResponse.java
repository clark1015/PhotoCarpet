package com.koss.photocarpet.controller.dto.response;

import com.koss.photocarpet.domain.photo.Photo;
import com.koss.photocarpet.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private User buyer;
    private User seller;
    private Photo photo;


}
