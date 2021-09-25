package hompage.basic.domain.member;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {
    private Long id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
