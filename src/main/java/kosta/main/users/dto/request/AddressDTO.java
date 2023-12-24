package kosta.main.users.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String jibunAddr;
    private String roadAddr;
    private String zcode;
}
